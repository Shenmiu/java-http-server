package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class HttpResponseEncoder {

    private State curState = State.ENCODE_INITIAL;

    private ByteBuffer curContent;

    /**
     * 将HttpResponse编码为字节流
     *
     * @param response HttpResponse
     * @param buffer   字节流
     */
    public void encode(Object message, ByteBuffer buffer) {
        switch (curState) {
            case ENCODE_INITIAL: {

            }
            case ENCODE_HEADER: {

            }
            case ENCODE_FIXED_LENGTH_CONTENT: {

            }
            case ENCODE_VARIABLE_LENGTH_CONTENT: {

            }
            case ENCODE_WAIT_CONTENT: {

            }
            case ENCODE_EMPTY_CONTENT: {

            }
        }
    }

    /**
     * 编码响应行
     *
     * @param response HttpResponse
     * @param buffer   ByteBuffer
     */
    private void encodeResponseLine(HttpResponse response, ByteBuffer buffer) {
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
    private void encodeResponseHeaders(HttpResponse response, ByteBuffer buffer) {
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
    private void encodeResponseContent(HttpResponse response, ByteBuffer buffer) {
        ByteBuffer content = response.content().byteBuffer();
        buffer.put(content);
    }

    /**
     * 当前HttpResponse的解析状态
     */
    private enum State {
        ENCODE_INITIAL,
        ENCODE_HEADER,
        ENCODE_VARIABLE_LENGTH_CONTENT,
        ENCODE_FIXED_LENGTH_CONTENT,
        ENCODE_WAIT_CONTENT,
        ENCODE_EMPTY_CONTENT
    }
}
