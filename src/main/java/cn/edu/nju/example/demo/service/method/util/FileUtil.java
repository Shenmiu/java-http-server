package cn.edu.nju.example.demo.service.method.util;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 新添加文件
     */
    public static boolean add(String fileName) {
        return true;
    }

    /**
     * 读文件
     */
    public static String read(String fileName) {
        try {
            RandomAccessFile indexFile = new RandomAccessFile
                    (FileUtil.class.getClassLoader().getResource(fileName).getPath(), "r");
            FileChannel indexChannel = indexFile.getChannel();
            ByteBuffer indexBuf = ByteBuffer.allocate(512);

            StringBuilder result = new StringBuilder();
            int bytesRead;
            while ((bytesRead = indexChannel.read(indexBuf)) != -1) {
                result.append(new String(indexBuf.array(), 0, bytesRead, StandardCharsets.UTF_8));
                indexBuf.rewind();
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 写文件
     */
    public static boolean write(String fileName, boolean overwrite) {
        return true;
    }

}
