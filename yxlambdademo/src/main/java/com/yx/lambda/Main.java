package com.yx.lambda;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Note:JDK8+
 */
public class Main {
    public static void main(String... args) {

        prt(System.out::println);

        //prt("Hello",System.out::println);

        //String str = get(()->"GOD");
        //System.out.println(str);

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
