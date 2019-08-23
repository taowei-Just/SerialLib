package com.tao.serialliba.tobaco;


public enum ResultType {

    OK((byte) 0x00, "执行完成"),// R.string.devices_hint_cmd_01),
    INVALID_CMD((byte) 0x01, "无效命令"),//.string.devices_hint_cmd_02),
    INVALID_PARA((byte) 0x02, "无效参数"),//.string.devices_hint_cmd_03),
    CHECKSUN_WRONG((byte) 0x03, "校验和错误"),//.string.devices_hint_cmd_04),
    FRAMES_TYPE_WRONG((byte) 0x04, "帧类型出错"),//.string.devices_hint_cmd_05),
    FRAMES_HEAD_WRONG((byte) 0x05, "帧头出错"),//.string.devices_hint_cmd_06),
    
    SYSTEM_BUSY((byte) 0x06, "系统忙碌"),//.string.devices_hint_cmd_07),
    
    CHANNEL_BOARD_COM_ERR((byte) 0x07, "货道板通信错误"),//.string.devices_hint_cmd_08),
    ADD_GOODS_COM_ERR((byte) 0x08, "加烟数量检测模块通信错误"),//.string.devices_hint_axis_01),
    X_AXIS_RESET_TIMEOUT((byte) 0x09, "X轴复位超时"),//.string.devices_hint_axis_02),
    Y_AXIS_RESET_TIMEOUT((byte) 0x0A, "Y轴复位超时"),//.string.devices_hint_axis_03),
    Z_AXIS_RESET_TIMEOUT((byte) 0x0B, "Z轴复位超时"),//.string.devices_hint_axis_04),
    X_AXIS_RUN_STUCK((byte) 0x0C, "X轴堵转"),//.string.devices_hint_axis_05),
    Y_AXIS_RUN_STUCK((byte) 0x0D, "Y轴堵转"),//.string.devices_hint_axis_06),
    Z_AXIS_RUN_STUCK((byte) 0x0E, "Z轴堵转"),//.string.devices_hint_axis_07),
    X_ZERO_ERR((byte) 0x0F, "X轴光耦异常"),//.string.devices_hint_axis_08),
    Y_ZERO_ERR((byte) 0x10, "Y轴光耦异常"),//.string.devices_hint_axis_09),
    Z_ZERO_ERR((byte) 0x11, "Z轴光耦异常"),//.string.devices_hint_axis_10),
    X_RUN_OVER((byte) 0x12, "X轴运动超出量程"),//.string.devices_hint_axis_11),
    Y_RUN_OVER((byte) 0x13, "Y轴运动超出量程"),//.string.devices_hint_axis_12),
    Z_RUN_OVER((byte) 0x14, "Z轴运动超出量程"),//.string.devices_hint_axis_13),
    
    OUTSIDE_CURTAIN_TIMEOUT((byte) 0x15, "导烟外帘电机超时"),//.string.devices_hint_conductor01),
    INSIDE_CURTAIN_TIMEOUT((byte) 0x16, "导烟内帘电机超时"),//.string.devices_hint_conductor02),
    
    WAREHOUSE_TIMEOUT((byte) 0x17, "提升仓电机超时"),//.string.devices_hint_motor_01),
    WAREHOUSE_CURTAIN_TIMEOUT((byte) 0x18, "取货帘电机超时"),//.string.devices_hint_motor_02),
    
    PUSH_GOODS_TIMEOUT((byte) 0x19, "推烟电机超时"),//.string.devices_hint_motor_03),
    
    OUTSIDE_CURTAIN_CURRENT_OVER((byte) 0x1A, "	导烟外帘电机过流"),//.string.devices_hint_02_01),
    INSIDE_CURTAIN_CURRENT_OVER((byte) 0x1B, "导烟内帘电机过流"),//.string.devices_hint_02_02),
    
    WAREHOUSE_CURRENT_OVER((byte) 0x1C, "提升仓电机过流"),//.string.devices_hint_motor_04),
    WAREHOUSE_CURTAIN_CURRENT_OVER((byte) 0x1D, "	取货帘电机过流"),//.string.devices_hint_02),
    PUSH_GOODS_CURRENT_OVER((byte) 0x1E, "推烟电机过流"),//.string.devices_hint_conductor03),
    CHANNEL_EMPTY((byte) 0x1F, "通道无货"),//.string.devices_hint_track_01),
    CHANNEL_ERR((byte) 0x20, "货道异常（出货模块检测异常）"),//.string.devices_hint_track_02),
    DROP_DETE_TIMEOUT((byte) 0x21, "货物掉落检测超时"),//.string.devices_hint_drop_02),
    GET_GOODS_TIMEOUT((byte) 0x22, "取货超时"),//.string.devices_hint_fetch_02),
    
    
    ADD_GOODS_NUM_ERR((byte) 0x23, "补货数量错误"),//.string.devices_hint_num_02),
    ADD_GOODS_OVER((byte) 0x24, "补货数量超上限");//.string.devices_hint_shipment_01);


    byte data;
    String detail;
    int stringId;

    ResultType(byte data, String detail ) {
        this.data = data;
        this.detail = detail;
    }

    public int getStringId() {
        return stringId;
    }

