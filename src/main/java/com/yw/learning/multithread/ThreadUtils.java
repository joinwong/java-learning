package com.yw.learning.multithread;

/**
 * Created by joinwong on 2018/12/6.
 */
public class ThreadUtils {

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
