package com.tao.serial;

import com.tao.serial.task.AsyncSerialParseTask;
import com.tao.serial.task.AsyncSerialReceiverTask;
import com.tao.serial.task.AsyncSerialSendTask;
import com.tao.utilslib.log.LogUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ASyncSerial extends BaseSerial<SerialData> {
    private   ExecutorService sendPool;
    private ExecutorService receiverPool;
    private SerialCall serialCall;
    private String tag = getClass().getSimpleName();
    private AsyncSerialParseTask parseTask ;
    private AsyncSerialReceiverTask task;

    public ASyncSerial(String serialPath, int baudrate, AsyncSerialParseTask parseTask , SerialCall serialCall ) throws Exception {
        super(serialPath, baudrate);
        this.serialCall = serialCall;
        this.parseTask = parseTask;
        this. parseTask.setSerial(this);
        receiverPool = Executors.newFixedThreadPool(2);
        sendPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void init() throws Throwable {
        super.init();
        task = new AsyncSerialReceiverTask (this);
        receiverPool.submit(task);
        receiverPool.submit(parseTask);
        parseTask.setReceiveTask(task);
    }


    public void send(SerialData data) {
        try {
            AsyncSerialSendTask task = new AsyncSerialSendTask<>(this, data, serialCall);
            sendPool.submit(task);
        } catch (Exception e) {
            e.printStackTrace();
            serialCall.onSendError(data);
        }
    }

    public void send(List<SerialData> dataS) {
        for (SerialData data : dataS) {
            send(data);
        }
        LogUtil.e(tag, " thread  over ");
    }


    @Override
    public void close() {
        closed = true;
        super.close();
        receiverPool.shutdownNow();
        receiverPool = Executors.newFixedThreadPool(2);
        closed = false;
    }

    boolean closed = false;
     
    @Override
    public void onOver(Runnable runnable) {
    }

    @Override
    public void sigleReceiver(SerialData serialData) {

    }
    public void setSerialCall(SerialCall serialCall) {
        this.serialCall = serialCall;
    }

}
