package com.tao.serial.task;

import com.tao.protocol.ICmdParse;
import com.tao.serial.ISerial;
import com.tao.serial.SerialData;
import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019-8-13.
 */

public class AsyncSerialParseTask<T> extends BaseTask {
    private ICmdParse parse;
    private String tag = getClass().getSimpleName();
    private AsyncSerialReceiverTask receiveTask;
    private ISerial serial;

    public AsyncSerialParseTask( ICmdParse parse) {
        this.parse = parse;
    }

    public ISerial getSerial() {
        return serial;
    }

    public void setSerial(ISerial serial) {
        this.serial = serial;
    }

    @Override
    public void run() {

        while (true) {
            byte[] bytes = new byte[0];
            try {
                bytes = receiveTask.operateBuff(null, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.e(tag, " 处理器 ：" + ParseSystemUtil.parseByte2HexStr(bytes));

            if (bytes == null || bytes.length == 0) {
                synchronized (serial) {
                    try {
                        serial.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    receiveTask.operateBuff(parse(bytes), 1 );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       
    }

    @Override
    public void close() {
        close = true;
    }


    private byte[]  parse(byte[] buff) {
        // 解析出头一条数据并分发处理
//        LogUtil.e(tag , " buff " + ParseSystemUtil.parseByte2HexStr(buff));
        byte[] head = parse.head();
        byte[] end = parse.end();
        if (buff.length < head.length + end.length) {
            return buff;
        }
        // 接收指标
        int[] indexS = new int[buff.length / (head.length + end.length)];
        for (int i = 0; i < indexS.length; i++) {
            indexS[i] = -1;
        }
        int index = -1;
        // 查询指标
        for (int i = 0; i < buff.length - (head.length + end.length); i++) {
            boolean match = matchHead(buff ,head, i);
            if (!match)
                continue;
            indexS[++index] = i;
        }

        // 提取指令
        int[] lenS = new int[indexS.length];
        for (int i = 0; i < indexS.length; i++) {
            lenS[i] = -1;
            if (indexS[i] < 0)
                continue;
            byte[] data01 = new byte[0];

            try {
                data01 = parse.matchFrameData(indexS[i], buff);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(tag, e.toString());
            }
            if (data01 == null || data01.length == 0)
                continue;
            // 分发指令
            lenS[i] = data01.length;
        }

       return  subBuff(buff, indexS, lenS);
        
    }

    private byte[] subBuff(byte[] buff, int[] indexS, int[] lenS) {
        byte[] cache = new byte[buff.length];
        System.arraycopy(buff, 0, cache, 0, cache.length);
        int subs = 0;
        for (int i = indexS.length - 1; i >= 0; i--) {
            if (indexS[i] >= 0 && lenS[i] >= 0) {
                subs = indexS[i] + lenS[i];
                break;
            }
        }
        byte[] cache2 = new byte[cache.length - subs];
        System.arraycopy(cache, subs, cache2, 0, cache2.length);

        return cache2;
    }

    private boolean matchHead(byte[] buff ,byte[] head, int i) {
        boolean match = false;
        for (int i1 = 0; i1 < head.length; i1++) {
            if (buff[i + i1] == head[i1]) {
                match = true;
            } else {
                match = false;
                break;
            }
        }
 
        return match;
    }

    public void setReceiveTask(AsyncSerialReceiverTask task) {
        receiveTask = task;
    }

    public static void main(String[] args) {
        final Object o = new Object();
        System.err.println("=========================");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (o) {
                        System.err.println(" " + Thread.currentThread().toString() + "  wait " + System.currentTimeMillis());
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                while (true) {
                    if (++i < 2000) {
                        System.err.println("run " + i);
                        continue;
                    }
                    i = 0;
                    synchronized (o) {
                        System.err.println(" " + Thread.currentThread().toString() + "  notify " + System.currentTimeMillis());
                        o.notify();
                    }

                }
            }
        }).start();


    }
}
