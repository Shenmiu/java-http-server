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
        StringBuilder builder = new StringBuilder();
        for (byte e : bytes) {
            if (e >= '0' && e <= '9') {
                builder.append(e - '0');
            } else {
                throw new IllegalArgumentException("can not parse");
            }

        }
        return Integer.parseInt(builder.toString());
    }

    /**
     * 将int值转换为byte数组
     *
     * @param num int值
     * @return byte数组
     */
    public static byte[] int2Bytes(int num) {
        return String.valueOf(num).getBytes();
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
