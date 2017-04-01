package com.devexperts.dxlab.lincheck;

import com.devexperts.dxlab.lincheck.transformers.BeforeSharedVariableClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loader to load and transform classes.
 * Can delegate some classes to parent ClassLoader.
 */
class ExecutionClassLoader extends ClassLoader {
    private final Map<String, Class<?>> cash = new ConcurrentHashMap<>();
    private final Map<String, byte[]> resources = new ConcurrentHashMap<>();
    private final String testClassName; // TODO we should transform test class (it contains algorithm logic)

    ExecutionClassLoader(String testClassName) {
        this.testClassName = testClassName;
    }

    /**
     * Transform class if it is not in excluded list and load it by this Loader
     * else delegate load to parent loader
     *
     * @param name name of class
     * @return transformed class loaded by this loader or by parent loader
     * @throws ClassNotFoundException if IOException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Print loading class
        // System.out.println("Loading: " + name);

        // Load transformed class from cash if it exists
        Class result = cash.get(name);
        if (result != null)
            return result;
        // Do not transform some classes
        if (shouldIgnoreClass(name)) {
            // Print delegated class
//            System.out.println("Loaded by super:" + name);
            return super.loadClass(name);
        }
        //Transform and save class
        try {
            // Print transforming class
//             System.out.println("Loaded by exec:" + name);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new BeforeSharedVariableClassVisitor(cw);
            ClassReader cr = new ClassReader(name);
            // Ignore TestClass
            // TODO transform test class too. Use DummyStrategy (and write it) during new instance constructing
            if (name.equals(testClassName)) {
                cr.accept(cw, ClassReader.SKIP_FRAMES);
            } else {
                cr.accept(cv, ClassReader.SKIP_FRAMES);
            }
            // Get transformed bytecode
            byte[] resultBytecode = cw.toByteArray();
            result = defineClass(name, resultBytecode, 0, resultBytecode.length);
            // Save it to cash and resources
            resources.put(name, resultBytecode);
            cash.put(name, result);
            return result;
        } catch (SecurityException e) {
            return super.loadClass(name);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e); // TODO write more helpful message
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        byte[] result = resources.get(name);
        if (result != null) {
            return new ByteArrayInputStream(result);
        } else {
            return super.getResourceAsStream(name);
        }
    }

    /***
     * Check if class should be ignored for transforming and defining
     * @param className checking class name
     * @return result of checking class
     */
    private static boolean shouldIgnoreClass(String className) {
        return
                className == null ||
                        className.startsWith("com.devexperts.dxlab.lincheck.") &&
                                !className.startsWith("com.devexperts.dxlab.lincheck.tests.") &&
                                !className.startsWith("com.devexperts.dxlab.lincheck.libtest.")
                        ||
                        className.startsWith("sun.") ||
                        className.startsWith("java.");
                        // TODO let's transform java.util.concurrent
    }

    Class<? extends TestThreadExecution> defineTestThreadExecution(String className, byte[] bytecode) {
        return (Class<? extends TestThreadExecution>) super.defineClass(className, bytecode, 0, bytecode.length);
    }

}
