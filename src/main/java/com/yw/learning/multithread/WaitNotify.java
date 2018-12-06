package com.yw.learning.multithread;

import java.time.LocalDateTime;

/**
 * Created by joinwong on 2018/12/6.
 */
public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String... args) {
        Thread waitThread = new Thread(new Wait(),"WaitThread");
        waitThread.start();
        ThreadUtils.sleep(5000);

        Thread notifyThread = new Thread(new Notify(),"NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                //条件不满足
                while (flag) {
                    try{
                        System.out.println(Thread.currentThread().getName()+" flag is true wait@"+(LocalDateTime.now() ));
                        lock.wait();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                //条件满足
                System.out.println(Thread.currentThread().getName()+" flag is false running@"+(LocalDateTime.now() ));

            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            //加锁，拥有lock monitor
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName()+" hold lock notify@"+(LocalDateTime.now() ));
                //lock.notifyAll();
                lock.notify();
                flag = false;
                ThreadUtils.sleep(5000);
            }

            //再次加锁
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName()+" hold lock again. sleep@"+(LocalDateTime.now() ));
                ThreadUtils.sleep(5000);
            }
        }
    }
}
