package com.tao.serialliba.tobaco;

public enum CmdType {

//    CMD	0xF0	命令帧
//    ACK	0xF1	应答帧
//    DATA	0xF2	数据帧
//    RESULT	0xF3	结果帧
//    INFO	0xF4	信息帧
//    WARNING	0xF5	警告帧

    CMD((byte) 0xF0, "命令帧",0),

    ACK((byte) 0xF1, "应答帧",0),

    DATA((byte) 0xF2, "数据帧",0),

    RESULT((byte) 0xF3, "结果帧",0),

    INFO((byte) 0xF4, "信息帧",0),

    WARNING((byte) 0xF5, "警告帧",0);

    byte data;
    String detail;
    int  stringId;

    public byte getData() {
        return data;
    }

    CmdType(byte data, String detail  , int stringId) {
        this.data = data;
        this.detail = detail;
        this.stringId = stringId;
    }

    public static CmdType type(byte data) {
        switch (data) {
            case (byte) 0xF0:
                return CMD;
            case (byte) 0xF1:
                return ACK;
            case (byte) 0xF2:
                return DATA;
            case (byte) 0xF3:
                return RESULT;
            case (byte) 0xF4:
                return INFO; 
            case (byte) 0xF5:
                return WARNING; 

            default:
                return null;
        }
    }

    static byte type(CmdType type) {
            switch (type) {
                case CMD:
                    return (byte) 0xF0;
                case ACK:
                    return (byte) 0xF1;
                case DATA:
                    return (byte) 0xF2;
                case RESULT:
                    return (byte) 0xF3;
                case INFO:
                    return (byte) 0xF4; 
                case WARNING:
                    return (byte) 0xF5; 
                default:
                    return 0;
            }
    }
}
