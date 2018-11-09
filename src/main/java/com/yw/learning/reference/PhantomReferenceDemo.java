package com.yw.learning.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 参见：https://blog.csdn.net/xlinsist/article/details/57089288
 * Created by joinwong on 2018/11/8.
 */
public class PhantomReferenceDemo {
    private final static ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public static void main(String... args) {
        Person p1 = new Person("小明");
        Person p2 = new Person("小花");
        Animal a1 = new Animal(p1, "dog");
        Animal a2 = new Animal(p2, "cat");
        //一旦Person 对象被回收，那么指向它的Animal 对象就会被放进队列中
        p1 = null;
        p2 = null;
        Runtime.getRuntime().gc();
        waitMoment(2000); // 给gc点时间收集，有时gc收集速度很快，可以不用加这句代码，我只不过是保险起见
        printReferenceQueue(queue);
    }

    static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    static class Animal extends PhantomReference<Object>  {
        private String name;
        public Animal(Person referent, String name) {
            super(referent, queue);
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    private static void waitMoment(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printReferenceQueue(ReferenceQueue<Object> rq) {
        int size = 0;
        Object obj;
        while ( ( obj = rq.poll() ) != null ) {
            Animal a = (Animal) obj;
            System.out.println(a.getName());
            size++;
        }
        System.out.println("引用队列大小为： " + size);
    }

}
