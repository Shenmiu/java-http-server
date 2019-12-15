package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestEncoder {

    /**
     * 初始状态
     */
    private State currentState = State.READ_INITIAL;

    /**
     * 当前HttpRequest
     */
    private HttpRequest curRequest = new HttpRequest();

    /**
     * 解析请求，将固定格式包装进request中
     *
     * @param buffer   字节流缓冲区
     * @param requests 请求列表
     */
    public int encode(ByteBuffer buffer, List<HttpRequest> requests) {

        int start = 0;
        int limit = buffer.limit();
        while (start != limit) {
            switch (currentState) {
                case READ_INITIAL: {
                    int result = encodeRequestLine(start, buffer, curRequest);
                    if (result == -1) {
                        return start;
                    }
                    start = result;
                    currentState = State.READ_HEADER;
                }
                case READ_HEADER: {
                    int result = encodeRequestHeaders(start, buffer, curRequest);
                    if (result == -1) {
                        return start;
                    }
                    start = result;

                    if (HttpHeaders.hasContentLength(curRequest)) {
                        currentState = State.READ_FIXED_LENGTH_CONTENT;
                    } else {
                        currentState = State.READ_VARIABLE_LENGTH_CONTENT;
                    }
                }
                case READ_FIXED_LENGTH_CONTENT: {
                    int result = encodeRequestContent(start, HttpHeaders.getContentLength(curRequest), buffer, curRequest);
                    if (result == -1) {
                        return start;
                    }
                    start = result;
                    requests.add(curRequest);

                    //reset
                    curRequest = new HttpRequest();
                    currentState = State.READ_INITIAL;
                }
                case READ_VARIABLE_LENGTH_CONTENT: {
                    //暂时默认这种情况不处理
                }
            }
        }
        return start;
    }

    /**
     * 解析请求行[method uri version]
     *
     * @param start   开始读取请求行的第一个字符下标
     * @param buffer  缓冲
     * @param request HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int encodeRequestLine(int start, ByteBuffer buffer, HttpRequest request) {
        List<Byte> contentList = new ArrayList<>();
        int limit = buffer.limit();
        int index = start;
        byte cur = buffer.get(start);
        while (cur != '\r') {
            contentList.add(cur);
            index++;
            if (index == limit) { //buffer已经读到末尾，但是没有找到请求行的结束符(\r\n)
                return -1;
            }

            cur = buffer.get(index);
        }

        index++;
        cur = buffer.get(index); //read the '\n'


        String[] info = byte2String(contentList).split("\\s");
        request.setMethod(HttpMethod.valueOf(info[0])); // method
        request.setUri(info[1]); //uri
        request.setVersion(HttpVersion.valueOf(info[2]));

        return index + 1;
    }

    /**
     * 解析请求首部
     *
     * @param start   开始读取请求行的第一个字符下标
     * @param buffer  缓冲
     * @param request HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int encodeRequestHeaders(int start, ByteBuffer buffer, HttpRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders(); //解析时创建，不能调用给上层提供的接口

        int limit = buffer.limit();
        int index = start;
        int countEnd = 0;
        while (countEnd != 2) {
            if (index >= limit) {
                return -1;
            }

            List<Byte> contentList = new ArrayList<>();
            byte cur = buffer.get(index);
            while (cur != '\r') {
                countEnd = 0; //恢复

                contentList.add(cur);
                index++;
                if (index == limit) { //buffer已经读到末尾，但是没有找到请求行的结束符(\r\n)
                    return -1;
                }
                cur = buffer.get(index);
            }

            index++; //指向'\n'
            cur = buffer.get(index); //read the '\n'
            index++;//指向下一行的起始字符

            if (countEnd == 1) {
                break; //读到了连续的两个\r\n
            }
            String[] header = byte2String(contentList).split(":");
            httpHeaders.set(header[0].trim(), header[1].trim());
            countEnd++;
        }

        request.setHeaders(httpHeaders);
        return index;
    }

    /**
     * 解析请求内容
     *
     * @param start         开始读取请求行的第一个字符下标
     * @param contentLength content的长度
     * @param buffer        缓冲
     * @param request       HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int encodeRequestContent(int start, int contentLength, ByteBuffer buffer, HttpRequest request) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(contentLength); //HeapByteBuffer
        int limit = buffer.limit();
        for (int i = 0; i < contentLength; i++) {
            if (start + i < limit) {
                byteBuffer.put(buffer.get(start + i));
            } else {
                return -1;
            }
        }
        request.setContent(byteBuffer);
        return start + contentLength;
    }

    /**
     * 根据字节list获取UTF-8编码的String
     *
     * @param list 字节list
     * @return UTF-8编码的String
     */
    private String byte2String(List<Byte> list) {
        byte[] info = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            info[i] = list.get(i);
        }
        return new String(info, StandardCharsets.UTF_8);
    }

    /**
     * 当前HttpRequest的解析状态
     */
    private enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED
    }
}
