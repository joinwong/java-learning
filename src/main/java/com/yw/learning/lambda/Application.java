package com.yw.learning.lambda;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * jdk8+
 * Created by joinwong on 2018/11/8.
 */
public class Application {
    public static void main(String... args) {
        prt(System.out::println);
    }

    private static void prt(String str, Consumer<String> action) {
        action.accept(str);
    }

    private static void prt(Consumer<String> action) {
        action.accept("Hello");
    }

    private static String get(Supplier<String> action) {
        return action.get();
    }
}
