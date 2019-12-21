package cn.edu.nju.example.demo.service.method.util;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Log4j2
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
     * 读资源文件
     */
    public static String readResourceFile(String resourceFileName) {
        try {
            RandomAccessFile file = new RandomAccessFile
                    (FileUtil.class.getClassLoader().getResource(resourceFileName).getPath(), "r");
            String content = readFileContent(file);
            file.close();
            return content;
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
            String content = readFileContent(file);
            file.close();
            return content;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件内容
     */
    private static String readFileContent(RandomAccessFile file) throws IOException {
        FileChannel channel = file.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(512);

        StringBuilder result = new StringBuilder();
        int bytesRead;
        while ((bytesRead = channel.read(buf)) != -1) {
            result.append(new String(buf.array(), 0, bytesRead, StandardCharsets.UTF_8));
            buf.rewind();
        }
        channel.close();
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
            channel.close();
            file.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件
     */
    public static boolean delete(String fileName) {
        File toDelete = new File(BASE_DIR + fileName);
        return toDelete.delete();
    }

    public static byte[] getResource(String resourceName) {
        try {
            FileInputStream is = new FileInputStream(getResourcePath(resourceName));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                os.write(data, 0, nRead);
            }
            return os.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static String getResourcePath(String resourceName) {
        String res = FileUtil.class.getClassLoader().getResource(resourceName).getPath();
        if (null == res) {
            log.debug("找不到资源文件: " + resourceName);
        }
        return res;
    }
}
