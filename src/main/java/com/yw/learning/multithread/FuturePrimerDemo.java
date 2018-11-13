package com.yw.learning.multithread;

import java.util.concurrent.*;

/**
 * 调用 Future 的 cancel(true) 就一定能取消正在运行的任务吗
 * 原来 cancel(true) 方法的原理是向正在运行任务的线程发送中断指令 —— 即调用运行任务的 Thread 的 interrupt() 方法。
 * 所以 如果一个任务是可取消的，那么它应该可以对 Thread 的 interrupt() 方法做出被取消时的响应。
 * https://segmentfault.com/a/1190000007961347
 * Created by wangjun04 on 2018/11/13.
 */
public class FuturePrimerDemo extends FutureBase{
    public static void main(String... args) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();

        long num = 1000000033L;
        PrimerTask task = new PrimerTask(num);
        Future<Boolean> f = pool.submit(task);
        pool.shutdown();

        cancelTask(f, 2_000);

        try {
            boolean ret = f.get();
            System.out.format("%d 是否为素数？%b\n", num, ret);
        } catch (CancellationException ex) {
            //任务被取消，但是任务并没有停止，而是运行13秒后才结束
            System.err.println("任务被取消");
        } catch (InterruptedException ex) {
            System.err.println("当前线程被中断");
        } catch (ExecutionException ex) {
            System.err.println("任务执行出错");
        }
    }

    /**
     * 素数：只能被1和自身整除
     */
    private static final class PrimerTask implements Callable<Boolean> {
        private final long num;

        public PrimerTask(long num) {
            this.num = num;
        }

        @Override
        public Boolean call() throws Exception {
            for (long i = 2; i < num; i++) {
                //如果在这里进行isInterrupted判断，则可以实时结束线程
                if (Thread.currentThread().isInterrupted()) { // 任务被取消
                    System.out.println("PrimerTask.call： 你取消我干啥？");
                    return false;
                }

                if (num % i == 0)
                    return false;
            }

            return true;
        }
    }
}
