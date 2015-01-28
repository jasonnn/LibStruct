package net.indiespot.struct;

import net.indiespot.struct.transform.ASMConstants;
import net.indiespot.struct.transform.StructEnv;
import org.objectweb.asm.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

//called reflectively from StructAgent
@SuppressWarnings("unused")
public class StructAgentDelegate implements AgentDelegate {

    @Override
    public void premain(String args, Instrumentation inst) {
        System.out.println("StructAgent: reading struct-defs from resource: '" + args + '\'');

        try (InputStream in = StructAgentDelegate.class.getClassLoader().getResourceAsStream(args)) {
            processStructDefinitionInfo(new BufferedReader(new InputStreamReader(in, "ASCII")));
        } catch (Throwable cause) {
            cause.printStackTrace();
            return;
        }
        for (StructInfo structInfo : StructInfo.values())
            StructEnv.addStruct(structInfo);
        StructEnv.linkStructs();

        System.out.println("StructAgent: initiating application...");

        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                // don't rewrite classes we know are bound to be none of our business
                if (className.startsWith("java/"))
                    return null;
                if (className.startsWith("javax/"))
                    return null;
                if (className.startsWith("sun/"))
                    return null;
                if (className.startsWith("com/sun"))
                    return null;
                if (className.startsWith("org/openjdk/jol"))
                    return null;
                if (className.startsWith("junit"))
                    return null;
                if (className.startsWith("org/junit"))
                    return null;
                if (className.startsWith("com/intellij"))
                    return null;


                try {
                    return StructEnv.rewriteClass(className, classfileBuffer);
                } catch (Throwable t) {
                    t.printStackTrace();
                    System.exit(-1);
                    return null;
                }
            }
        });
    }

    private static void processStructDefinitionInfo(BufferedReader br) throws IOException {

        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            line = line.trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 1)
                throw new IllegalStateException("Usage (each line): [FQCN]");

            String fqcn = parts[0].replace('.', '/');

            StructInfo info = StructInfo.lookup(fqcn);

            if (parts.length == 1) {
                try (InputStream bytecode = StructAgentDelegate.class.getClassLoader().getResourceAsStream(fqcn + ".class")) {
                    info = gatherStructInfo(fqcn, bytecode);
                }
//				InputStream bytecode = StructAgentDelegate.class.getClassLoader().getResourceAsStream(fqcn + ".class");
//				if(bytecode == null) {
//					throw new IllegalStateException("failed to find '" + fqcn + "' in classpath");
//				}
                System.out.println("StructAgent: found struct in classpath: " + fqcn);
                //info = gatherStructInfo(fqcn, bytecode);
            } else {
                if (parts.length < 1)
                    throw new IllegalStateException("Usage (each line): [FQCN] [SIZEOF|FIELD|SKIPZEROFILL] [param]*");

                String prop = parts[1];
                if (info == null) {
                    info = new StructInfo(fqcn);
                }

                switch (prop) {
                    case "SIZEOF":
                        if (parts.length != 3)
                            throw new IllegalStateException("SIZEOF must have 1 parameter: size in bytes");
                        info.setSizeof(Integer.parseInt(parts[2]));
                        break;
                    case "FIELD":
                        String name = parts[2];
                        String type = parts[3];
                        int sizeof;
                        int count = 1;
                        boolean embed = false;

                        switch (parts.length) {
                            case 7:
                                embed = Boolean.parseBoolean(parts[6]);
                                // fall through
                            case 6:
                                count = Integer.parseInt(parts[5]);
                                // fall through
                            case 5:
                                sizeof = Integer.parseInt(parts[4]);
                                break;
                            default:
                                throw new IllegalStateException("FIELD must have 3 or 4 parameters: name, type, offset [, count [, embed]]");
                        }
                        info.addField(name, type, sizeof, count, embed);
                        break;
                    case "SKIPZEROFILL":
                        if (parts.length != 2)
                            throw new IllegalStateException("SKIPZEROFILL must have no paramters");
                        info.skipZeroFill();
                        break;
                    default:
                        throw new IllegalStateException("unexpected property: " + prop);
                }
            }
        }
    }

    private static StructInfo gatherStructInfo(final String fqcn, final InputStream bytecode) throws IOException {
        final StructInfo info = new StructInfo(fqcn);

        //  ClassWriter writer = new ClassWriter(0);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5) {

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                if (!name.equals(fqcn))
                    throw new IllegalStateException();
                super.visit(version, access, name, signature, superName, interfaces);
            }

            // find struct.sizeof
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (ASMConstants.STRUCT_TYPE.equals(desc)) {
                    return new AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                        @Override
                        public void visit(String name, Object value) {
                            if (name.equals("sizeof")) {
                                info.setSizeof((Integer) value);
                            }
                            super.visit(name, value);
                        }
                    };
                } else if (ASMConstants.FORCE_UNINITIALIZED_MEMORY.equals(desc)) {
                    return new AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                        @Override
                        public void visit(String name, Object value) {
                            info.skipZeroFill();
                            super.visit(name, value);
                        }
                    };
                }
                return super.visitAnnotation(desc, visible);
            }

            // find fields.[name,offset]
            @Override
            public FieldVisitor visitField(final int fieldAccess, final String fieldName, final String fieldDesc, String signature, Object value) {
                if ((fieldAccess & Opcodes.ACC_STATIC) == 0) {
                    return new FieldVisitor(Opcodes.ASM5, super.visitField(fieldAccess, fieldName, fieldDesc, signature, value)) {
                        @Override
                        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                            if (ASMConstants.STRUCT_FIELD.equals(desc)) {

                                return new AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                                    int offset = -1;
                                    int length = 1;
                                    boolean embed = false;

                                    @Override
                                    public void visit(String name, Object value) {
                                        switch (name) {
                                            case "offset":
                                                offset = (Integer) value;
                                                break;
                                            case "length":
                                                length = (Integer) value;
                                                embed = true;
                                                break;
                                            case "embed":
                                                embed = (Boolean) value;
                                                break;
                                        }
                                        super.visit(name, value);
                                    }

                                    @Override
                                    public void visitEnd() {
                                        info.addField(fieldName, fieldDesc, offset, length, embed);

                                        super.visitEnd();
                                    }
                                };
                            }
                            return super.visitAnnotation(desc, visible);
                        }
                    };
                }

                return super.visitField(fieldAccess, fieldName, fieldDesc, signature, value);
            }
        };
        new ClassReader(bytecode).accept(visitor, 0);
        return info;
    }
}
