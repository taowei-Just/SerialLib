package com.tao.serialliba.tobaco;

import com.tao.utilslib.encrypt.ParseSystemUtil;
import com.tao.utilslib.log.LogUtil;

/**
 * 1. 出货
 * 2. 上烟
 * 3. 识别烟
 * 4. 条码扫描
 * 5.
 */

public class TobacoProtocol implements IProtocol {

    private static String tag = "TobacoProtocol";
    
    
    
//    
//    //  特殊货道位置微调
//    public static byte[] spc_pos_adjust(int channel , int dis,int offeset) {
//        byte[] bytes = headEnd(new byte[13]);
//        bytes[2] = CmdType.CMD.getData();
//        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
//        bytes[4] = CommandContentType.SPC_POS_ADJUST.getData();
//        bytes[5] = ParseSystemUtil.parseHexStr2Byte((channel > 16 ? "" : "0") + Integer.toHexString(channel))[0];
//        bytes[6] = ParseSystemUtil.parseHexStr2Byte((dis > 16 ? "" : "0") + Integer.toHexString(dis))[0];
//        bytes[7] = ParseSystemUtil.parseHexStr2Byte((offeset > 16 ? "" : "0") + Integer.toHexString(offeset))[0];
//        byte[] bytes1 = ParseSystemUtil.parseHexStr2Byte((offeset > 16 ? "" : "0") + Integer.toHexString(offeset));
//        bytes[8] = bytes1.length > 1 ? bytes1[2] : 0;
//        bytes[9] = bytes1[0];
//        bytes[10] = checkFrame(bytes);
//        return bytes;
//    }
//  //  Z轴翻转背面时，货道X轴OFFSET
//    public static byte[] z_rotate_pos_offset(int dis ,int offeset) {
//
//        byte[] bytes = headEnd(new byte[10]);
//        bytes[2] = CmdType.CMD.getData();
//        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
//        bytes[4] = CommandContentType.Z_ROTATE_POS_OFFSET.getData();
//        bytes[5] = ParseSystemUtil.parseHexStr2Byte((dis > 16 ? "" : "0") + Integer.toHexString(dis))[0];
//        bytes[6] = ParseSystemUtil.parseHexStr2Byte((offeset > 16 ? "" : "0") + Integer.toHexString(offeset))[0];
//        bytes[7] = checkFrame(bytes);
//        return bytes;
//    }
 
