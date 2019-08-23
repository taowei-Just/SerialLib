package com.tao.serialliba.tobaco;

import android.text.TextUtils;

import com.tao.protocol.ICmdParse;
import com.tao.serial.ASyncSerial;
import com.tao.serial.SerialCall;
import com.tao.serial.SerialData;
import com.tao.serial.task.AsyncSerialParseTask;
import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

/**
 * Created by Administrator on 2019-8-15.
 */

public class TobaccoCmdHelper {
    private   CountDownHelper countDownHelper;
    private String tag = getClass().getSimpleName();
    private SerialData lastSendData;
    private boolean cmdSend = false;
    private ASyncSerial asyncSerial;
    private MyCall serialCall;
    private int waitAskTag;
    private MyPrase parse;
    private Iui iui;

    String serialPath = "/dev/ttyS2";
    int baudrate = 115200;


    public TobaccoCmdHelper(String serialPath, int baudrate) {
        if (!TextUtils.isEmpty(serialPath))
            this.serialPath = serialPath;
        if (baudrate > 0)
            this.baudrate = baudrate;
        try {
            serialCall = new MyCall(iui);
            parse = new MyPrase(iui, this);
            asyncSerial = new ASyncSerial(this.serialPath, this.baudrate,new AsyncSerialParseTask(parse), serialCall );
            asyncSerial.init();
            countDownHelper = new CountDownHelper();

            LogUtil.e(tag, "打开串口成功：" + asyncSerial.getSerialPath() + "  " + asyncSerial.getBaudrate());
            if (null != iui)
                iui.showText("打开串口成功：" + asyncSerial.getSerialPath() + "  " + asyncSerial.getBaudrate());

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if (null != iui) iui.showText("初始化失败！");
        }
    }

    public void setIui(Iui iui) {
        LogUtil.e(tag, "setiui 1 ");
        LogUtil.e(tag, " " + this);

        LogUtil.e(tag, " serialCall " + serialCall);
        LogUtil.e(tag, " parse " + parse);

        this.iui = iui;
        serialCall.seIui(iui);
        parse.seIui(iui);
    }

    //出货失败时间控制
    public void fail_time_set(int time) {
        senddata(TobacoProtocol.fail_time_set(time));
    }

    //特殊货道位置微调
    public void spc_pos_adjust(int channel, int dis, int offeset) {
//        senddata(TobacoProtocol.spc_pos_adjust(channel, dis, offeset));
    }

    //  Z轴翻转背面时，货道X轴OFFSET
    public void z_rotate_pos_offset(int dis, int offeset) {
//        senddata(TobacoProtocol.z_rotate_pos_offset(dis, offeset));
    }

    /**
     * 补货
     *
     * @param channel 补货货道
     */
    public void replenishment(int channel) {
        senddata(TobacoProtocol.autoReplenishment(channel));
    }

