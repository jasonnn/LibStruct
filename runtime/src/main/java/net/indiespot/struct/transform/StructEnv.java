package net.indiespot.struct.transform;

import net.indiespot.struct.Struct;
import net.indiespot.struct.StructInfo;
import net.indiespot.struct.api.StructConfig;
import net.indiespot.struct.api.runtime.StructAllocationStack;
import net.indiespot.struct.api.runtime.StructGC;
import net.indiespot.struct.api.runtime.StructMemory;
import net.indiespot.struct.api.runtime.StructThreadLocalStack;
import org.objectweb.asm.*;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import static net.indiespot.struct.transform.StructEnv.Descs.*;
import static org.objectweb.asm.Opcodes.*;

public class StructEnv {
    public static final boolean SAFETY_FIRST;
    public static final boolean PRINT_LOG;
    public static final boolean MEMORY_BASE_OFFSET;

    static class Descs {
        public static final String PLAIN_STRUCT_FLAG = "$truct";
        public static final String ARRAY_WRAPPED_STRUCT_FLAG = "[L" + PLAIN_STRUCT_FLAG + ';';
        public static final String WRAPPED_STRUCT_FLAG = 'L' + PLAIN_STRUCT_FLAG + ';';
        static final String RENAMED_CONSTRUCTOR_NAME = "_<init>_";
    }


    static {
        SAFETY_FIRST = StructConfig.parseVmArg(StructEnv.class, "SAFETY_FIRST", 0, false) != 0L;
        PRINT_LOG = StructConfig.parseVmArg(StructEnv.class, "PRINT_LOG", 0, false) != 0L;
        MEMORY_BASE_OFFSET = StructConfig.parseVmArg(StructEnv.class, "MEMORY_BASE_OFFSET", 0, false) != 0L;
    }

    private static Set<String> plain_struct_types = new HashSet<>();
    private static Set<String> wrapped_struct_types = new HashSet<>();
    private static Set<String> array_wrapped_struct_types = new HashSet<>();
    private static Map<String, StructInfo> struct2info = new HashMap<>();

    private static final boolean struct_rewrite_early_out = true;
    private static volatile boolean is_rewriting = false;

    public static void addStruct(StructInfo info) {
        if (is_rewriting)
            throw new IllegalStateException("cannot add struct definition when classes have been rewritten already");
        struct2info.put(info.fqcn, info);
        plain_struct_types.add(info.fqcn);
        wrapped_struct_types.add('L' + info.fqcn + ';');
        array_wrapped_struct_types.add("[L" + info.fqcn + ';');
    }

    public static void linkStructs() {
        for (StructInfo info : struct2info.values())
            info.validate();
    }

    private enum ReturnValueStrategy {
        COPY, PASS
    }

    private static class ClassInfo {
        final Set<String> fieldsWithStructType = new HashSet<>();
        final Set<String> methodsWithStructAccess = new HashSet<>();
        final Set<String> methodsWithStructCreation = new HashSet<>();
        final Map<String, Integer> methodNameDesc2locals = new HashMap<>();
        final Set<String> ignore = new HashSet<>();

        public boolean needsRewrite() {
            return !fieldsWithStructType.isEmpty() || !methodsWithStructAccess.isEmpty() || !methodsWithStructCreation.isEmpty();
        }

        private void analyze(final String fqcn, byte[] bytecode) {

            if (PRINT_LOG)
                System.out.println("analyzing class: " + fqcn);

            ClassWriter writer = new ClassWriter(0);
            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {

                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    if (plain_struct_types.contains(fqcn)) {
                        if (!superName.equals("java/lang/Object")) {
                            throw new IllegalStateException("struct [" + fqcn + "] must have java.lang.Object as super-type");
                        }
                        if (interfaces != null && interfaces.length != 0) {
                            throw new IllegalStateException("struct [" + fqcn + "] must not implement any interfaces");
                        }
                    }

                    super.visit(version, access, name, signature, superName, interfaces);
                }

                @Override
                public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                    if (ASMConstants.STRUCT_TYPE.equals(desc)) {
                        if (!plain_struct_types.contains(fqcn)) {
                            throw new IllegalStateException("encountered undefined struct: " + fqcn);
                        }
                    }
                    return super.visitAnnotation(desc, visible);
                }

                @Override
                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    if (wrapped_struct_types.contains(desc)) {
                        this.flagRewriteField(name, desc);
                    }
                    if (array_wrapped_struct_types.contains(desc)) {
                        this.flagRewriteField(name, desc);
                    }

                    return super.visitField(access, name, desc, signature, value);
                }

                private void flagRewriteField(String name, String desc) {
                    if (fieldsWithStructType.add(name + desc))
                        if (PRINT_LOG)
                            System.out.println("\tflagging field: " + name + desc);
                }

