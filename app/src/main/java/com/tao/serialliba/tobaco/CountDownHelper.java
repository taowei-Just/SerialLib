package com.tao.serialliba.tobaco;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019-8-15.
 */

public class CountDownHelper {

    private ExecutorService threadExecutor;

    Map<Integer, CountDownTask> taskMap = new HashMap<>();

    public CountDownHelper() {
        threadExecutor = Executors.newCachedThreadPool();
    }

    static int tag = 0;

    public static int getTag() {
        return tag++;
    }

    public void subTime(long time, int tag, OnTimeCountDown timeCountDown) {
        CountDownTask task = new CountDownTask( this,timeCountDown, time, tag);
        threadExecutor.submit(task);
        taskMap.put(tag, task);
    }

    public void closeTime(int tag) {
        if (taskMap.containsKey(tag)) {
            taskMap.get(tag).close();
            taskMap.remove(tag);
        }
    }

    public interface OnTimeCountDown {
        void OnTime(long time, int tag);
    }


    public static class CountDownTask implements Runnable {
        OnTimeCountDown onTimeCountDown;
        long time;
        boolean close = false;
        int tag;
        CountDownHelper countDownHelper;
        public CountDownTask(CountDownHelper countDownHelper, OnTimeCountDown onTimeCountDown, long time, int tag) {
            this.countDownHelper = countDownHelper;
            this.onTimeCountDown = onTimeCountDown;
            this.time = time;
            this.tag = tag;
        }

        public void close() {
            close = true;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (!close) {
                if (System.currentTimeMillis() - startTime >= time) {
                    countDownHelper.closeTime(tag);
                    onTimeCountDown.OnTime(time, tag);
                    return;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Override
    public String toString() {
        return "CountDownHelper{" +
                "taskMap=" + taskMap +
                '}';
    }

    public static void main(String[] args) {

        final CountDownHelper downHelper = new CountDownHelper();
        for (int i = 1; i < 5; i++) {
            downHelper.subTime(i * 1000, CountDownHelper.getTag(), new OnTimeCountDown() {
                @Override
                public void OnTime(long time, int tag) {
                    System.err.println("时间到 time:" + time + " tag:" +tag  +"  "+ new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(new Date(System.currentTimeMillis())));
                    System.err.println(" " + downHelper.toString());
                }
            });
        }
       

    }
}