    /**
     * 出货
     *
     * @param channel 出货货道
     */
    public void outGoods(int channel) {
        try {
            senddata(TobacoProtocol.deliverFromGodown(channel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置通讯
     */
    public void resetCommunication() {
        senddata(TobacoProtocol.resetCommunication());
    }

    /**
     * 获取版本
     */
    public void version() {
        senddata(TobacoProtocol.GET_VERSIONS());
    }

    /**
     * 主控握手
     */
    public void bootreset() {
        senddata(TobacoProtocol.bootReady());
    }

    /**
     * 开关 设备
     *
     * @param deviceType 设备类型
     * @param index      设备编号
     * @param b          状态设置
     */

    public void operateDevice(DeviceType deviceType, int index, boolean b) {
        senddata(TobacoProtocol.openDevices(deviceType, index, b));
    }

    /**
     * 设置led 亮度等级
     *
     * @param light 亮度等级
     */

    public void light_s(int light) {
        try {
            senddata(TobacoProtocol.ledLight(light));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 补货之前发送此指令
     * 补货模块运动到补货口
     */
    public void activeReplenishmentMoudle() {
        senddata(TobacoProtocol.activeReplenishmentMoudle());
    }

    /**
     * 设置设备运动到指定位置
     *
     * @param n 设备
     * @param x x坐标
     * @param y y坐标
     */

    public void activeMotor(int n, int x, int y) {
        try {
            senddata(TobacoProtocol.step_motor_move_tar(n, x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 补货电机微调
     *
     * @param z 轴
     * @param l 位置
     * @param d 方向
     * @param n 微调量  单位mm  最大值 65535mm
     */

    public void motorFine(int z, int l, int d, int n) {
        try {
            senddata(TobacoProtocol.fine_tuning(z, l, d, n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void door_state(int i) {
        try {
            senddata(TobacoProtocol.getDoorStatus(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取货道状态
     * 返回100 byte 数据帧
     */
    public void channel_state() {
        senddata(TobacoProtocol.get_channel_state());
    }

    private void senddata(byte[] data2) {
        SerialData data = new SerialData();
        data.setData(data2);
        data.setId(ASyncSerial.getId());
        if (cmdSend) {
            serialCall.onCmdHandleing(data);
            return;
        }
        cmdSend = true;
        asyncSerial.send(data);
    }

    public void repeatData(CmdType cmdType) {
        resetSend();
        senddata(TobacoProtocol.replyFrame(cmdType));
    }

    public boolean isCmdSend() {
        return cmdSend;
    }

    public void resetSend() {
        this.cmdSend = false;
    }

    // 提交出货
    public void submitOutGoods(SerialData data) {
        if (data == null)
            return;
        senddata(data.getData());
    }

    public void resetMotor() {
        senddata(TobacoProtocol.resetMotor(4));
    }

    public void clearCall() {
        setIui(null);
    }

    public void closeWarehouse() {
        senddata(TobacoProtocol.pn_motor_move(1, 0));
    }

    public void closeLight() {
        senddata(TobacoProtocol.openDevices(DeviceType.LED_REBBON, 0, false));
    }

    public void checkChannelStatus() {
        senddata(TobacoProtocol.get_channel_state());
    }

    public void openDoor() {
        operateDevice(DeviceType.DOOR, 0, true);
    }

    class MyCall implements SerialCall {
        Iui iui;

        public MyCall(Iui iui) {
            this.iui = iui;
        }

        @Override
        public void onInitError(String path, int bound) {
            LogUtil.e(tag, "onInitError " + path + "  " + bound);
            if (null != iui) iui.showText("onInitError " + path + "  " + bound);
        }

        @Override
        public void onSendError(SerialData data) {
            LogUtil.e(tag, "指令发送失败 " + data.toString());
            resetSend();

            if (null != iui) iui.showText("onSendError " + data.toString());
            CommandResult lastSend = TobacoProtocol.praceata(lastSendData.getData());
            SerialRsult serialRsult = new SerialRsult();
            serialRsult.setSendResult(lastSend);

            serialRsult.setDataResult(parse.lastDataFrameResult);
            serialRsult.setDataIndex(parse.lastDataFrameIndex);
            if (null != iui) try {
                iui.onCmdSendFaile(serialRsult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetSend();
        }

        @Override
        public void onSendSuccess(SerialData data) {
            LogUtil.e(tag, "指令发送成功 " + data.toString());
            if (null != iui) iui.showText("数据发送 " + data.toString());
            // 初始化等待时间 
            CommandResult lastSend = TobacoProtocol.praceata(data.getData());
            LogUtil.e(tag, "发送指令类型 " + lastSend.toString());

            if (lastSend.getCmdType()[0] == CmdType.CMD) {
                lastSendData = data;
                waitAsk(2 * 1000);
            }
        }
    
        @Override
        public void onUnInit(String path, int bound) {
            LogUtil.e(tag, "onUnInit " + path + "  " + bound);
            if (null != iui) iui.showText("onUnInit " + path + "  " + bound);
        }

        public void onCmdHandleing(SerialData data) {
            LogUtil.e(tag, "onCmdHandleing " + data.toString());
            if (null != iui) iui.showText(" 指令未发送  " + data.toString());
            CommandResult lastSend =null ;
            if (lastSendData !=null)
                lastSend = TobacoProtocol.praceata(lastSendData.getData());
            SerialRsult serialRsult = new SerialRsult();
            serialRsult.setSendResult(lastSend);
            serialRsult.setDataResult(parse.lastDataFrameResult);
            serialRsult.setDataIndex(parse.lastDataFrameIndex);

            if (null != iui) try {
                iui.onCmdSendFaile(serialRsult);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void seIui(Iui iui) {
            LogUtil.e(tag, " setiui  2" + iui);
            this.iui = iui;
        }
    }


    class MyPrase implements ICmdParse {
        Iui iui;
        TobaccoCmdHelper tobaccoCmdHelper;
        private CommandResult lastDataFrameResult;
        private int lastDataFrameIndex;
        private String tag = getClass().getSimpleName();

        public MyPrase(Iui iui, TobaccoCmdHelper tobaccoCmdHelper) {
            LogUtil.e(tag, " MyPrase 00 " + iui);
            this.iui = iui;
            this.tobaccoCmdHelper = tobaccoCmdHelper;
        }

        @Override
        public byte[] head() {
            return IProtocol.head;
        }

        @Override
        public byte[] end() {
            return IProtocol.end;
        }

        @Override
        public byte[] checkFrame(byte[] data, boolean contain) {
            return new byte[]{TobacoProtocol.checkFrame(data, contain)};
        }

        private int indexEnd(int indexHead, byte[] buff) {
            int indexEnd = -1;
            for (int i = indexHead; i < buff.length - 1; i++) {

                boolean match = false;
                for (int j = 0; j < end().length; j++) {
                    if (buff[i + j] == end()[j]) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    indexEnd = i + 1;
                }

            }
            return indexEnd;
        }

        @Override
        public byte[] matchFrameData( int indexHead, byte[] buff) throws Exception {
            LogUtil.e(tag, " matchFrameData 01  " + ParseSystemUtil.parseByte2HexStr(buff) + " " + Thread.currentThread().toString());
            int i1 = indexEnd(indexHead, buff);
            if (i1 < 0)
                return null;
            int len = i1 - indexHead + 1;

            if (len > buff.length || indexHead + len > buff.length)
                return null;

            byte[] cache = new byte[len];
            System.arraycopy(buff, indexHead, cache, 0, len);
            CommandResult commandResult = TobacoProtocol.praceata(cache);
            if (commandResult == null) {
                LogUtil.e(tag, " 数据结果为 NULL");
                return null;
            }

            LogUtil.e(tag, " 解析数据结果 :" + commandResult.toString());
            if (null != iui) iui.showText(" RESULTT :" + commandResult.toString());
            
            LogUtil.e(tag, "    cache data  1: " + ParseSystemUtil.parseByte2HexStr(cache));
            int i = 0;
            CmdType cmdType = commandResult.getCmdType()[i];
            if (cmdType == null)
                return cache;
            switch (cmdType) {
                case DATA:
                    tobaccoCmdHelper.repeatData(CmdType.DATA);
                    onDataFrame(commandResult, i);
                    break;
                case RESULT:
                    tobaccoCmdHelper.repeatData(CmdType.RESULT);
                    onResultFrame(commandResult, i);
                    break;
                case CMD:
                    break;
                case ACK:
                    tobaccoCmdHelper.countDownHelper.closeTime(waitAskTag);
                    onAskFrame(commandResult, i);
                    break;
                case INFO:
                    tobaccoCmdHelper.repeatData(CmdType.INFO);
                    break;
                case WARNING:
                    tobaccoCmdHelper.repeatData(CmdType.WARNING);
                    break;
            }


          
            return cache;
        }

        /**
         * 应答帧
         *
         * @param commandResult
         * @param i
         */

        private void onAskFrame(CommandResult commandResult, int i) {

            if (commandResult == null)
                return;
            // 收到应答帧 的条件 是上位机发送了 指令帧  
            // 根据发送的指令帧  设置等待 结果或 数据帧的时间
            LogUtil.e(tag, " 收到应答帧 ：" + commandResult.toString());
            if (null != lastSendData) {
                CommandResult lastSend = TobacoProtocol.praceata(lastSendData.getData());
                LogUtil.e(tag, "  发送的指令帧数据 ：" + lastSend.toString());

                switch (lastSend.getContentType()[0]) {
                    case SELL_PROCESS:
                    case ADD_GOODS:
                        waitAsk(60 * 1000);
                        break;
                    case ADD_GOODS_READY:
                    case STEP_MOTOR_MOVE:
                    case FINE_TUNING:
                        waitAsk(30 * 1000);
                        break;
                    case PN_MOTOR_MOVE:
                        waitAsk(10 * 1000);
                        break;
                    default:
                        waitAsk(2 * 1000);
                        break;
                }
            }
        }

        /**
         * 数据帧
         *
         * @param commandResult
         * @param i
         */

        private void onDataFrame(CommandResult commandResult, int i) {
            // 收到数据帧 跟据 发送的指令帧 设置等待结果帧的时间 
            if (commandResult == null)
                return;
            lastDataFrameResult = commandResult;
            lastDataFrameIndex = i;
            LogUtil.e(tag, " 收到数据帧 ：" + commandResult.toString());
            if (null != lastSendData) {
                CommandResult lastSend = TobacoProtocol.praceata(lastSendData.getData());
                LogUtil.e(tag, "  发送的指令帧数据 ：" + lastSend.toString());
            }
        }

        /**
         * 收到结果帧 结束指令会话 处理指令数据或结果
         *
         * @param commandResult
         * @param cmdindex
         */

        private void onResultFrame(CommandResult commandResult, int cmdindex) {
            closeWait();
            if (commandResult == null)
                return;
            // 收到结果帧  根据指令帧 返回指令执行结果
            LogUtil.e(tag, "   收到结果帧 " + commandResult.toString());
            SerialRsult serialRsult = new SerialRsult();
            if (null != lastSendData) {
                LogUtil.e(tag, "  3 " + lastSendData.toString());
                CommandResult lastSend = TobacoProtocol.praceata(lastSendData.getData());
                serialRsult.setSendResult(lastSend);
            }

            serialRsult.setDataResult(lastDataFrameResult);
            serialRsult.setEndResult(commandResult);
            serialRsult.setDataIndex(lastDataFrameIndex);
            serialRsult.setResultIndex(cmdindex);
            try {

                LogUtil.e(tag, " " + this);
                LogUtil.e(tag, " tobaccoCmdHelper " + tobaccoCmdHelper);
                LogUtil.e(tag, " call result " + Thread.currentThread().toString());
                iui.onResult(serialRsult);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void seIui(Iui iui) {
            this.iui = iui;
            LogUtil.e(tag, " setiui  3 " + iui);
        }
    }


    private void waitAsk(long time) {
        waitAskTag = CountDownHelper.getTag();
        countDownHelper.subTime(time, waitAskTag, new CountDownCall());
    }

    private void closeWait() {
        countDownHelper.closeTime(waitAskTag);
        resetSend();
    }

    class CountDownCall implements CountDownHelper.OnTimeCountDown {
        @Override
        public void OnTime(long time, int tag) {
            if (tag == waitAskTag) {
                // 数据接收超时
                LogUtil.e(TobaccoCmdHelper.this.tag, " 超时接收数据 " + tag + " " + time + "   " + Thread.currentThread().toString());
                resetSend();
                CommandResult lastSend = TobacoProtocol.praceata(lastSendData.getData());
                SerialRsult serialRsult = new SerialRsult();
                serialRsult.setSendResult(lastSend);
                serialRsult.setDataResult(parse.lastDataFrameResult);
                serialRsult.setDataIndex(parse.lastDataFrameIndex);
                if (null != iui) iui.onCmdTimeOut(serialRsult);
            }
        }
    }
}