    public String getDetail() {
        return detail;
    }

    public static ResultType type(byte data) {

        switch (data) {
            case (byte) 0x00:
                return OK;//((byte) 0x00, "执行完成", 0),
            case (byte) 0x01:
                return INVALID_CMD;//((byte) 0x01, "无效命令", 0),
            case (byte) 0x02:
                return INVALID_PARA;//((byte) 0x02, "无效参数", 0),
            case (byte) 0x03:
                return CHECKSUN_WRONG;//((byte) 0x03, "校验和错误", 0),
            case (byte) 0x04:
                return FRAMES_TYPE_WRONG;//((byte) 0x04, "帧类型出错", 0),
            case (byte) 0x05:
                return FRAMES_HEAD_WRONG;//((byte) 0x05, "帧头出错", 0),
            case (byte) 0x06:
                return SYSTEM_BUSY;// ((byte) 0x06, "系统忙碌", 0),
            case (byte) 0x07:
                return CHANNEL_BOARD_COM_ERR;//((byte) 0x07, "货道板通信错误", 0),
            case (byte) 0x08:
                return ADD_GOODS_COM_ERR;//((byte) 0x08, "加烟数量检测模块通信错误", 0),
            case (byte) 0x09:
                return X_AXIS_RESET_TIMEOUT;//((byte) 0x09, "X轴复位超时", 0),
            case (byte) 0x0A:
                return Y_AXIS_RESET_TIMEOUT;//((byte) 0x0A, "Y轴复位超时", 0),
            case (byte) 0x0B:
                return Z_AXIS_RESET_TIMEOUT;//((byte) 0x0B, "Z轴复位超时", 0),
            case (byte) 0x0C:
                return X_AXIS_RUN_STUCK;//((byte) 0x0C, "X轴堵转", 0),
            case (byte) 0x0D:
                return Y_AXIS_RUN_STUCK;//((byte) 0x0D, "Y轴堵转", 0),
            case (byte) 0x0E:
                return Z_AXIS_RUN_STUCK;//((byte) 0x0E, "Z轴堵转", 0),
            case (byte) 0x0F:
                return X_ZERO_ERR;//((byte) 0x0F, "X轴光耦异常", 0),
            case (byte) 0x10:
                return Y_ZERO_ERR;//((byte) 0x10, "Y轴光耦异常", 0),
            case (byte) 0x11:
                return Z_ZERO_ERR;//((byte) 0x11, "Z轴光耦异常", 0),
            case (byte) 0x12:
                return X_RUN_OVER;//((byte) 0x12, "X轴运动超出量程", 0),
            case (byte) 0x13:
                return Y_RUN_OVER;//((byte) 0x13, "Y轴运动超出量程", 0),
            case (byte) 0x14:
                return Z_RUN_OVER;//((byte) 0x14, "Z轴运动超出量程", 0),
            case (byte) 0x15:
                return OUTSIDE_CURTAIN_TIMEOUT;//((byte) 0x15, "导烟外帘电机超时", 0),
            case (byte) 0x16:
                return INSIDE_CURTAIN_TIMEOUT;//((byte) 0x16, "导烟内帘电机超时", 0),
            case (byte) 0x17:
                return WAREHOUSE_TIMEOUT;// ((byte) 0x17, "提升仓电机超时", 0),
            case (byte) 0x18:
                return WAREHOUSE_CURTAIN_TIMEOUT;//((byte) 0x18, "取货帘电机超时", 0),
            case (byte) 0x19:
                return PUSH_GOODS_TIMEOUT;//((byte) 0x19, "推烟电机超时", 0),
            case (byte) 0x1A:
                return OUTSIDE_CURTAIN_CURRENT_OVER;// ((byte) 0x1A, "	导烟外帘电机过流", 0),
            case (byte) 0x1B:
                return INSIDE_CURTAIN_CURRENT_OVER;//((byte) 0x1B, "导烟内帘电机过流", 0),
            case (byte) 0x1C:
                return WAREHOUSE_CURRENT_OVER;//((byte) 0x1C, "提升仓电机过流", 0),
            case (byte) 0x1D:
                return WAREHOUSE_CURTAIN_CURRENT_OVER;//((byte) 0x1D, "	取货帘电机过流", 0),
            case (byte) 0x1E:
                return PUSH_GOODS_CURRENT_OVER;//((byte) 0x1E, "推烟电机过流", 0),
            case (byte) 0x1F:
                return CHANNEL_EMPTY;//((byte) 0x1F, "通道无货", 0),
            case (byte) 0x20:
                return CHANNEL_ERR;//((byte) 0x20, "货道异常（出货模块检测异常）", 0),
            case (byte) 0x21:
                return DROP_DETE_TIMEOUT;//((byte) 0x21, "货物掉落检测超时", 0),
            case (byte) 0x22:
                return GET_GOODS_TIMEOUT;//((byte) 0x22, "取货超时", 0),
            case (byte) 0x23:
                return ADD_GOODS_NUM_ERR;//((byte) 0x23, "补货数量错误", 0),
            case (byte) 0x24:
                return ADD_GOODS_OVER;//((byte) 0x24, "补货数量超上限", 0);
            default:
                return null;
        }
    }


}
