package net.indiespot.struct.testlib;

import net.indiespot.struct.api.annotations.StructField;
import net.indiespot.struct.api.annotations.StructType;

/**
 * Created by jason on 1/24/15.
 */
@StructType
public class Ship {
    private static int id_gen = 100000;
    @StructField
    public int id;
    @StructField
    public Vec3 pos;

    public Ship() {
        id = ++id_gen;// new Random().nextInt(); // FIXME
        // id = new Random().nextInt(); // FIXME
    }

    @Override
    public String toString() {
        return "Ship[id=" + id + ", pos=" + pos + "]";
    }
}
