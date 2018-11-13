package com.yw.learning.multithread;

import java.util.concurrent.*;

/**
 * Future cancel
 * https://segmentfault.com/a/1190000007961347
 * Created by wangjun04 on 2018/11/13.
 */
public class FutureTestDemo extends FutureBase{
    public static void main(String... args) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        SimpleTask task = new SimpleTask(3_000); //sleep 3s
        Future<Double> f = pool.submit(task);
        pool.shutdown(); //关闭线程池

        cancelTask(f, 2_000); // 在 2 秒之后取消该任务

        try {
            double time = f.get();
            System.out.format("任务运行时间：%.3f s\n", time);
        } catch (CancellationException ex) {
            System.out.println("任务被取消");
        } catch (InterruptedException ex) {
            System.out.println("当前线程被中断");
        } catch (ExecutionException ex) {
            System.out.println("任务执行出错");
        }
    }

    private static final class SimpleTask implements Callable<Double> {
        private final int sleepTime; //ms

        public SimpleTask(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Double call() throws Exception {
            long begin = System.nanoTime();

            Thread.sleep(sleepTime);

            long end = System.nanoTime();

            double time = (end-begin) / 1e9;

            return time; //s
        }
    }

}
