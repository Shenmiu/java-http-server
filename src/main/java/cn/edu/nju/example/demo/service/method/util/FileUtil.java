package cn.edu.nju.example.demo.service.method.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    private static final String BASE_DIR = System.getProperty("user.dir") + "/data/";

    /**
     * 找到真正的 fileName
     */
    public static String realFileName(String uri) {
        String curFileName = uri.substring(8);
        int paramMarkIndex = curFileName.indexOf("?");
        if (paramMarkIndex != -1) {
            curFileName = curFileName.substring(0, paramMarkIndex);
        }
        return curFileName;
    }

    /**
     * 新添加文件
     */
    public static boolean add(String fileName) {
        return true;
    }

    /**
     * 读资源文件
     */
    public static String readResourceFile(String resourceFileName) {
        try {
            RandomAccessFile file = new RandomAccessFile
                    (FileUtil.class.getClassLoader().getResource(resourceFileName).getPath(), "r");
            return readFileContent(file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读数据文件
     */
    public static String read(String fileName) {
        try {
            RandomAccessFile file = new RandomAccessFile(BASE_DIR + fileName, "r");
            return readFileContent(file);
        } catch (IOException e) {
            return null;
        }
    }

    private static String readFileContent(RandomAccessFile file) throws IOException {
        FileChannel channel = file.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(512);

        StringBuilder result = new StringBuilder();
        int bytesRead;
        while ((bytesRead = channel.read(buf)) != -1) {
            result.append(new String(buf.array(), 0, bytesRead, StandardCharsets.UTF_8));
            buf.rewind();
        }
        return result.toString();
    }

    /**
     * 写数据文件
     */
    public static boolean write(String fileName, String content, boolean overwrite) {
        try {
            RandomAccessFile file = new RandomAccessFile(BASE_DIR + fileName, "rw");
            FileChannel channel = file.getChannel();

            if (!overwrite) {
                String preContent = read(fileName);
                content = preContent == null ? content : preContent.concat(content);
            }
            channel.write(ByteBuffer.wrap(content.getBytes()));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