    // FAIL_TIME_SET
    public static byte[] fail_time_set(int time) {

        byte[] bytes = headEnd(new byte[10]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.BOOT_READY.getData();
        byte[] bytes1 = ParseSystemUtil.parseHexStr2Byte((time > 16 ? "" : "0") + Integer.toHexString(time));
        bytes[5] = bytes1.length > 1 ? bytes1[2] : 0;
        bytes[6] = bytes1[0];
        bytes[7] = checkFrame(bytes);
        return bytes;
    }
  // 主控握手
    public static byte[] bootReady() {

        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.BOOT_READY.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;
    }

    /**
     * 获取版本号
     *
     * @param
     * @return
     */
    public static byte[] GET_VERSIONS() {

        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.GET_VERSIONS.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;

    }


    /**
     * 获取货到状态
     *
     * @param
     * @return
     */
    public static byte[] get_channel_state() {

        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.GET_CHANNEL_STATE.getData();
        
        bytes[5] = checkFrame(bytes);
        return bytes;

    }


    /**
     * 正反转电机转动
     *
     * @param number  编号
     * @param operate 参数  0.向下/向后/关闭    1.向上/向前/打开
     * @return
     */
    public static byte[] pn_motor_move(int number, int operate) {

        byte[] bytes = headEnd(new byte[10]);

        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.PN_MOTOR_MOVE.getData();

        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = ParseSystemUtil.parseHexStr2Byte((operate > 16 ? "" : "0") + Integer.toHexString(operate))[0];

        bytes[7] = checkFrame(bytes);
        return bytes;

    }


    // 重置 电机马达
    public static byte[] resetMotor(int number) {
        byte[] bytes = headEnd(new byte[9]);
        //头
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 9 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.STEP_MOTOR_RESET.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 9 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = checkFrame(bytes);
        return bytes;
    }

    //回复帧
    public static byte[] replyFrame(CmdType type) {
        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.ACK.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 9 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = type.getData();
        bytes[5] = checkFrame(bytes);

        return bytes;
    }

    // 开关设备 led 等
    public static byte[] openDevices(DeviceType type, int number, boolean open) {
        byte[] bytes = headEnd(new byte[10]);

        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = open ? CommandContentType.OPEN.getData() : CommandContentType.CLOSE.getData();
        bytes[5] = type.getData();
        bytes[6] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[7] = checkFrame(bytes);
        return bytes;
    }

    // 出货指令

    /**
     * number  货道编号
     */
    public static byte[] deliverFromGodown(int number) throws Exception {
        if (number > 99)
            throw new Exception(" channel is out bound max is 99 current " + number);
        byte[] bytes = headEnd(new byte[9]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.SELL_PROCESS.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = checkFrame(bytes);
        return bytes;
    }


    // 控制led亮度 
    public static byte[] ledLight(int number) throws Exception {
        if (number > 3)
            throw new Exception(" light is out bound max is 3 current " + number);
        byte[] bytes = headEnd(new byte[9]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.LED_BAR_CTRL.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = checkFrame(bytes);
        return bytes;
    }

    // 获取门控状态
    public static byte[] getDoorStatus(int number) throws Exception {

        byte[] bytes = headEnd(new byte[9]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.GET_DOOR_STATE.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = checkFrame(bytes);
        return bytes;
    }


    // 检测主控是否启动完成
    public static byte[] getBootStatus() {
        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.BOOT_READY.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;
    }

    //  重置通讯
    public static byte[] resetCommunication() {
        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.RESET_COM.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;
    }


    //  补货模块运动到补货口
    public static byte[] activeReplenishmentMoudle() {
        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.ADD_GOODS_READY.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;
    }


    //  补货模块自动加货
    public static byte[] autoReplenishment(int number) {
        byte[] bytes = headEnd(new byte[9]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.ADD_GOODS.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = checkFrame(bytes);
        return bytes;
    }

    //  获取付款二维码
    public static byte[] payQrcode(int number) {
        byte[] bytes = headEnd(new byte[8]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.GET_BARCODER.getData();
        bytes[5] = checkFrame(bytes);
        return bytes;
    }


    // 设置数据头部 尾部
    private static byte[] headEnd(byte[] bytes) {
        bytes[0] = head[0];
        bytes[1] = head[1];
        bytes[bytes.length - 2] = end[0];
        bytes[bytes.length - 1] = end[1];
        return bytes;
    }


    // 校验帧
    public static byte checkFrame(byte[] data) {
        return checkFrame(data, true);
    }

    public static byte checkFrame(byte[] data, boolean content) {
        byte[] cache = new byte[content ? data.length - 5 : data.length];
        System.arraycopy(data, content ? 2 : 0, cache, 0, cache.length);
        int c = 0;
        for (int i = 0; i < cache.length; i++) {
            c = c ^ cache[i];
        }
        return (byte) c;
    }

    // 指令内容
    public static CommandContentType matchCmdCode(byte[] data) {
        return CommandContentType.type(data[4]);
    }

    // 数据内容
    public static byte[] extractData(byte[] data) {
        byte[] buff = new byte[data.length - 8];
        System.arraycopy(data, 5, buff, 0, buff.length);
        return buff;
    }

    // 收到数据解析
    public static CommandResult praceata(byte[] data) {
        if (data == null || data.length < 8)
            return null;
        int len = 0;
        int[] index = new int[20] ;
        for (int i = 0; i < index.length; i++) {
            index[i]=-1 ;
        }
        
        int ii = -1;
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] == (byte) 0xEE && data[i + 1] == (byte) 0x55) {
                index[++ii] = i;
            }
        }

        if (ii < 0)
            return null;

        CmdType[] types = new CmdType[index.length];
        ResultType[] resultTypes = new ResultType[index.length];
        CommandContentType[] contentTypes = new CommandContentType[index.length];

        CommandResult commandResult = new CommandResult();
        commandResult.setBytes(data);
        commandResult.setCmdType(types);
        commandResult.setResultType(resultTypes);
        commandResult.setContentType(contentTypes);

        for (int i = 0; i < index.length; i++) {
            commandResult.getData().add(null);
            commandResult.getCmdDataS().add(null);
            
            if (index[i] < 0 ||  data.length-index[i] < 2 )
                continue;
            
            // 长度
            len = Integer.parseInt(ParseSystemUtil.parseByte2HexStr(new byte[]{data[index[i] + 3]}), 16);

            int index1 = index[i];
            
            if ( data.length-index[i] <len-1 || data[index1 + len - 1] != (byte) 0x0A || data[index1 + len - 2] != (byte) 0x0D)
                continue;

            byte[] buff01 = new byte[len  ];
            System.arraycopy(data, index[i]  , buff01, 0, buff01.length);
            
               byte[] buff = new byte[len - 5];
            System.arraycopy(data, index[i] + 2, buff, 0, buff.length);
            
            byte checkFrame = checkFrame(buff, false);
            if (data[index[i] + len - 3] != checkFrame)
                continue;
            types[i] = CmdType.type(buff[0]);
            contentTypes[i] = CommandContentType.type(buff[2]);
            byte[] buff2;
       
            commandResult.getCmdDataS().set(i, buff01);
            
            switch (types[i]) {
                case CMD:
                    buff2 = new byte[buff.length - 3];
                    System.arraycopy(buff, 3, buff2, 0, buff2.length);
                    commandResult.getData().set(i, buff2);
                    break;
                case ACK:
                    break;
                case DATA:
                    buff2 = new byte[buff.length - 3];
                    System.arraycopy(buff, 3, buff2, 0, buff2.length);
                    commandResult.getData().set(i, buff2);
                    break;
                    
                case RESULT:
                    buff2 = new byte[buff.length - 3];
                    System.arraycopy(buff, 3, buff2, 0, buff2.length);
                    ResultType resultType = ResultType.type(buff[3]);
                    resultTypes[i] = resultType;
                    commandResult.getData().set(i, buff2);

                    break;
                    
                case INFO:

                    break;
                case WARNING:
                    buff2 = new byte[buff.length - 3];
                    System.arraycopy(buff, 3, buff2, 0, buff2.length);
                    commandResult.getData().set(i, buff2);
                    contentTypes[i] = CommandContentType.type(buff[5]);
                    break;
            }
        }


        /**
         *
         * 收到指令 解析指令  根据指令类型进行下一步
         * 例如 ：  收到应答 后 开始等待数据 
         * 1. 发送数据 
         * 2. 等待应答 超时
         * 3. 根据场景  等待 结果 或者 数据帧 
         * 4.等到数据后 应答
         */


        return commandResult;
    }


    public static void main(String[] args) {
        byte[] bytes = resetMotor(0);
        System.err.println(ParseSystemUtil.parseByte2HexStr(bytes));
        System.err.println(ParseSystemUtil.parseByte2HexStr(extractData(bytes)));
        System.err.println(ParseSystemUtil.parseByte2HexStr(new byte[]{matchCmdCode(bytes).getData()}));
        System.err.println(ParseSystemUtil.parseByte2HexStr(replyFrame(CmdType.CMD)));
        System.err.println(ParseSystemUtil.parseByte2HexStr(openDevices(DeviceType.DOOR, 2, false)));

        System.err.println("内烟电机关闭");
        System.err.println(ParseSystemUtil.parseByte2HexStr(pn_motor_move(3, 0)));
        System.err.println("内烟电机打开");
        System.err.println(ParseSystemUtil.parseByte2HexStr(pn_motor_move(3, 1)));

        try {
            System.err.println(ParseSystemUtil.parseByte2HexStr(deliverFromGodown(10)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            byte[] buf = ledLight(3);
            System.err.println(ParseSystemUtil.parseByte2HexStr(buf));
            System.err.println(praceata(buf).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] da = new byte[]{(byte) 0xEE, 0x55, (byte) 0xF0, 0x0D, 0x51, 0x28, 0x03, (byte) 0xE8, 0x03, (byte) 0xE8, (byte) 0x84, 0x0D, 0x0A};
        System.err.println(praceata(da).toString());

    }


    public static byte[] step_motor_move_tar(int number, int x, int y) throws Exception {
        if (number > 254)
            throw new Exception("  device number out of range ");

        byte[] bytes = headEnd(new byte[11]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.STEP_MOTOR_MOVE.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((number > 16 ? "" : "0") + Integer.toHexString(number))[0];
        bytes[6] = ParseSystemUtil.parseHexStr2Byte((x > 16 ? "" : "0") + Integer.toHexString(x))[0];
        bytes[7] = ParseSystemUtil.parseHexStr2Byte((y > 16 ? "" : "0") + Integer.toHexString(y))[0];
        bytes[8] = checkFrame(bytes);
        return bytes;
    }

    public static byte[] fine_tuning(int z, int l, int d, int n) throws Exception {

        LogUtil.e(tag, " z " + z + " l " + l + " d " + d + " n  " + n);
        if (z != 0 && z != 1)
            throw new Exception(" xy must 0 or 1");

        if (d != 0 && d != 1)
            throw new Exception(" orention must 0 or 1");

        byte[] bytes = headEnd(new byte[13]);
        bytes[2] = CmdType.CMD.getData();
        bytes[3] = ParseSystemUtil.parseHexStr2Byte((bytes.length > 16 ? "" : "0") + Integer.toHexString(bytes.length))[0];
        bytes[4] = CommandContentType.FINE_TUNING.getData();
        bytes[5] = ParseSystemUtil.parseHexStr2Byte((z > 16 ? "" : "0") + Integer.toHexString(z))[0];
        bytes[6] = ParseSystemUtil.parseHexStr2Byte((l > 16 ? "" : "0") + Integer.toHexString(l))[0];
        bytes[7] = ParseSystemUtil.parseHexStr2Byte((d > 16 ? "" : "0") + Integer.toHexString(d))[0];
        byte[] bytes1 = ParseSystemUtil.parseHexStr2Byte((n > 16 ? "" : "0") + Integer.toHexString(n));
        bytes[8] = bytes1.length > 1 ? bytes1[2] : 0;
        bytes[9] = bytes1[0];
        bytes[10] = checkFrame(bytes);
        return bytes;

    }
}
