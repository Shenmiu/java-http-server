package cn.edu.nju.nioserver.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BytesUtilTest {

    @Test
    public void test() {
        int value = Integer.MAX_VALUE;
        byte[] tmp = BytesUtil.int2Bytes(value);

        int other = BytesUtil.bytes2Int(tmp);
        assertEquals(value, other);
    }

}
