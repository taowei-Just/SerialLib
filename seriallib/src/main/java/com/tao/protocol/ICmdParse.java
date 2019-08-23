package com.tao.protocol;

import java.util.List;

/**
 * Created by Administrator on 2019-8-13.
 */

public interface ICmdParse {

    byte[] head();

    byte[] end();

    byte[] checkFrame(byte[] data , boolean contain);

    byte[] matchFrameData( int index, byte[] buff) throws Exception;
    
}
