package com.tao.serial.task;

import com.tao.serial.ISerial;
import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019-8-13.
 */

public class AsyncSerialReceiverTask extends BaseTask {
    ISerial serial;
    private String tag = getClass().getSimpleName();
    byte[] buff = new byte[0];

    public AsyncSerialReceiverTask(ISerial serial) {
        this.serial = serial;
    }

    @Override
    public void run() {
        serial.switchOn();
        while (!close) {
            try {
                byte[] receiver = serial.receiver();
                if (receiver == null || receiver.length == 0) {
                    TimeUnit.MILLISECONDS.sleep(50);
                    continue;
                }
                LogUtil.e(tag, "接收器" + ParseSystemUtil.parseByte2HexStr(receiver));
                saveBuff(receiver);
                TimeUnit.MILLISECONDS.sleep(50);
                synchronized (serial) {
                    serial.notify();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                LogUtil.e(tag, " " + throwable.toString());
                try {
                    serial.init();
                } catch (Throwable throwable1) {
                    throwable1.printStackTrace();
                }
            }
        }
    }

    private void saveBuff(byte[] data) {
        byte[] cacheData = new byte[buff.length + data.length];
        System.arraycopy(buff, 0, cacheData, 0, buff.length);
        System.arraycopy(data, 0, cacheData, buff.length > 0 ? buff.length : 0, data.length);
        try {
            operateBuff(cacheData, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步方法 只允许单个线程对数据进行操作
     *
     * @param data    数据源
     * @param operate 写入方式 -1 不追加数据直接返回数据，  0 追加到尾部 ，1 追加到头部
     * @return 返回处理后的数据
     */

    public synchronized byte[] operateBuff(byte[] data, int operate) throws Exception{
        synchronized (AsyncSerialReceiverTask.class) {
            if (data == null || data.length == 0 || operate == -1) {
                byte[] bytes = new byte[buff.length];
                System.arraycopy(buff, 0, bytes, 0, buff.length);
                buff = new byte[0];
                return bytes;
            }
            byte[] cache = new byte[buff.length + data.length];
            if (operate == 1) {
                System.arraycopy(data, 0, cache, 0, data.length);
                System.arraycopy(buff, 0, cache, data.length, buff.length);
                buff = cache;
            } else {
                System.arraycopy(buff, 0, cache, 0, buff.length);
                System.arraycopy(data, 0, cache, buff.length, data.length);
                buff = cache;
            }
            return buff;
        }
    }


    @Override
    public void close() {
        close = true;
    }


}
