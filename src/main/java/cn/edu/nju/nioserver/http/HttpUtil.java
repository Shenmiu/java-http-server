package cn.edu.nju.nioserver.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 请求解析工具类
 *
 * @author jjenkov
 * @date 19-10-2015
 */
public class HttpUtil {

    private static final byte[] GET = new byte[]{'G', 'E', 'T'};
    private static final byte[] POST = new byte[]{'P', 'O', 'S', 'T'};
    private static final byte[] PUT = new byte[]{'P', 'U', 'T'};
    private static final byte[] HEAD = new byte[]{'H', 'E', 'A', 'D'};
    private static final byte[] DELETE = new byte[]{'D', 'E', 'L', 'E', 'T', 'E'};

    private static final byte[] HOST = new byte[]{'H', 'o', 's', 't'};
    private static final byte[] CONTENT_LENGTH = new byte[]{'C', 'o', 'n', 't', 'e', 'n', 't', '-', 'L', 'e', 'n', 'g', 't', 'h'};

    /**
     * 解析 HTTP 请求
     *
     * @param src        保存 HTTP 请求的数据源
     * @param startIndex 该 HTTP 请求的起始位置
     * @param endIndex   该 HTTP 请求的结束位置
     * @param request    保存 HTTP 请求的元数据
     * @return 该 HTTP 请求的结束位置，若解析失败返回 -1
     */
    public static int parseHttpRequest(byte[] src, int startIndex, int endIndex, HttpRequest request) {
        return -1;
    }

    public static int parseHttpResponse(byte[] target, HttpResponse response) {
        return 0;
    }

    /**
     * 计算 ContentLength 的大小
     *
     * @param src        数据源
     * @param startIndex ContentLength 请求头的开始索引
     * @param endIndex   ContentLength 请求头的结束索引(include)
     * @return ContentLength 的值
     */
    private static int findContentLength(byte[] src, int startIndex, int endIndex) {
        int indexOfColon = findNext(src, startIndex, endIndex, (byte) ':');

        // 跳过 ":" 之后的空格
        int index = indexOfColon + 1;
        while (src[index] == ' ') {
            index++;
        }

        boolean endOfValueFound = false;

        int contentLength = 0;

        while (index < endIndex && !endOfValueFound) {
            switch (src[index]) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    contentLength = contentLength * 10 + ((char) src[index] - '0');
                    index++;
                    break;
                }

                default: {
                    endOfValueFound = true;
                }
            }
        }

        return contentLength;
    }

    private static int findNext(byte[] src, int startIndex, int endIndex, byte value) {
        for (int index = startIndex; index < endIndex; index++) {
            if (src[index] == value) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 查找行间隔符
     *
     * @param src        数据
     * @param startIndex 开始索引
     * @param endIndex   结束索引(exclude)
     * @return 行间隔符的位置，如果不存在则返回 -1
     */
    public static int findNextLineBreak(byte[] src, int startIndex, int endIndex) {
        for (int index = startIndex; index < endIndex; index++) {
            if (src[index] == '\n') {
                if (src[index - 1] == '\r') {
                    return index;
                }
            }
        }
        return -1;
    }
}
