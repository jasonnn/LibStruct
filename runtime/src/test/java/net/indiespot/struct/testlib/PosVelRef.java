package net.indiespot.struct.testlib;

import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;

/**
 * Created by jason on 1/24/15.
 */
@StructType
public class PosVelRef {
    @StructField
    public int id;
    @StructField
    public Vec3 pos;
    @StructField
    public Vec3 vel;

    @Override
    public String toString() {
        return "PosVelRef[id=" + id + ", pos=" + pos.toString() + ", vel=" + vel.toString() + "]";
    }
}
