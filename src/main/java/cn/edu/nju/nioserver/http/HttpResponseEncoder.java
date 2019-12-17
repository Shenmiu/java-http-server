package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class HttpResponseEncoder {

    /**
     * 将HttpResponse编码为字节流
     *
     * @param response HttpResponse
     * @param buffer   字节流
     */
    public static void encode(HttpResponse response, ByteBuffer buffer) {
        encodeResponseLine(response, buffer);
        encodeResponseHeaders(response, buffer);
        encodeResponseContent(response, buffer);
    }

    /**
     * 编码响应行
     *
     * @param response HttpResponse
     * @param buffer   ByteBuffer
     */
    private static void encodeResponseLine(HttpResponse response, ByteBuffer buffer) {
        StringBuilder builder = new StringBuilder();
        //HTTP协议版本号
        builder.append(response.version().text())
                .append(" ")
                //状态码
                .append(response.status().code())
                .append(" ")
                //状态码消息
                .append(response.status().codeAsText())
                .append("\r\n");
        byte[] result = builder.toString().getBytes(StandardCharsets.UTF_8);
        buffer.put(result);
    }

    /**
     * 编码响应首部
     *
     * @param response HttpResponse
     * @param buffer   ByteBuffer
     */
    private static void encodeResponseHeaders(HttpResponse response, ByteBuffer buffer) {
        HttpHeaders httpHeaders = response.headers();
        Iterator<Map.Entry<String, String>> iterator = httpHeaders.headersIterator();
        StringBuilder builder = new StringBuilder();

        //迭代获取所有首部
        while (iterator.hasNext()) {
            Map.Entry<String, String> header = iterator.next();
            builder.append(header.getKey())
                    .append(":")
                    .append(header.getValue())
                    .append("\r\n");
        }
        builder.append("\r\n");

        byte[] result = builder.toString().getBytes(StandardCharsets.UTF_8);
        buffer.put(result);
    }

    /**
     * 编码响应内容
     *
     * @param response HttpResponse
     * @param buffer   ByteBuffer
     */
    private static void encodeResponseContent(HttpResponse response, ByteBuffer buffer) {
        ByteBuffer content = response.content();
        buffer.put(content);
    }
}
