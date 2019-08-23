package com.tao.serial;

import android.serialport.SerialPort;
import android.text.TextUtils;

import com.tao.protocol.ICmdParse;
import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class BaseSerial<T extends SerialData> implements ISerial {

    private SerialPort serialPort;
    private String serialPath;
    private int baudrate;
    private InputStream inputStream;
    private OutputStream outputStream;
    private long wattingTime = 50;
    boolean brock = false;
    private String tag = getClass().getSimpleName();
    static int id = 0;
    int defaultLen = 100;

    public static int getId() {
        return ++id;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public String getSerialPath() {
        return serialPath;
    }

    public BaseSerial(String serialPath, int baudrate) throws Exception {
        if (TextUtils.isEmpty(serialPath))
            throw new Exception("sorry input serialPath is invalid");
        this.serialPath = serialPath;
        this.baudrate = baudrate;
    }


    public void init(int flag) throws Throwable {

        close();
        serialPort = new SerialPort(serialPath, baudrate, flag);
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
        brock = false;
    }

    @Override
    public void init() throws Throwable {
        init(0);
    }

    @Override
    public void send(byte[] data) throws Throwable {
//        LogUtil.e(tag, " send byte ");
//        boolean selialUse = isSelialUse();
//        boolean chechStream = chechStream();
//        LogUtil.e(tag , " selialUse " + selialUse + " chechStream " + chechStream);

        if (!chechStream() && !isSelialUse()) {
            init();
        }
        brock = false;
        outputStream.flush();
//        LogUtil.e(tag, " serial send " + ParseSystemUtil.parseByte2HexStr(data));
        outputStream.write(data, 0, data.length);
    }

    private boolean chechStream() {
//        LogUtil.e(tag, " chechStream ");

        try {
            outputStream.write(new byte[]{0});
//            LogUtil.e(tag, "  chechStream stream wrte " + 0);
            TimeUnit.MILLISECONDS.sleep(50);
            return true;

        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void flashInput() {
//        LogUtil.e(tag, " flashInput ");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int len = 0;
        try {
            byte[] buff;
            while ((len = inputStream.available()) > 0) {
                buff = new byte[len];
                inputStream.read(buff, 0, len);

                String s = ParseSystemUtil.parseByte2HexStr(buff);
//                LogUtil.e(tag, "  flash stream read " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] receiver() throws Throwable {
        return receiver(defaultLen, 50);
    }


    @Override
    public byte[] receiver(int len, long time) throws Throwable {

        byte[] data = new byte[len];


        if (!isSelialUse())
            init();
        try {
            Thread.sleep(wattingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long startTimeMillis = System.currentTimeMillis();
        int lenth = 0;
        int totalLen = 0;
        byte[] buff;

        while (!brock && (System.currentTimeMillis() - startTimeMillis) < time && ((lenth = inputStream.available()) + totalLen) < len) {
            if (lenth == 0)
                continue;

//            LogUtil.e(tag, " while " + lenth);

            buff = new byte[lenth];
            inputStream.read(buff, 0, lenth);
            System.arraycopy(buff, 0, data, totalLen, buff.length);
            totalLen += lenth;
        }
        if (totalLen < len && (lenth = inputStream.available()) > 0) {
            buff = new byte[(len - totalLen)];
            inputStream.read(buff, 0, buff.length);
            System.arraycopy(buff, 0, data, totalLen, buff.length);
            totalLen += lenth;
        }
        if (totalLen == 0)
            return null;

        buff = new byte[totalLen];
        System.arraycopy(data, 0, buff, 0, buff.length);
        LogUtil.e(tag, "total len " + totalLen + " serial receiver " + ParseSystemUtil.parseByte2HexStr(buff));
        return buff;
    }

    @Override
    public void close() {
        breakOff();
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (IOException e) {
            }
        }
        if (null != serialPort)
            serialPort.close();

        inputStream = null;
        outputStream = null;
        serialPort = null;

    }

    @Override
    public void reset() throws Throwable {
        close();
        init();
    }

    @Override
    public boolean isSelialUse() {
//        LogUtil.e(tag, " isSelialUse ");

        return null != serialPort &&
                null != inputStream &&
                null != outputStream;
    }

    @Override
    public void breakOff() {
        brock = true;
    }

    @Override
    public void switchOn() {
        brock = false;
    }
}