                @Override
                public MethodVisitor visitMethod(int access, final String methodName, final String methodDesc, String signature, String[] exceptions) {
                    return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, methodName, methodDesc, signature, exceptions)) {
                        {
                            if (PRINT_LOG)
                                System.out.println("\tchecking method for rewrite: " + methodName + methodDesc);

                            if (plain_struct_types.contains(fqcn)) {
                                this.flagRewriteMethod(false);
                            }

                            for (String wrappedStructType : wrapped_struct_types) {
                                if (methodDesc.contains(wrappedStructType)) {
                                    this.flagRewriteMethod(false);
                                }
                            }
                        }

                        @Override
                        public AnnotationVisitor visitAnnotation(String s, boolean b) {
                            if (ASMConstants.SKIP_TRANSFORMATION.equals(s)) {
                                this.flagIgnore();
                                this.flagRewriteMethod(false);
                            }
                            return super.visitAnnotation(s, b);
                        }

                        @Override
                        public void visitTypeInsn(int opcode, String type) {
                            if (opcode == NEW) {
                                if (plain_struct_types.contains(type)) {
                                    this.flagRewriteMethod(true);
                                }
                            }
                            if (opcode == INSTANCEOF) {
                                if (plain_struct_types.contains(type)) {
                                    this.flagRewriteMethod(false);
                                }
                            }
                            if (opcode == ANEWARRAY) {
                                if (plain_struct_types.contains(type)) {
                                    this.flagRewriteMethod(true);
                                }
                            }
                            super.visitTypeInsn(opcode, type);
                        }

                        @Override
                        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                            if (plain_struct_types.contains(owner)) {
                                this.flagRewriteMethod(false);
                            }
                            for (String wrappedStructType : wrapped_struct_types) {
                                if (desc.contains(wrappedStructType)) {
                                    this.flagRewriteMethod(false);
                                }
                            }

                            super.visitFieldInsn(opcode, owner, name, desc);
                        }

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            if (plain_struct_types.contains(owner)) {
                                this.flagRewriteMethod(false);
                            }
                            for (String wrappedStructType : wrapped_struct_types) {
                                if (desc.contains(wrappedStructType)) {
                                    this.flagRewriteMethod(false);
                                }
                            }
                            if (owner.equals(jvmClassName(Struct.class))) {
                                this.flagRewriteMethod(false);
                            }

                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                        }

                        @Override
                        public void visitLdcInsn(Object cst) {
                            if (cst instanceof Type) {
                                if (plain_struct_types.contains(((Type) cst).getInternalName())) {
                                    this.flagRewriteMethod(false);
                                }
                            }

                            super.visitLdcInsn(cst);
                        }

                        private void flagIgnore() {
                            ignore.add(methodName + methodDesc);
                        }

                        private void flagRewriteMethod(boolean forStructCreation) {
                            String id = (methodName + methodDesc);
                            if (ignore.contains(id)) return;
                            if (methodsWithStructAccess.add(id))
                                if (PRINT_LOG)
                                    System.out.println("\t\tflagged for rewrite");

                            if (forStructCreation) {
                                methodsWithStructCreation.add(id);
                            }
                        }

                        @Override
                        public void visitMaxs(int maxStack, int maxLocals) {
                            methodNameDesc2locals.put(methodName + methodDesc, maxLocals);
                            super.visitMaxs(maxStack, maxLocals);
                        }
                    };
                }
            };
            new ClassReader(bytecode).accept(visitor, 0);
        }
    }

    private static final Map<Integer, String> _aload2type = new HashMap<>();
    private static final Map<Integer, String> _astore2type = new HashMap<>();

    static {
        _aload2type.put(Opcodes.LALOAD, "J");
        _aload2type.put(Opcodes.DALOAD, "D");
        _aload2type.put(Opcodes.IALOAD, "I");
        _aload2type.put(Opcodes.FALOAD, "F");
        _aload2type.put(Opcodes.BALOAD, "B");
        _aload2type.put(Opcodes.CALOAD, "C");
        _aload2type.put(Opcodes.SALOAD, "S");

        _astore2type.put(Opcodes.LASTORE, "J");
        _astore2type.put(Opcodes.DASTORE, "D");
        _astore2type.put(Opcodes.IASTORE, "I");
        _astore2type.put(Opcodes.FASTORE, "F");
        _astore2type.put(Opcodes.BASTORE, "B");
        _astore2type.put(Opcodes.CASTORE, "C");
        _astore2type.put(Opcodes.SASTORE, "S");
    }

    public static byte[] rewriteClass(final String fqcn, byte[] bytecode) {
        is_rewriting = true;

        final ClassInfo info = new ClassInfo();
        info.analyze(fqcn, bytecode);
        if (!info.needsRewrite())
            return null;

        if (StructEnv.PRINT_LOG) {
            System.out.println("StructEnv.rewrite INPUT [" + fqcn + "]:");
            int flags = 0;
            ClassReader cr = new ClassReader(bytecode);
            cr.accept(new TraceClassVisitor(null, new Textifier(), new PrintWriter(System.out)), flags);
        }

        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS) {
            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                String type3 = super.getCommonSuperClass(type1, type2);
                System.out.println("StructEnv.rewriteClass ClassWriter.getCommonSuperClass(" + type1 + ", " + type2 + ") -> " + type3);
                return type3;
            }
        };

        if (StructMemory.CHECK_SOURCECODE) {
            File srcDir = new File("./src/");
            if (srcDir.exists()) {
                String relpath = fqcn;
                if (relpath.indexOf('$') != -1)
                    relpath = relpath.substring(0, relpath.indexOf('$'));
                File srcFile = new File(srcDir, relpath + ".java");
                if (srcFile.exists()) {
                    try {
                        InputStream fis = new FileInputStream(srcFile);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] tmp = new byte[4 * 1024];
                        while (true) {
                            int got = fis.read(tmp, 0, tmp.length);
                            if (got == -1)
                                break;
                            baos.write(tmp, 0, got);
                        }
                        String src = new String(baos.toByteArray(), "UTF-8");

                        for (String struct : plain_struct_types) {
                            struct = struct.substring(struct.lastIndexOf('/') + 1);
                            struct = struct.substring(struct.indexOf('$') + 1);
                            if (src.contains('<' + struct + '>')) {
                                int io = src.indexOf('<' + struct + '>');
                                int io2 = -1;
                                for (char c : " \t(".toCharArray()) {
                                    int lio = src.substring(0, io).lastIndexOf(c);
                                    if (lio != -1)
                                        io2 = Math.max(io2, lio);
                                }
                                throw new UnsupportedOperationException("Cannot use generics with structs.\n" + //
                                        "Found: " + src.substring(io2 == -1 ? io : io2 + 1, io) + '<' + struct + "> in: " + relpath + ".java");
                            }
                        }
                    } catch (IOException exc) {
                        // ignore
                        exc.printStackTrace();
                    }
                }
            }
        }

        final Map<String, Set<String>> final2origMethods = new HashMap<>();

        final String[] currentMethodName = new String[1];
        final String[] currentMethodDesc = new String[1];

        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public void visitSource(String source, String debug) {
                super.visitSource(source, debug);
            }

            @Override
            public void visitOuterClass(String owner, String name, String desc) {
                super.visitOuterClass(owner, name, desc);
            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                return super.visitAnnotation(desc, visible);
            }

            @Override
            public void visitAttribute(Attribute attr) {
                super.visitAttribute(attr);
            }

            @Override
            public void visitInnerClass(String name, String outerName, String innerName, int access) {
                super.visitInnerClass(name, outerName, innerName, access);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                if (PRINT_LOG)
                    System.out.println("\tfield1: " + name + ' ' + desc);

                if (struct2info.containsKey(fqcn)) {
                    if ((access & ACC_STATIC) == 0) {
                        if (PRINT_LOG)
                            System.out.println("\tremoved struct field");
                        return null; // remove instance fields
                    }
                }

                if (wrapped_struct_types.contains(desc))
                    desc = WRAPPED_STRUCT_FLAG;
                if (array_wrapped_struct_types.contains(desc))
                    desc = ARRAY_WRAPPED_STRUCT_FLAG;
                if (PRINT_LOG)
                    System.out.println("\tfield2: " + name + ' ' + desc);
                String finalFieldDesc = desc.replace(WRAPPED_STRUCT_FLAG, "I");
                return super.visitField(access, name, finalFieldDesc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String methodName, String methodDesc, String signature, String[] exceptions) {
                currentMethodName[0] = methodName;
                currentMethodDesc[0] = methodDesc;

                final String origMethodName = methodName;
                final String origMethodDesc = methodDesc;

                if (PRINT_LOG)
                    System.out.println("\tmethod1: " + methodName + ' ' + methodDesc);

                String returnsStructType = null;
                for (String struct : struct2info.keySet()) {
                    if (methodDesc.endsWith(")L" + struct + ';'))
                        returnsStructType = struct;
                    methodDesc = methodDesc.replace('L' + struct + ';', WRAPPED_STRUCT_FLAG);
                }
                final String _returnsStructType = returnsStructType;

                if (struct2info.containsKey(fqcn)) {
                    if (methodName.equals("<init>"))
                        methodName = RENAMED_CONSTRUCTOR_NAME;

                    if ((access & ACC_STATIC) == 0) {
                        // make instance methods static
                        // add 'this' as first parameter
                        access |= ACC_STATIC;
                        methodDesc = '(' + WRAPPED_STRUCT_FLAG + methodDesc.substring(1);
                    }
                }

                if (PRINT_LOG)
                    System.out.println("\tmethod2: " + methodName + ' ' + methodDesc);

                String finalMethodName = methodName.replace(WRAPPED_STRUCT_FLAG, "I");
                String finalMethodDesc = methodDesc.replace(WRAPPED_STRUCT_FLAG, "I");

                {
                    String key = finalMethodName + ' ' + finalMethodDesc;
                    Set<String> origMethods = final2origMethods.get(key);
                    if (origMethods == null)
                        final2origMethods.put(key, origMethods = new TreeSet<>());
                    origMethods.add(origMethodName + ' ' + origMethodDesc);
                }

                final MethodVisitor mv = super.visitMethod(access, finalMethodName, finalMethodDesc, signature, exceptions);

                if (struct_rewrite_early_out) {
                    // do we need to rewrite this method?
                    boolean hasStructAccess = info.methodsWithStructAccess.contains(origMethodName + origMethodDesc);
                    if (PRINT_LOG)
                        System.out.println("\t\tearly out for rewrite? [" + !hasStructAccess + ']');
                    if (!hasStructAccess)
                        return mv; // nope!
                }
                final boolean hasStructCreation = info.methodsWithStructCreation.contains(origMethodName + origMethodDesc);

                final String _methodName = methodName;
                final int _access = access;
                final FlowAnalysisMethodVisitor flow = new FlowAnalysisMethodVisitor(mv, access, fqcn, methodName, methodDesc, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM5, flow) {
                    private ReturnValueStrategy strategy;

                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if (ASMConstants.COPY_STRUCT.equals(desc)) {
                            strategy = ReturnValueStrategy.COPY;
                            if (PRINT_LOG)
                                System.out.println("\t\t\twith struct return value, with Copy strategy");
                        } else if (ASMConstants.TAKE_STRUCT.equals(desc)) {
                            strategy = ReturnValueStrategy.PASS;
                            if (PRINT_LOG)
                                System.out.println("\t\t\twith struct return value, with Pass strategy");
                        }
                        return super.visitAnnotation(desc, visible);
                    }

                    @Override
                    public void visitCode() {
                        //TODO are these checks necessary?
                        if (_returnsStructType == null) {
                            if (strategy != null) {
                                String msg = "";
                                msg += "@CopyStruct and @TakeStruct must only be used with struct return values";
                                msg += "\n\t\t" + fqcn + '.' + _methodName + origMethodDesc;

                                throw new IllegalStateException(msg);
                            }
                        } else {
                            if (strategy == null) {
                                if ((_access & ACC_SYNTHETIC) != 0) {
                                    strategy = ReturnValueStrategy.PASS;
                                    if (PRINT_LOG)
                                        System.out.println("\t\t\twith struct return value, using fallback Pass strategy due to synthetic method");
                                }
                            }
                            if (strategy == null) {
                                String msg = "";
                                msg += "must define how struct return values are handled: ";
                                msg += "\n\t\t" + fqcn + '.' + _methodName + origMethodDesc;

                                throw new IllegalStateException(msg);
                            }
                        }

                        super.visitCode();

                        if (hasStructCreation) {
                            // ...
                            super.visitMethodInsn(INVOKESTATIC, jvmClassName(StructThreadLocalStack.class), "saveStack", "()L" + StructAllocationStack.class.getName().replace('.', '/') + ';', false);
                            // ..., sas
                            super.visitVarInsn(ASTORE, info.methodNameDesc2locals.get(origMethodName + origMethodDesc));
                        }
                    }

                    @SuppressWarnings("StatementWithEmptyBody")
                    @Override
                    public void visitInsn(int opcode) {
                        if (hasStructCreation) {
                            switch (opcode) {
                                case RETURN:
                                case ARETURN:
                                case IRETURN:
                                case FRETURN:
                                case LRETURN:
                                case DRETURN:
                                    super.visitVarInsn(ALOAD, info.methodNameDesc2locals.get(origMethodName + origMethodDesc));
                                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, StructEnv.jvmClassName(StructAllocationStack.class), "restore", "()I", false);
                                    super.visitInsn(Opcodes.POP);
                                    break;
                            }
                        }

                        if (opcode >= 79 && opcode <= 86) { // Opcodes.*ASTORE
                            boolean isWide = (opcode == Opcodes.LASTORE || opcode == Opcodes.DASTORE);
                            if (_astore2type.containsKey(opcode) && flow.stack.peek(isWide ? 3 : 2) == VarType.EMBEDDED_ARRAY) {
                                flow.stack.set(isWide ? 3 : 2, VarType.INT);
                                super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), _astore2type.get(opcode).toLowerCase() + "aput", "(II" + _astore2type.get(opcode) + ")V", false);
                                return;
                            }
                        }
                        if (opcode >= 46 && opcode <= 53) { // Opcodes.*ALOAD
                            if (_aload2type.containsKey(opcode) && flow.stack.peek(1) == VarType.EMBEDDED_ARRAY) {
                                flow.stack.set(1, VarType.INT);
                                super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), _aload2type.get(opcode).toLowerCase() + "aget", "(II)" + _aload2type.get(opcode), false);
                                return;
                            }
                        }
                        if (opcode == ARRAYLENGTH && flow.stack.peek() == VarType.EMBEDDED_ARRAY) {
                            throw new IllegalStateException("cannot fetch length of embedded array, as the array does not exist");
                        }

                        if (opcode == ARETURN && flow.stack.peek() == VarType.EMBEDDED_ARRAY) {
                            throw new IllegalStateException("cannot return embedded array, as the array does not exist");
                        }

                        if (opcode == ARETURN && flow.stack.peek() == VarType.STRUCT) {
                            if (strategy == null)
                                throw new IllegalStateException();

                            if (PRINT_LOG)
                                System.out.println("\t\t\treturn value strategy: " + strategy);

                            if (strategy == ReturnValueStrategy.PASS) {
                                // no-op
                            } else if (strategy == ReturnValueStrategy.COPY) {
                                super.visitIntInsn(SIPUSH, struct2info.get(_returnsStructType).sizeof);
                                super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "allocateCopy", '(' + WRAPPED_STRUCT_FLAG + "I)" + WRAPPED_STRUCT_FLAG, false);
                            } else {
                                throw new IllegalStateException();
                            }
                        }

                        super.visitInsn(opcode);
                    }

                    @Override
                    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                        //TODO investigate SuspiciousMethodCalls
                        if (local != null) {
                            for (int i = 0; i < local.length; i++) {
                                if (array_wrapped_struct_types.contains(local[i]))
                                    local[i] = ARRAY_WRAPPED_STRUCT_FLAG;
                                else if (wrapped_struct_types.contains(local[i]))
                                    local[i] = WRAPPED_STRUCT_FLAG;
                            }
                        }
                        if (stack != null) {
                            for (int i = 0; i < stack.length; i++) {
                                if (array_wrapped_struct_types.contains(stack[i]))
                                    stack[i] = ARRAY_WRAPPED_STRUCT_FLAG;
                                else if (wrapped_struct_types.contains(stack[i]))
                                    stack[i] = WRAPPED_STRUCT_FLAG;
                            }
                        }

                        super.visitFrame(type, nLocal, local, nStack, stack);
                    }

                    @Override
                    public void visitTypeInsn(int opcode, String type) {
                        if (opcode == NEW) {
                            if (struct2info.containsKey(type)) {
                                super.visitIntInsn(Opcodes.SIPUSH, struct2info.get(type).sizeof);
                                super.visitVarInsn(ALOAD, info.methodNameDesc2locals.get(origMethodName + origMethodDesc));
                                if (struct2info.get(type).skipZeroFill)
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "allocateSkipZeroFill", "(IL" + StructEnv.jvmClassName(StructAllocationStack.class) + ";)" + WRAPPED_STRUCT_FLAG, false);
                                else
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "allocate", "(IL" + StructEnv.jvmClassName(StructAllocationStack.class) + ";)" + WRAPPED_STRUCT_FLAG, false);
                                return;
                            }
                        } else if (opcode == ANEWARRAY) {
                            if (struct2info.containsKey(type)) {
                                super.visitIntInsn(Opcodes.SIPUSH, struct2info.get(type).sizeof);
                                super.visitVarInsn(ALOAD, info.methodNameDesc2locals.get(origMethodName + origMethodDesc));
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "allocateArray", "(IIL" + jvmClassName(StructAllocationStack.class) + ";)" + ARRAY_WRAPPED_STRUCT_FLAG, false);
                                flow.stack.popEQ(VarType.STRUCT_ARRAY);
                                flow.stack.push(VarType.STRUCT_ARRAY);
                                return;
                            }
                        } else if (opcode == CHECKCAST) {
                            if (flow.stack.peek() == VarType.STRUCT) {
                                return;
                            }
                            if (flow.stack.peek() == VarType.STRUCT_ARRAY) {
                                return;
                            }
                        }

                        super.visitTypeInsn(opcode, type);
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                        if (StructEnv.SAFETY_FIRST) {
                            if (opcode == PUTFIELD || opcode == PUTSTATIC) {
                                if (wrapped_struct_types.contains(desc)) {
                                    if (plain_struct_types.contains(owner)) {
                                        super.visitInsn(DUP2);
                                        super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "checkFieldAssignment", '(' + WRAPPED_STRUCT_FLAG + "" + WRAPPED_STRUCT_FLAG + ")V", false);
                                    } else {
                                        super.visitInsn(DUP);
                                        super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), "checkFieldAssignment", '(' + WRAPPED_STRUCT_FLAG + ")V", false);
                                    }
                                }
                            }
                        }

                        if (opcode == PUTFIELD) {
                            if (struct2info.containsKey(owner)) {
                                StructInfo info = struct2info.get(owner);
                                int offset = info.field2offset.get(name);
                                boolean embed = info.field2embed.get(name);
                                String type = info.field2type.get(name);

                                if (embed) {
                                    throw new IllegalStateException("cannot assign to embedded struct field");
                                }

                                String methodName, paramType, returnType;
                                if (wrapped_struct_types.contains(type)) {
                                    methodName = "$put";
                                    paramType = WRAPPED_STRUCT_FLAG;
                                    returnType = "V";
                                } else {
                                    methodName = type.toLowerCase() + "put";
                                    paramType = type;
                                    returnType = "V";
                                }

                                super.visitIntInsn(SIPUSH, offset);
                                super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), methodName, '(' + WRAPPED_STRUCT_FLAG + paramType + "I)" + returnType, false);
                                return;
                            }
                        } else if (opcode == GETFIELD) {
                            if (struct2info.containsKey(owner)) {
                                StructInfo info = struct2info.get(owner);
                                int offset = info.field2offset.get(name);
                                boolean embed = info.field2embed.get(name);
                                String type = info.field2type.get(name);

                                if (embed) {
                                    flow.stack.popEQ(VarType.STRUCT);
                                    flow.stack.push(VarType.INT);

                                    super.visitIntInsn(SIPUSH, StructMemory.bytes2words(offset));
                                    super.visitInsn(IADD);

                                    flow.stack.popEQ(VarType.INT);

                                    if (type.startsWith("[") && type.length() == 2)
                                        flow.stack.push(VarType.EMBEDDED_ARRAY);
                                    else
                                        flow.stack.push(VarType.STRUCT);
                                    return;
                                }

                                String methodName, paramType, returnType;
                                if (wrapped_struct_types.contains(type)) {
                                    methodName = "$get";
                                    paramType = WRAPPED_STRUCT_FLAG;
                                    returnType = WRAPPED_STRUCT_FLAG;
                                } else {
                                    methodName = type.toLowerCase() + "get";
                                    paramType = WRAPPED_STRUCT_FLAG;
                                    returnType = type;
                                }

                                super.visitIntInsn(SIPUSH, offset);
                                super.visitMethodInsn(INVOKESTATIC, StructEnv.jvmClassName(StructMemory.class), methodName, '(' + paramType + "I)" + returnType, false);
                                return;
                            }
                        }

                        if (wrapped_struct_types.contains(desc)) {
                            desc = WRAPPED_STRUCT_FLAG;
                        }
                        if (array_wrapped_struct_types.contains(desc)) {
                            desc = ARRAY_WRAPPED_STRUCT_FLAG;
                        }

                        super.visitFieldInsn(opcode, owner, name, desc);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                        for (String wrapped_struct_type : wrapped_struct_types) {
                            desc = desc.replace(wrapped_struct_type, WRAPPED_STRUCT_FLAG);
                        }

                        if (struct2info.containsKey(fqcn) && _methodName.equals(RENAMED_CONSTRUCTOR_NAME)) {
                            // remove Object.super() from any Struct constructor
                            if (opcode == INVOKESPECIAL && name.equals("<init>") && desc.equals("()V")) {
                                super.visitInsn(POP);
                                return;
                            }
                        }

                        if (struct2info.containsKey(owner)) {
                            if (opcode == INVOKESPECIAL) {
                                if (name.equals("<init>")) {
                                    name = RENAMED_CONSTRUCTOR_NAME;

                                    // add 'this' as first parameter
                                    opcode = INVOKESTATIC;
                                    desc = '(' + WRAPPED_STRUCT_FLAG + desc.substring(1);
                                }
                            } else if (opcode == INVOKEVIRTUAL) {
                                // add 'this' as first parameter
                                opcode = INVOKESTATIC;
                                desc = '(' + WRAPPED_STRUCT_FLAG + desc.substring(1);
                            }
                        }

                        if (owner.equals(StructEnv.jvmClassName(Struct.class))) {
                            if (name.equals("nullStruct") && desc.equals("(Ljava/lang/Class;)Ljava/lang/Object;")) {
                                if (flow.stack.peek() == VarType.STRUCT_TYPE) {
                                    flow.stack.set(0, VarType.INT);
                                    // ..., sizeof
                                    super.visitInsn(Opcodes.POP);
                                    // ...
                                    super.visitInsn(Opcodes.ICONST_0);
                                    // ..., 'null'
                                    flow.stack.set(0, VarType.STRUCT);
                                    return;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("sizeof") && desc.equals("(Ljava/lang/Class;)I")) {
                                if (flow.stack.peek() == VarType.STRUCT_TYPE) {
                                    flow.stack.set(0, VarType.INT);
                                    // ...,sizeof
                                    return;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("nullArray") && desc.equals("(Ljava/lang/Class;I)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,sizeof,length
                                    flow.visitInsn(Opcodes.SWAP);
                                    // ...,length,sizeof
                                    flow.visitInsn(Opcodes.POP);
                                    // ...,length
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "nullArray";
                                    desc = "(I)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("malloc") && desc.equals("(Ljava/lang/Class;)Ljava/lang/Object;")) {
                                if (flow.stack.peek(0) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(0, VarType.INT);
                                    // ...,sizeof
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "malloc";
                                    desc = "(I)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("mallocArray") && desc.equals("(Ljava/lang/Class;I)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,sizeof,length
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "mallocArray";
                                    desc = "(II)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("calloc") && desc.equals("(Ljava/lang/Class;)Ljava/lang/Object;")) {
                                if (flow.stack.peek(0) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(0, VarType.INT);
                                    // ...,sizeof
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "calloc";
                                    desc = "(I)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("callocArray") && desc.equals("(Ljava/lang/Class;I)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,sizeof,length
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "callocArray";
                                    desc = "(II)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("reallocArray") && desc.equals("(Ljava/lang/Class;[Ljava/lang/Object;I)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(2) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(2, VarType.INT);
                                    // ...,sizeof,struct[],length
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "reallocArray";
                                    desc = "(I[" + WRAPPED_STRUCT_FLAG + "I)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("mallocArrayBase") && desc.equals("(Ljava/lang/Class;I)Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,sizeof,length
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "mallocArrayBase";
                                    desc = "(II)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("callocArrayBase") && desc.equals("(Ljava/lang/Class;I)Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,sizeof,length
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "callocArrayBase";
                                    desc = "(II)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("free") && desc.equals("(Ljava/lang/Object;)V")) {
                                if (flow.stack.peek() == VarType.NULL) {
                                    // ..., NULL
                                    super.visitInsn(Opcodes.POP); // right thing
                                    // to do?
                                    // ...
                                    return;
                                } else if (flow.stack.peek() == VarType.STRUCT) {
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "freeHandle";
                                    desc = '(' + WRAPPED_STRUCT_FLAG + ")V";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("free") && desc.equals("([Ljava/lang/Object;)V")) {
                                if (flow.stack.peek() == VarType.NULL) {
                                    // ..., NULL
                                    super.visitInsn(Opcodes.POP); // right thing
                                    // to do?
                                    // ...
                                    return;
                                } else if (flow.stack.peek() == VarType.STRUCT_ARRAY) {
                                    owner = StructEnv.jvmClassName(StructGC.class);
                                    name = "freeHandles";
                                    desc = "([" + WRAPPED_STRUCT_FLAG + ")V";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("copy") && desc.equals("(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)V")) {
                                if (flow.stack.peek(2) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(2, VarType.INT);
                                    // ...,sizeof,src,dst
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "copy";
                                    desc = "(I" + WRAPPED_STRUCT_FLAG + "" + WRAPPED_STRUCT_FLAG + ")V";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek(2));
                                }
                            } else if (name.equals("copy") && desc.equals("(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;I)V")) {
                                if (flow.stack.peek(3) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(3, VarType.INT);
                                    // ...,sizeof,src,dst,count
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "copy";
                                    desc = "(I" + WRAPPED_STRUCT_FLAG + "" + WRAPPED_STRUCT_FLAG + "I)V";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek(3));
                                }
                            } else if (name.equals("swap") && desc.equals("(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)V")) {
                                if (flow.stack.peek(2) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(2, VarType.INT);
                                    // ...,sizeof,src,dst
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "swap";
                                    desc = "(I" + WRAPPED_STRUCT_FLAG + "" + WRAPPED_STRUCT_FLAG + ")V";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek(2));
                                }
                            } else if (name.equals("view") && desc.equals("(Ljava/lang/Object;Ljava/lang/Class;I)Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,this,asType,offset
                                    super.visitInsn(Opcodes.SWAP);
                                    // ...,this,offset,asType
                                    super.visitInsn(Opcodes.POP);
                                    // ...,this,offset
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "view";
                                    desc = '(' + WRAPPED_STRUCT_FLAG + "I)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek(2));
                                }
                            } else if (name.equals("index") && desc.equals("(Ljava/lang/Object;Ljava/lang/Class;I)Ljava/lang/Object;")) {
                                if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,address,sizeof,index
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "index";
                                    desc = '(' + WRAPPED_STRUCT_FLAG + "II)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek(2));
                                }
                            } else if (name.equals("fromPointer") && desc.equals("(J)Ljava/lang/Object;")) {
                                if (flow.stack.peek() == VarType.MISC) {
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "pointer2handle";
                                    desc = "(J)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("fromPointer") && desc.equals("(JLjava/lang/Class;I)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(2) == VarType.MISC && flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(1, VarType.INT);
                                    // ...,address,sizeof,length
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "pointer2handles";
                                    desc = "(JII)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("fromPointer") && desc.equals("(JII)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(2) == VarType.MISC) {
                                    // ...,address,sizeof,length
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "pointer2handles";
                                    desc = "(JII)[" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("getPointer") && desc.equals("(Ljava/lang/Object;)J")) {
                                if (flow.stack.peek() == VarType.NULL) {
                                    // ..., NULL
                                    super.visitInsn(Opcodes.POP);
                                    // ...
                                    super.visitInsn(Opcodes.ICONST_0);
                                    // ..., 0
                                    super.visitInsn(Opcodes.I2L);
                                    // ..., 0L
                                    return;
                                } else if (flow.stack.peek() == VarType.STRUCT) {
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "handle2pointer";
                                    desc = '(' + WRAPPED_STRUCT_FLAG + ")J";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("isReachable") && desc.equals("(Ljava/lang/Object;)Z")) {
                                if (flow.stack.peek() == VarType.NULL) {
                                    // ..., NULL
                                    super.visitInsn(Opcodes.POP);
                                    // ...
                                    super.visitInsn(Opcodes.ICONST_0);
                                    // ..., 'false'
                                    return;
                                } else if (flow.stack.peek() == VarType.STRUCT) {
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "isValid";
                                    desc = '(' + WRAPPED_STRUCT_FLAG + ")Z";
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            } else if (name.equals("map") && desc.equals("(Ljava/lang/Class;Ljava/nio/ByteBuffer;)[Ljava/lang/Object;")) {
                                if (flow.stack.peek() == VarType.REFERENCE) {
                                    if (flow.stack.peek(1) == VarType.STRUCT_TYPE) {
                                        // ...,type,buffer
                                        flow.stack.set(1, VarType.INT);
                                        // ...,sizeof,buffer
                                        owner = StructEnv.jvmClassName(StructMemory.class);
                                        name = "mapBuffer";
                                        desc = "(ILjava/nio/ByteBuffer;)[" + WRAPPED_STRUCT_FLAG;
                                    }
                                } else {
                                    throw new IllegalStateException();
                                }
                            } else if (name.equals("map") && desc.equals("(Ljava/lang/Class;Ljava/nio/ByteBuffer;II)[Ljava/lang/Object;")) {
                                if (flow.stack.peek(2) == VarType.REFERENCE) {
                                    if (flow.stack.peek(3) == VarType.STRUCT_TYPE) {
                                        // ...,type,buffer,stride,offset
                                        flow.stack.set(3, VarType.INT);
                                        // ...,sizeof,buffer,stride,offset
                                        owner = StructEnv.jvmClassName(StructMemory.class);
                                        name = "mapBuffer";
                                        desc = "(ILjava/nio/ByteBuffer;II)[" + WRAPPED_STRUCT_FLAG;
                                    }
                                } else {
                                    throw new IllegalStateException();
                                }
                            } else if (name.equals("createStructAllocationStack") && desc.equals("(I)L" + jvmClassName(StructAllocationStack.class) + ';')) {
                                owner = StructEnv.jvmClassName(StructMemory.class);
                            } else if (name.equals("discardStructAllocationStack") && desc.equals("(L" + jvmClassName(StructAllocationStack.class) + ";)V")) {
                                owner = StructEnv.jvmClassName(StructMemory.class);
                            } else if (name.equals("stackAlloc") && desc.equals("(L" + jvmClassName(StructAllocationStack.class) + ";Ljava/lang/Class;)Ljava/lang/Object;")) {
                                if (flow.stack.peek(0) == VarType.STRUCT_TYPE) {
                                    flow.stack.set(0, VarType.INT);
                                    // ...,stack,sizeof
                                    flow.visitInsn(Opcodes.SWAP);
                                    // ...,sizeof,stack
                                    owner = StructEnv.jvmClassName(StructMemory.class);
                                    name = "allocateSkipZeroFill";
                                    desc = "(IL" + jvmClassName(StructAllocationStack.class) + ";)" + WRAPPED_STRUCT_FLAG;
                                } else {
                                    throw new IllegalStateException("peek: " + flow.stack.peek());
                                }
                            }
                        }

                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }

                    @Override
                    public void visitLdcInsn(Object cst) {
                        if (cst instanceof Type) {
                            if (plain_struct_types.contains(((Type) cst).getInternalName())) {
                                flow.visitIntInsn(Opcodes.SIPUSH, struct2info.get(((Type) cst).getInternalName()).sizeof);
                                flow.stack.popEQ(VarType.INT);
                                flow.stack.push(VarType.STRUCT_TYPE);
                                return;
                            }
                        }

                        super.visitLdcInsn(cst);
                    }
                };
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }
        };

        if (StructEnv.PRINT_LOG) {
            System.out.println("StructEnv.rewrite TRANSFORM [" + fqcn + "]:");
        }

        try {
            new ClassReader(bytecode).accept(visitor, 0);
        } catch (Throwable cause) {
            String msg = "LibStruct failed to rewrite classpath:\n";
            msg += "\tLast entered class: \n";
            msg += "\t\tfqcn: " + fqcn + '\n';
            msg += "\n";
            msg += "\tLast entered method: \n";
            msg += "\t\tname: " + currentMethodName[0] + '\n';
            msg += "\t\tdesc: " + currentMethodDesc[0] + '\n';
            if (!StructEnv.PRINT_LOG)
                msg += "\n\t\tfor more information set: -DLibStruct.PRINT_LOG=true";
            throw new IllegalStateException(msg, cause);
        }

        for (Entry<String, Set<String>> entry : final2origMethods.entrySet()) {
            if (entry.getValue().size() > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("LibStruct failed to rewrite classpath:\n")
                        .append("\tLast entered class: \n")
                        .append("\t\tfqcn: ").append(fqcn).append("\n").append("\n")
                        .append("\tThe following methods collide after transformation: \n");
                for (String m : entry.getValue())
                    sb.append("\t\t").append(m).append("\n");
                sb.append("\tdue to shared name and description: (struct references are rewritten to ints)\n");
                sb.append("\t\t").append(entry.getKey()).append('\n');
                if (!StructEnv.PRINT_LOG)
                    sb.append("\n\t\tfor more information set: -DLibStruct.PRINT_LOG=true");
                throw new IllegalStateException(sb.toString());
            }
        }

        bytecode = writer.toByteArray();

        if (StructEnv.PRINT_LOG) {
            System.out.println("StructEnv.rewrite OUTPUT [" + fqcn + "]:");
            int flags = 0;
            ClassReader cr = new ClassReader(bytecode);
            cr.accept(new TraceClassVisitor(null, new Textifier(), new PrintWriter(System.out)), flags);
        }

        return bytecode;
    }

    public static String jvmClassName(Class<?> type) {
        return type.getName().replace('.', '/');
    }

    public static void printClass(byte[] bytecode) {
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
        new ClassReader(bytecode).accept(traceClassVisitor, 0);
    }
}
