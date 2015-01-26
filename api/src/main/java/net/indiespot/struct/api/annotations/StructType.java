package net.indiespot.struct.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface StructType {
    int sizeof() default -1;
}
