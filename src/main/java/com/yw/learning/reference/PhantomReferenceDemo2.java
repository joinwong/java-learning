package com.yw.learning.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 参见：https://blog.csdn.net/xlinsist/article/details/57089288
 * Created by joinwong on 2018/11/8.
 */
public class PhantomReferenceDemo2 {
    private final static ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public static void main(String[] args) {
        Person p = new Person();
        PhantomReference<Person> pr = new PhantomReference<>(p, queue);
        p = null; // 使Person对象变的不可达

        // 这次gc会把Person对象标记为不可达的，由于它重写了finalize，因此它会被放入到finalization队列
        Runtime.getRuntime().gc();
        waitMoment(2000); // 给gc更多的时间去处理，并且去执行队列中的finalize方法
        Runtime.getRuntime().gc(); // 再次发起gc，收集Person对象
        waitMoment(2000); // 给gc更多的时间去处理
        printReferenceQueue(queue); // 如果Person对象已经被回收，这个队列中应该有值
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
