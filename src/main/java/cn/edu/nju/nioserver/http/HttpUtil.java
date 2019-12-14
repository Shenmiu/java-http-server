package cn.edu.nju.nioserver.http;

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
     * @param src         保存 HTTP 请求的数据源
     * @param startIndex  该 HTTP 请求的起始位置
     * @param endIndex    该 HTTP 请求的结束位置
     * @param request 保存 HTTP 请求的元数据
     * @return 该 HTTP 请求的结束位置，若解析失败返回 -1
     */
    public static int parseHttpRequest(byte[] src, int startIndex, int endIndex, HttpRequest request) {

//        // 解析请求行
//        int endOfFirstLine = findNextLineBreak(src, startIndex, endIndex);
//        if (endOfFirstLine == -1) {
//            return -1;
//        }
//
//        // 解析请求头
//        int prevEndOfHeader = endOfFirstLine + 1;
//        int endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);
//
//        //prevEndOfHeader + 1 = end of previous header + 2 (+2 = CR + LF)
//        while (endOfHeader != -1 && endOfHeader != prevEndOfHeader + 1) {
//
//            if (matches(src, prevEndOfHeader, CONTENT_LENGTH)) {
//                httpHeaders.contentLength = findContentLength(src, prevEndOfHeader, endIndex);
//            }
//
//            prevEndOfHeader = endOfHeader + 1;
//            endOfHeader = findNextLineBreak(src, prevEndOfHeader, endIndex);
//        }
//
//        if (endOfHeader == -1) {
//            return -1;
//        }
//
//        //check that byte array contains full HTTP message.
//        int bodyStartIndex = endOfHeader + 1;
//        int bodyEndIndex = bodyStartIndex + httpHeaders.contentLength;
//
//        if (bodyEndIndex <= endIndex) {
//            //byte array contains a full HTTP request
//            httpHeaders.bodyStartIndex = bodyStartIndex;
//            httpHeaders.bodyEndIndex = bodyEndIndex;
//            return bodyEndIndex;
//        }

        return -1;
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

    public static void resolveHttpMethod(byte[] src, int startIndex, HttpHeaders httpHeaders) {
//        if (matches(src, startIndex, GET)) {
//            httpHeaders.httpMethod = HttpHeaders.HTTP_METHOD_GET;
//        }
//        if (matches(src, startIndex, POST)) {
//            httpHeaders.httpMethod = HttpHeaders.HTTP_METHOD_POST;
//        }
//        if (matches(src, startIndex, PUT)) {
//            httpHeaders.httpMethod = HttpHeaders.HTTP_METHOD_PUT;
//        }
//        if (matches(src, startIndex, HEAD)) {
//            httpHeaders.httpMethod = HttpHeaders.HTTP_METHOD_HEAD;
//        }
//        if (matches(src, startIndex, DELETE)) {
//            httpHeaders.httpMethod = HttpHeaders.HTTP_METHOD_DELETE;
//        }
    }

    /**
     * 用于比较数据源中的数据和相应的字段是否相同
     *
     * @param src    数据源
     * @param offset 比较数据的偏移量
     * @param value  用于对比的字段
     * @return 是否相同
     */
    private static boolean matches(byte[] src, int offset, byte[] value) {
        for (int i = offset, n = 0; n < value.length; i++, n++) {
            if (src[i] != value[n]) {
                return false;
            }
        }
        return true;
    }
}
