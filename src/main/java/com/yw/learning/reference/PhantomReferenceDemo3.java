package com.yw.learning.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 参见：https://blog.csdn.net/xlinsist/article/details/57089288
 * Created by joinwong on 2018/11/8.
 */
public class PhantomReferenceDemo3 {
    private final static ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public static void main(String[] args) {
        BlockFinalization bf = new BlockFinalization();
        PhantomReference<BlockFinalization> prbf = new PhantomReference<>(bf, queue);
        bf = null; // 使BlockFinalization对象变的不可达
        // 我让BlockFinalization对象中的finalize方法睡了1000秒，这样会导致主线程即使结束，finalize方法也不会执行完
        Runtime.getRuntime().gc();

        Person p = new Person();
        PhantomReference<Person> pr = new PhantomReference<>(p, queue);
        p = null; // 使Person对象变的不可达

        // 这次会把Person对象放入到finalization队列
        Runtime.getRuntime().gc();
        waitMoment(2000);
        Runtime.getRuntime().gc();
        waitMoment(2000);
        // 如果这2个对象中的finalize方法不被执行完，它们都不会被回收，根据队列输出的值就可以看出来了
        printReferenceQueue(queue);
    }

    static class Person {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize method in Person");
        }
    }

    private static void waitMoment(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class BlockFinalization {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize method in BlockFinalization");
            Thread.sleep(1000000);
        }
    }

    private static void printReferenceQueue(ReferenceQueue<Object> rq) {
        int size = 0;
        Object obj;
        while ( ( obj = rq.poll() ) != null ) {
            System.out.println(obj);
            size++;
        }
        System.out.println("引用队列大小为： " + size);
    }
}
