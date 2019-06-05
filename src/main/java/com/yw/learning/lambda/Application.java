package com.yw.learning.lambda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * jdk8+
 * Created by joinwong on 2018/11/8.
 */
public class Application {
    public static void main(String... args) {
        prt(System.out::println);

        ArrayList a = new ArrayList();
        LinkedList l = new LinkedList();
        Vector<String> v = new Vector<>();

        System.out.println(test());
    }

    private static String test(){
        try{
            throw new Exception("xx");
//            return "1";
        } catch (Exception ex){
            return "2:exception";
        }finally {
            return "3:finally";
        }
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
