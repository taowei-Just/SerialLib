package com.tao.serial.task;

import com.tao.serial.ISerial;
import com.tao.serial.SerialCall;
import com.tao.serial.SerialData;
import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

/**
 * Created by Administrator on 2019-8-13.
 */

public class AsyncSerialSendTask<T extends SerialData> extends BaseTask {

    ISerial serial;
    SerialCall serialCall;
    T data;

    String tag = getClass().getSimpleName();

    public AsyncSerialSendTask(ISerial serial, T data, SerialCall serialCall) {
        this.data = data;
        this.serial = serial;
        this.serialCall = serialCall;
    }

    @Override
    public void run() {
//        LogUtil.e(tag , " run ");
        if (close)
            return;
        try {
            serial.send(data.getData());
            LogUtil.e(tag, "发送：" + ParseSystemUtil.parseByte2HexStr(data.getData()));

            serialCall.onSendSuccess(data);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            serialCall.onSendError(data);

        }
    }


    @Override
    public void close() {
        close = true;
    }
}
