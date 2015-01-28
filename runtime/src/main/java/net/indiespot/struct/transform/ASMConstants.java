package net.indiespot.struct.transform;

import net.indiespot.struct.api.annotations.*;
import org.objectweb.asm.Type;

/**
 * Created by jason on 1/27/15.
 */
public class ASMConstants {
    public static final String COPY_STRUCT = Type.getDescriptor(CopyStruct.class);
    public static final String FORCE_UNINITIALIZED_MEMORY = Type.getDescriptor(ForceUninitializedMemory.class);
    public static final String STRUCT_FIELD = Type.getDescriptor(StructField.class);
    public static final String STRUCT_TYPE = Type.getDescriptor(StructType.class);
    public static final String TAKE_STRUCT = Type.getDescriptor(TakeStruct.class);
    public static final String SKIP_TRANSFORMATION = Type.getDescriptor(SkipTransformation.class);
}
