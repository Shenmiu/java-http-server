package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpRequestEncoder {

    /**
     * 解析请求，将固定格式包装进request中
     *
     * @param src        字节流数组
     * @param startIndex start下标
     * @param endIndex   end下标
     * @param request    HttpRequest
     */
    public static void encode(byte[] src, int startIndex, int endIndex, HttpRequest request) {
        int startOfHeaders = encodeRequestLine(src, startIndex, endIndex, request);
        int startOfBody = encodeHeaders(src, startOfHeaders, endIndex, request);
        int result = encodeContent(src, startOfBody, endIndex, request);
    }

    /**
     * 解析请求行[method uri version]
     *
     * @param src        字节流数组
     * @param startIndex start下标
     * @param endIndex   end下标
     * @param request    HttpRequest
     * @return int 下一部分的开始下标
     */
    private static int encodeRequestLine(byte[] src, int startIndex, int endIndex, HttpRequest request) {
        int endOfRequestLine = HttpUtil.findNextLineBreak(src, startIndex, endIndex); //当前指向'\n'

        String[] info = new String(src, startIndex, endOfRequestLine - startIndex - 1, StandardCharsets.UTF_8).split("\\s");
        request.setMethod(HttpMethod.valueOf(info[0])); // method
        request.setUri(info[1]); //uri
        request.setVersion(HttpVersion.valueOf(info[2]));

        return endOfRequestLine + 1;
    }

    /**
     * 解析请求首部
     *
     * @param src        字节流数组
     * @param startIndex start下标
     * @param endIndex   end下标
     * @param request    HttpRequest
     * @return int 下一部分的开始下标
     */
    private static int encodeHeaders(byte[] src, int startIndex, int endIndex, HttpRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders(); //解析时创建，不能调用给上层提供的接口

        int startOfHeader = startIndex;
        int endOfHeader = HttpUtil.findNextLineBreak(src, startOfHeader, endIndex);
        while (endOfHeader != -1 && endOfHeader != startOfHeader + 1) {
            String[] header = new String(src, startOfHeader, endOfHeader - startOfHeader - 1, StandardCharsets.UTF_8).split(":");
            httpHeaders.set(header[0], header[1]);
            startOfHeader = endOfHeader + 1;
            endOfHeader = HttpUtil.findNextLineBreak(src, startOfHeader, endIndex);
        }

        request.setHeaders(httpHeaders);
        return endOfHeader + 1;
    }

    /**
     * 解析请求内容
     *
     * @param src        字节流数组
     * @param startIndex start下标
     * @param endIndex   end下标
     * @param request    HttpRequest
     * @return int 返回-1表示结束
     */
    private static int encodeContent(byte[] src, int startIndex, int endIndex, HttpRequest request) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(endIndex - startIndex + 1); //HeapByteBuffer
        byteBuffer.put(src, startIndex, endIndex - startIndex + 1);
        request.setContent(byteBuffer);
        return -1;
    }
}
