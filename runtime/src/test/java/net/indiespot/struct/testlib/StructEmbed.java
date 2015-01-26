package net.indiespot.struct.testlib;

import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;

/**
 * Created by jason on 1/24/15.
 */
@StructType
public class StructEmbed {
    @StructField(embed = true)
    public Vec3 pos;
    @StructField(embed = true)
    public Vec3 vel;

    @Override
    public String toString() {
        return "StructEmbed[]";
    }
}
