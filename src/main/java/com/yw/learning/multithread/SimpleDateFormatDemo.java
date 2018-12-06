package com.yw.learning.multithread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 参考：https://segmentfault.com/a/1190000010251063
 * Created by joinwong on 2018/11/13.
 */
public class SimpleDateFormatDemo {
    public static void main(String... args) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            DateFormatTask task = new DateFormatTask();
            Future<?> future = threadPool.submit(task); // 将任务提交到线程池

            futures.add(future);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException ex) { // 运行时如果出现异常则进入 catch 块
                System.err.println("执行时出现异常：" + ex.getMessage());
            }
        }

        threadPool.shutdown();
    }

    static class DateFormatTask implements Callable<Void> {
        @Override
        public Void call() throws Exception {
//            String str = DateFormatWrapper.format(
//                    DateFormatWrapper.parse("2017-07-17 16:54:54"));
            String str = DateFormatThreadLocalWrapper.format(DateFormatThreadLocalWrapper.parse("2018-11-13 10:09:28"));
            System.out.printf("Thread(%s) -> %s\n", Thread.currentThread().getName(), str);

            return null;
        }

    }

    /**
     * 1、为 DateFormatWrapper 的 format 和 parse 方法加上 synchronized 关键字，坏处就是前面提到的这会加大线程间的竞争和切换而降低效率；
     * 2、不使用全局的 SimpleDateFormat 对象，而是每次使用 format 和 parse 方法都新建一个 SimpleDateFormat 对象，
     * 坏处也很明显，每次调用 format 或者 parse 方法都要新建一个 SimpleDateFormat，这会加大 GC 的负担；
     * 3、使用 ThreadLocal。ThreadLocal<SimpleDateFormat> 可以为每个线程提供一个独立的 SimpleDateFormat 对象，
     * 创建的 SimpleDateFormat 对象个数最多和线程个数相同，相比于 (1)，使用ThreadLocal不存在线程间的竞争；
     * 相比于 (2)，使用ThreadLocal创建的 SimpleDateFormat 对象个数也更加合理（不会超过线程的数量）。
     */
    private static final class DateFormatWrapper {

        private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // add synchronized
        public static String format(Date date) {
            return SDF.format(date);
        }

        // add synchronized
        public static Date parse(String str) throws ParseException {
            return SDF.parse(str);
        }

    }

    private static final class DateFormatThreadLocalWrapper {

        private static final ThreadLocal<SimpleDateFormat> SDF = new ThreadLocal<SimpleDateFormat>(){
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        };

        // add synchronized
        public static String format(Date date) {
            return SDF.get().format(date);
        }

        // add synchronized
        public static Date parse(String str) throws ParseException {
            return SDF.get().parse(str);
        }

    }
}
