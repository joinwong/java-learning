package com.yw.learning.multithread;

/**
 * Volatile
 * 1、保证内存 可见性
 * 2、防止指令 重排序
 * 3、保证对 64 位变量 读写的原子性
 * Created by joinwong on 2018/11/13.
 */
public class VolatileDemo {
    // 1、每次对该变量的写操作，都将立即同步到主存；
    // 2、每次对该变量的读操作，都将从主存读取，而不是线程栈
    private static volatile boolean running = true;

    public static class AnotherThread extends Thread {

        @Override
        public void run() {
            System.out.println("AnotherThread is running");

            while (running) { }

            System.out.println("AnotherThread is stoped");
        }
    }

    public static void main(String... args) throws Exception{
        new AnotherThread ().start();

        Thread.sleep(1000);
        running = false;  // 1 秒之后想停止 AnotherThread
    }
}
