package com.tao.serialliba.tobaco;

import com.tao.utilslib.encrypt.ParseSystemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandResult {

    int flag = 0;
    // 指令类型
    CmdType[] cmdType;
    // 结果类型
    ResultType[] resultType;
    // 具体指令
    CommandContentType[] contentType;
    // 指令数据
    List<byte[]> data = new ArrayList<>();
    List<byte[]> cmdDataS = new ArrayList<>();

    byte[] bytes;

    public List<byte[]> getCmdDataS() {
        return cmdDataS;
    }

    public void setCmdDataS(List<byte[]> cmdDataS) {
        this.cmdDataS = cmdDataS;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CmdType[] getCmdType() {
        return cmdType;
    }

    public void setCmdType(CmdType[] cmdType) {
        this.cmdType = cmdType;
    }

    public ResultType[] getResultType() {
        return resultType;
    }

    public void setResultType(ResultType[] resultType) {
        this.resultType = resultType;
    }

    public CommandContentType[] getContentType() {
        return contentType;
    }

    public void setContentType(CommandContentType[] contentType) {
        this.contentType = contentType;
    }

    public List<byte[]> getData() {
        return data;
    }

    public void setData(List<byte[]> data) {
        this.data = data;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (byte[] cmdData : cmdDataS) {
            sb.append(ParseSystemUtil.parseByte2HexStr(cmdData));
            sb.append(" ,  ");
        }
        sb.append("  截取 数据 ");
        for (byte[] cmdData : data) {
            sb.append(ParseSystemUtil.parseByte2HexStr(cmdData));
            sb.append("  , ");
        }
        return "\n bytes=" + ParseSystemUtil.parseByte2HexStr(bytes) + "       cmdType=" + Arrays.toString(cmdType) + "    resultType=" + Arrays.toString(resultType) +
                "    contentType = " + Arrays.toString(contentType) + sb.toString();
    }
}
