package com.yw.learning.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TransmittableThreadLocal
 * git:https://github.com/alibaba/transmittable-thread-local
 * Created by joinwong on 2018/11/12.
 */
public class TransmittableDemo {
    //可以使用线程池，并且保持数据正确传递
    private static final ThreadLocal<String> locals = new TransmittableThreadLocal<String>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(1);

    public static void main(String... args) throws Exception{
        locals.set("Hello");
        String s = locals.get();
        System.out.println("main-start:"+s);

        Runnable runnable = ()->{
            System.out.println("==========");
            System.out.println("runnable-1:"+locals.get());
            locals.set("Runnable");
            System.out.println("runnable-2:"+locals.get());
        };

        pool.submit(TtlRunnable.get(runnable));
        TimeUnit.SECONDS.sleep(1);
        pool.submit(TtlRunnable.get(runnable));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("==========");
        s = locals.get();
        System.out.println("main-end:"+s);

    }
}
