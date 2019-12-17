package cn.edu.nju.nioserver.util;

import java.util.List;

public class BytesUtil {

    /**
     * 工具类不提供构造器初始化
     */
    private BytesUtil() {
    }

    /**
     * 将byte数组转换为int
     *
     * @param bytes byte数组
     * @return int
     */
    public static int bytes2Int(byte[] bytes) {
        int result = 0;
        //将每个byte依次搬运到int相应的位置
        result = bytes[0] & 0xff;
        result = result << 8 | bytes[1] & 0xff;
        result = result << 8 | bytes[2] & 0xff;
        result = result << 8 | bytes[3] & 0xff;
        return result;
    }

    /**
     * 将int值转换为byte数组
     *
     * @param num int值
     * @return byte数组
     */
    public static byte[] int2Bytes(int num) {
        byte[] bytes = new byte[4];
        //通过移位运算，截取低8位的方式，将int保存到byte数组
        bytes[0] = (byte) (num >>> 24);
        bytes[1] = (byte) (num >>> 16);
        bytes[2] = (byte) (num >>> 8);
        bytes[3] = (byte) num;
        return bytes;
    }

    /**
     * 根据字节list获取int
     *
     * @param list 字节列表
     * @return byte[]
     */
    public static byte[] list2ByteArray(List<Byte> list) {
        byte[] info = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            info[i] = list.get(i);
        }
        return info;
    }
}
