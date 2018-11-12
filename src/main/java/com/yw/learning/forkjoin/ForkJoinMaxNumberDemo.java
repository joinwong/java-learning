package com.yw.learning.forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join 例子
 * Created by wangjun04 on 2018/11/12.
 */
public class ForkJoinMaxNumberDemo{
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String... args) throws Exception {
        int[] array = generate(1000000);

        normal(array);
        fork(array);
    }

    private static int[] generate(int max) throws Exception{
        int[] array = new int[max];
        for (int i = 0; i < max; i++) {
            array[i] = random.nextInt(max);
        }

        return array;
    }

    private static void fork(int[] array) throws Exception{
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(2);
        MaxNumber task = new MaxNumber(array, 0, array.length - 1);
        Future f = pool.submit(task);

        int max = (int)f.get(10, TimeUnit.SECONDS);

        System.out.println("fork:"+(System.currentTimeMillis()-start)+"ms");
        System.out.println(max);

        pool.shutdown(); //向线程池发送关闭的指令
    }

    private static void normal(int[] array) throws Exception {
        long start = System.currentTimeMillis();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
//            TimeUnit.MILLISECONDS.sleep(1);
            max = Math.max(max, array[i]);
        }

        System.out.println("normal:" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(max);
    }
}

class MaxNumber extends RecursiveTask<Integer> {
    // 任务分割最小值，对 ForkJoinPool 执行任务的效率有着至关重要的影响。
    // 临界值选取过大，任务分割的不够细，则不能充分利用 CPU；
    // 临界值选取过小，则任务分割过多，可能产生过多的子任务，导致过多的线程间的切换和加重 GC 的负担从而影响了效率。
    // 所以，需要根据实际的应用场景选择一个合适的分割任务的临界值。
    private int threshold = 100000;

    private int[] array;

    private int start = 0;
    private int end = 0;

    public MaxNumber(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute(){
        int max = Integer.MIN_VALUE;

        if((end-start) <= threshold) {
            for (int i = start; i <= end; i++) {
                /*try {
                    TimeUnit.MILLISECONDS.sleep(1);
                }catch (Exception ex){

                }*/
                max = Math.max(max, array[i]);
            }
        } else {
            //fork
            int mid = start + (end- start)/2;
            MaxNumber lMax = new MaxNumber(array,start,mid);
            MaxNumber rMax = new MaxNumber(array,(mid+1),end);

            lMax.fork();
            rMax.fork();

            int lRet = lMax.join();
            int rRet = rMax.join();


            max = Math.max(lRet,rRet);
        }

        return max;
    }
}
