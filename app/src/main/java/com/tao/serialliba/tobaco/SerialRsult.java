package com.tao.serialliba.tobaco;

 

/**
 * Created by Administrator on 2019-8-16.
 */

/**
 * 收到结果帧 
 * @param sendResult    发送的指令帧
 * @param dataResult  收到的数据帧
 * @param endResult       收到的结果帧
 * @param  
 */
public class SerialRsult {
    
    CommandResult sendResult;
    CommandResult dataResult;
    CommandResult endResult;
    
    int sendIndex=0;
    int dataIndex;
    int resultIndex;

    public CommandResult getSendResult() {
        return sendResult;
    }

    public void setSendResult(CommandResult sendResult) {
        this.sendResult = sendResult;
    }

    public CommandResult getDataResult() {
        return dataResult;
    }

    public void setDataResult(CommandResult dataResult) {
        this.dataResult = dataResult;
    }

    public CommandResult getEndResult() {
        return endResult;
    }

    public void setEndResult(CommandResult endResult) {
        this.endResult = endResult;
    }

    public int getSendIndex() {
        return sendIndex;
    }

    public void setSendIndex(int sendIndex) {
        this.sendIndex = sendIndex;
    }

    public int getDataIndex() {
        return dataIndex;
    }

    public void setDataIndex(int dataIndex) {
        this.dataIndex = dataIndex;
    }

    public int getResultIndex() {
        return resultIndex;
    }

    public void setResultIndex(int resultIndex) {
        this.resultIndex = resultIndex;
    }

    @Override
    public String toString() {
        return "SerialRsult{" +
                "sendResult=" + sendResult +
                ", dataResult=" + dataResult +
                ", endResult=" + endResult +
                ", sendIndex=" + sendIndex +
                ", dataIndex=" + dataIndex +
                ", resultIndex=" + resultIndex +
                '}';
    }
}
