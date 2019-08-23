package com.tao.serialliba.tobaco;

/**
 * Created by Administrator on 2019-8-15.
 */

public interface Iui {
    // 显示串口交互数据
    void showText(String str);

    //  回调结果
    void onResult(SerialRsult serialRsult) throws Exception;

    // 指令发送失败
    void onCmdSendFaile(SerialRsult serialRsult) throws Exception;

    // 指令等待超时
    void onCmdTimeOut(SerialRsult serialRsult);
}
