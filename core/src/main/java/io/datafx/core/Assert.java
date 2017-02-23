package io.datafx.core;

import java.util.Objects;

/**
 * Created by hendrikebbers on 23.02.17.
 */
public class Assert {

    public static <T> T requireNonNull(T param, String name) {
        return Objects.requireNonNull(param, "Value " + name + " should not be null!");
    }

}
