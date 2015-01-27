package net.indiespot.struct.testlib;

import net.indiespot.struct.Struct;
import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;
import net.indiespot.struct.api.annotations.TakeStruct;

/**
 * Created by jason on 1/24/15.
 */
@StructType(sizeof = 4 + 12 + 12)
public class PosVelView {
    @StructField
    public int id;

    @TakeStruct
    public Vec3 pos() {
        return Struct.view(this, Vec3.class, 4);
    }

    @TakeStruct
    public Vec3 vel() {
        return Struct.view(this, Vec3.class, 16);
    }

    @Override
    public String toString() {
        return "PosVelView[id=" + id + ", pos=" + pos().toString() + ", vel=" + vel().toString() + "]";
    }
}