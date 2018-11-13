package com.yw.learning.multithread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun04 on 2018/11/13.
 */
public class ScheduleTestDemo {

    public static void main(String[] args) throws Exception {
//        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

        TimerTask timerTask = new TimerTask(3_000); // 任务需要 2000 ms 才能执行完毕

        System.out.printf("起始时间：%s\n\n", new SimpleDateFormat("HH:mm:ss").format(new Date()));

        // scheduleAtFixedRate 的处理方式为 上一次任务刚完成，则紧接着立即运行下一次任务，
        // 而不是使用线程池中的空闲线程来运行任务以维护 2 秒这个周期 —— 由此可见，
        // 每个定时任务在 ScheduledThreadPoolExecutor 中，都是串行运行的，即下一次运行任务一定在上一次任务结束之后。
//        timer.scheduleAtFixedRate(timerTask, 1_000, 2_000, TimeUnit.MILLISECONDS);

        timer.scheduleWithFixedDelay(timerTask, 1_000, 2_000, TimeUnit.MILLISECONDS);
    }

    private static class TimerTask implements Runnable {

        private final int sleepTime;
        private final SimpleDateFormat dateFormat;

        public TimerTask(int sleepTime) {
            this.sleepTime = sleepTime;
            dateFormat = new SimpleDateFormat("HH:mm:ss");
        }

        @Override
        public void run() {
            System.out.println("thread-name:"+Thread.currentThread().getName());
            System.out.println("任务开始，当前时间：" + dateFormat.format(new Date()));

            try {
                System.out.println("模拟任务运行...");
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

            System.out.println("任务结束，当前时间：" + dateFormat.format(new Date()));
            System.out.println();
        }

    }
}
