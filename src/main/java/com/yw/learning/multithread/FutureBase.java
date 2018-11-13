package com.yw.learning.multithread;

import java.util.concurrent.Future;

/**
 * Created by wangjun04 on 2018/11/13.
 */
public class FutureBase {

    public static void cancelTask(final Future<?> future, final int delay) {
        Runnable runnable = ()->{
            try{
                Thread.sleep(delay);
                future.cancel(true); //取消与 future 关联的正在运行的任务
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        };

        new Thread(runnable).start();
    }
}
