package com.devexperts.dxlab.lincheck.transformers;

import com.devexperts.dxlab.lincheck.strategy.Strategy;
import com.devexperts.dxlab.lincheck.strategy.StrategyHolder;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import static org.objectweb.asm.Opcodes.ALOAD;

/**
 * MethodTransformer to insert StrategyHolder.currentStrategy.onSharedVariable method call
 *
 * @see Strategy
 * @see StrategyHolder
 */
class BeforeSharedVariableMethodTransformer extends GeneratorAdapter {

    private static final Type STRATEGYHOLDER_TYPE = Type.getType(StrategyHolder.class);
    private static final Method STRATEGYHOLDER_GET = new Method("getCurrentStrategy", Type.getType(Strategy.class), new Type[]{});
    private static final Type STRATEGY_ITF_TYPE = Type.getType(Strategy.class);
    private static final Method STRATEGY_ITF_METHOD = new Method("onSharedVariableAccess", Type.VOID_TYPE, new Type[]{Type.INT_TYPE});

    private final String className;
    private final String methodName;
    private final String methodDesc;

    private final LocationManager lm = LocationManager.getInstance();

    private int line;

    BeforeSharedVariableMethodTransformer(int api, MethodVisitor mv, int access, String name, String desc, String className) {
        super(api, mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.methodDesc = desc;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.line = line;
        mv.visitLineNumber(line, start);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        // TODO add comments
        // If not ALOAD 0 (this) TODO I cannot understand such comments (it is the same as the code)
        if (!(opcode == ALOAD && var == 0)) {
            // TODO use instruction number instead of line number
            int id = lm.getLocationId(className, methodName, methodDesc, line);
            invokeStatic(STRATEGYHOLDER_TYPE, STRATEGYHOLDER_GET);
            push(id);
            invokeInterface(STRATEGY_ITF_TYPE, STRATEGY_ITF_METHOD);
        }
        mv.visitVarInsn(opcode, var);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        // TODO add comments
        int id = lm.getLocationId(className, methodName, methodDesc, line);
        invokeStatic(STRATEGYHOLDER_TYPE, STRATEGYHOLDER_GET);
        push(id);
        invokeInterface(STRATEGY_ITF_TYPE, STRATEGY_ITF_METHOD);
        mv.visitFieldInsn(opcode, owner, name, desc);
    }
}
