package cn.edu.nju.nioserver.http;

/**
 * HTTP 请求解析工具类
 *
 * @author jjenkov
 * @date 19-10-2015
 */
public class HttpUtil {


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

}
