package com.yw.learning.multithread.pool;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by joinwong on 2018/12/6.
 */
public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    //保证所有runner能同时开始
    static CountDownLatch start = new CountDownLatch(1);
    static CountDownLatch end;

    public static void main(String... args) throws Exception {
        //线程数量
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Runner(count,got,notGot));
            thread.start();
        }
        start.countDown();
        end.await();

        System.out.println("total Invoke:" + (threadCount * count));
        System.out.println("got Connection:" + got);
        System.out.println("NotGot Connection:" + notGot);

        System.out.println(start.getCount());
        System.out.println(end.getCount());
    }

    @AllArgsConstructor
    static class Runner implements Runnable {
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        @Override
        public void run() {
            try{
//                System.out.println("start:"+start.getCount());
                start.await();
            }catch (Exception ex){
                ex.printStackTrace();
            }

            while (count > 0) {
                try{
                    //从线程池获取链接，如果1000ms内无法获取，则返回null
                    //统计got和notGot数量
                    Connection connection = pool.fetchConnection(1000);
                    if(connection != null) {
                        try{
                            connection.createStatement();
                            connection.commit();
                        }finally {
                            pool.releaseConnection(connection);
                            got.getAndIncrement();
                        }
                    } else {
                        notGot.getAndIncrement();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }finally {
                    count--;
                }
            }

            end.countDown();
        }
    }
}
