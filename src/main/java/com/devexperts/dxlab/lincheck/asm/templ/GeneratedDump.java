package com.devexperts.dxlab.lincheck.asm.templ;

import com.devexperts.dxlab.lincheck.util.MethodParameter;
import jdk.internal.org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;


public class GeneratedDump implements Opcodes {

    private static String GetBrackets(String s){
        StringBuilder bracketBuilder = new StringBuilder();
        for (char i:s.toCharArray()
             ) {
            if (i == '[')
                bracketBuilder.append("[");
        }
        return bracketBuilder.toString();
    }
    public static byte[] dump(
            String generatedClassName, // "com/devexperts/dxlab/lincheck/asmtest/Generated10"
            String testFieldName, // queue
            String testClassName, // com/devexperts/dxlab/lincheck/tests/custom/QueueTestAnn
            String[] methodNames,
            MethodParameter[][] parameters,
            String[] methodTypes
    ) throws Exception {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

//        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/devexperts/dxlab/lincheck/asmtest/Generated10", null, "com/devexperts/dxlab/lincheck/asmtest/Generated", null);
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, generatedClassName, null, "com/devexperts/dxlab/lincheck/asm/Generated", null);

        {
//            fv = cw.visitField(ACC_PUBLIC, "queue", "Lcom/devexperts/dxlab/lincheck/tests/custom/QueueTestAnn;", null, null);
            fv = cw.visitField(ACC_PUBLIC, testFieldName, "L" + testClassName + ";", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
//            mv.visitMethodInsn(INVOKESPECIAL, "com/devexperts/dxlab/lincheck/asmtest/Generated", "<init>", "()V", false);
            mv.visitMethodInsn(INVOKESPECIAL, "com/devexperts/dxlab/lincheck/asm/Generated", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
//            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lcom/devexperts/dxlab/lincheck/tests/custom/QueueTestAnn;)V", null, null);
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(L" + testClassName + ";)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
//            mv.visitMethodInsn(INVOKESPECIAL, "com/devexperts/dxlab/lincheck/asmtest/Generated", "<init>", "()V", false);
            mv.visitMethodInsn(INVOKESPECIAL, "com/devexperts/dxlab/lincheck/asm/Generated", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
//            mv.visitFieldInsn(PUTFIELD, "com/devexperts/dxlab/lincheck/asmtest/Generated10", "queue", "Lcom/devexperts/dxlab/lincheck/tests/custom/QueueTestAnn;");
            mv.visitFieldInsn(PUTFIELD, generatedClassName, testFieldName, "L" + testClassName + ";");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "process", "([Lcom/devexperts/dxlab/lincheck/util/Result;[[Lcom/devexperts/dxlab/lincheck/util/MethodParameter;[ILjava/util/concurrent/Phaser;)V", null, null);
            mv.visitCode();
            Label[][] labelsForMethod = new Label[methodNames.length][3];
            for (int i = 0; i < labelsForMethod.length; i++) {
                for (int j = 0; j < labelsForMethod[i].length; j++) {
                    labelsForMethod[i][j] = new Label();
                }
                mv.visitTryCatchBlock(labelsForMethod[i][0], labelsForMethod[i][1], labelsForMethod[i][2], "java/lang/Exception");
            }
            Label l12 = new Label();
            int border = 5;
            int[][] borders = new int[methodNames.length][];
            String[] methods = new String[methodNames.length];
            List<Object> parList = new ArrayList<>();
            parList.add(generatedClassName);
            parList.add("[Lcom/devexperts/dxlab/lincheck/util/Result;");
            parList.add("[[Lcom/devexperts/dxlab/lincheck/util/MethodParameter;");
            parList.add("[I");
            parList.add("java/util/concurrent/Phaser");
            for (int i = 0; i < methodNames.length; i++) {
                borders[i] = new int[parameters[i].length];
                StringBuilder type = new StringBuilder();
                type.append("(");
                for (int j = 0; j < parameters[i].length; j++) {
                    String s = parameters[i][j].type.replace(".", "/");
                    String brackets = GetBrackets(s);
                    s = s.replace("[]", "");
                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitIntInsn(SIPUSH, i);
                    mv.visitInsn(AALOAD);
                    mv.visitIntInsn(SIPUSH, j);
                    mv.visitInsn(AALOAD);
                    mv.visitFieldInsn(GETFIELD, "com/devexperts/dxlab/lincheck/util/MethodParameter", "value", "Ljava/lang/Object;");

                    switch (s){
                        case "boolean":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "Z");
                                mv.visitTypeInsn(CHECKCAST, brackets + "Z");
                                parList.add(brackets + "Z");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                                mv.visitVarInsn(ISTORE, border);
                                parList.add(Opcodes.INTEGER);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("Z");
                            break;
                        case "int":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "I");
                                mv.visitTypeInsn(CHECKCAST, brackets + "I");
                                parList.add(brackets + "I");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                                mv.visitVarInsn(ISTORE, border);
                                parList.add(Opcodes.INTEGER);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("I");
                            break;
                        case "short":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "S");
                                mv.visitTypeInsn(CHECKCAST, brackets + "S");
                                parList.add(brackets + "S");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                                mv.visitVarInsn(ISTORE, border);
                                parList.add(Opcodes.INTEGER);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("S");
                            break;
                        case "long":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "J");
                                mv.visitTypeInsn(CHECKCAST, brackets + "J");
                                parList.add(brackets + "J");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                                borders[i][j] = border;
                                border += 1;

                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                                mv.visitVarInsn(LSTORE, border);
                                borders[i][j] = border;
                                border += 2;
                                parList.add(Opcodes.LONG);
                            }
                            type.append("J");
                            break;
                        case "byte":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "B");
                                mv.visitTypeInsn(CHECKCAST, brackets + "B");
                                parList.add(brackets + "B");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Byte");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                                mv.visitVarInsn(ISTORE, border);
                                parList.add(Opcodes.INTEGER);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("B");
                            break;
                        case "float":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "F");
                                mv.visitTypeInsn(CHECKCAST, brackets + "F");
                                parList.add(brackets + "F");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                                mv.visitVarInsn(FSTORE, border);
                                parList.add(Opcodes.FLOAT);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("F");
                            break;
                        case "double":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "D");
                                mv.visitTypeInsn(CHECKCAST, brackets + "D");
                                parList.add(brackets + "D");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                                borders[i][j] = border;
                                border += 1;
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                                mv.visitVarInsn(DSTORE, border);
                                borders[i][j] = border;
                                border += 2;
                                parList.add(Opcodes.DOUBLE);
                            }
                            type.append("D");
                            break;
                        case "char":
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "C");
                                mv.visitTypeInsn(CHECKCAST, brackets + "C");
                                parList.add(brackets + "C");
                                type.append(brackets);
                                mv.visitVarInsn(ASTORE, border);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                                mv.visitVarInsn(ISTORE, border);
                                parList.add(Opcodes.INTEGER);
                            }
                            borders[i][j] = border;
                            border += 1;
                            type.append("C");
                            break;
                        default:
                            if (!brackets.equals("")){
                                mv.visitTypeInsn(CHECKCAST, brackets + "L"+s+";");
                                mv.visitTypeInsn(CHECKCAST, brackets + "L"+s+";");
                                parList.add(brackets + s);
                                type.append(brackets);
                            } else {
                                mv.visitTypeInsn(CHECKCAST, s);
                                parList.add(s);
                            }
                            mv.visitVarInsn(ASTORE, border);
                            borders[i][j] = border;
                            border += 1;
                            type.append("L"+s+";");
                            break;
                    }



                }
                String s = methodTypes[i].replace(".", "/");
                String brackets = GetBrackets(s);
                s = s.replace("[]", "");
                type.append(")");
                switch (methodTypes[i]){
                    case "void":
                        type.append("V");
                        break;
                    case "boolean":
                        type.append("Z");
                        break;
                    case "int":
                        type.append("I");
                        break;
                    case "short":
                        type.append("S");
                        break;
                    case "long":
                        type.append("J");
                        break;
                    case "byte":
                        type.append("B");
                        break;
                    case "float":
                        type.append("F");
                        break;
                    case "double":
                        type.append("D");
                        break;
                    case "char":
                        type.append("C");
                        break;
                    default:
                        if (brackets.equals(""))
                            type.append("L"+s+";");
                        else
                            type.append(s);
                        break;
                }
                methods[i] = type.toString();
            }

            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/Phaser", "arriveAndAwaitAdvance", "()I", false);
            mv.visitInsn(POP);



            for (int i = 0; i < methodNames.length; i++) {

                mv.visitLabel(labelsForMethod[i][0]);
                if (i > 0)
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitIntInsn(SIPUSH, i);
                mv.visitInsn(IALOAD);
                mv.visitMethodInsn(INVOKESTATIC, "com/devexperts/dxlab/lincheck/util/MyRandom", "busyWait", "(I)V", false);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitIntInsn(SIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, generatedClassName, testFieldName, "L" + testClassName + ";");
                //mv.visitVarInsn(ALOAD, 0);
                //mv.visitVarInsn(ALOAD, 0);
                for (int j = 0; j < parameters[i].length; j++) {
                    String s = parameters[i][j].type;
                    switch (s){
                        case "boolean":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        case "int":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        case "short":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        case "long":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        case "byte":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        case "float":
                            mv.visitVarInsn(FLOAD, borders[i][j]);
                            break;
                        case "double":
                            mv.visitVarInsn(DLOAD, borders[i][j]);
                            break;
                        case "char":
                            mv.visitVarInsn(ILOAD, borders[i][j]);
                            break;
                        default:
                            mv.visitVarInsn(ALOAD, borders[i][j]);
                            break;
                    }
                }
                String s = methodTypes[i].replace(".", "/");
                String brackets = GetBrackets(s);
                s = s.replace("[]", "");
                mv.visitMethodInsn(INVOKEVIRTUAL, testClassName, methodNames[i], methods[i], false);
                switch (methodTypes[i]){
                    case "void":
                        break;
                    case "boolean":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                        break;
                    case "int":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                        break;
                    case "short":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                        break;
                    case "long":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                        break;
                    case "byte":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                        break;
                    case "float":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                        break;
                    case "double":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                        break;
                    case "char":
                        if (brackets.equals(""))
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                        break;
                    default:
                        break;
                }
                if (methodTypes[i].equals("void")){
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/devexperts/dxlab/lincheck/util/Result", "setVoid", "()V", false);
                } else {
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/devexperts/dxlab/lincheck/util/Result", "setValue", "(Ljava/lang/Object;)V", false);
                }
                mv.visitLabel(labelsForMethod[i][1]);
                if (i < labelsForMethod.length - 1) {
                    mv.visitJumpInsn(GOTO, labelsForMethod[i + 1][0]);
                } else{
                    mv.visitJumpInsn(GOTO, l12);
                }
                mv.visitLabel(labelsForMethod[i][2]);
                if (i == 0)
                    mv.visitFrame(Opcodes.F_FULL, parList.size(), parList.toArray(), 1, new Object[]{"java/lang/Exception"});
                else
                    mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
                mv.visitVarInsn(ASTORE, border);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitIntInsn(BIPUSH, i);
                mv.visitInsn(AALOAD);
                mv.visitVarInsn(ALOAD, border);
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/devexperts/dxlab/lincheck/util/Result", "setException", "(Ljava/lang/Exception;)V", false);
            }
            mv.visitLabel(l12);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 5);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}