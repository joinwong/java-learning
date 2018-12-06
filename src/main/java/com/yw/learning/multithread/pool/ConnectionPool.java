package com.yw.learning.multithread.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * Created by joinwong on 2018/12/6.
 */
public class ConnectionPool {
    private final LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initSize) {
        if(initSize > 0) {
            for(int i=0;i<initSize;i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection){
        if(connection != null) {
            synchronized (pool) {
                //链接释放后需要通知，这样其他链接能感知到连接池中新增一个链接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }

    /**
     * 在mills内无法获取链接，会返回null
     * @param mills
     * @return
     * @throws InterruptedException
     */
    public Connection fetchConnection(long mills) throws InterruptedException{
        synchronized (pool) {
            //完全超时
            if(mills <=0 ) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long feature = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(mills);
                    remaining = feature - System.currentTimeMillis();
                }

                Connection result = null;
                if(!pool.isEmpty()) {
                    result = pool.removeFirst();
                }

                return result;
            }
        }
    }
}
