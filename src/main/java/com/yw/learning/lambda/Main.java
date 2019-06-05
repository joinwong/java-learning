package com.yw.learning.lambda;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String... args) {
        Stream.generate(()-> new Random().ints())
                .limit(10)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
