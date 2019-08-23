package com.tao.serial.task;

/**
 * Created by Administrator on 2019-8-13.
 */

public abstract class BaseTask implements Runnable {
    public  boolean close  =false;
    public abstract void   close();
}
