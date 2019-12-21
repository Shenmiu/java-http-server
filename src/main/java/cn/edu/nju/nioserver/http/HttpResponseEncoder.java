package cn.edu.nju.nioserver.http;

import cn.edu.nju.nioserver.util.BytesUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpResponseEncoder {

    /**
     * 当前encoder的解析状态
     */
    private State curState = State.ENCODE_INITIAL;

    /**
     * 将HttpResponse编码为字节流(调用者需要根据target的长度是否变化来判定是否有解析好的字节流)
     *
     * @param message [HttpResponse || HttpContent]
     * @param target  字节流
     */
    public void encode(Object message, List<Byte> target) {
        List<Byte> curContent = new ArrayList<>();
        boolean isEnd = false;
        while (!isEnd) {
            switch (curState) {
                case ENCODE_INITIAL: {
                    if (message instanceof HttpResponse) {
                        encodeResponseLine((HttpResponse) message, curContent);
                        curState = State.ENCODE_HEADER;
                    } else {
                        throw new IllegalArgumentException("data is error");
                    }
                    //接下来直接处理Header，不break
                }
                case ENCODE_HEADER: {
                    //认定响应行与首部是绑定在一起，不需要继续判断当前的message是否为HttpResponse
                    HttpResponse response = (HttpResponse) message;
                    encodeResponseHeaders(response, curContent);

                    if (response.headers().containsContentLength()) {
                        if (response.headers().get(HttpHeaderNames.CONTENT_LENGTH).equals("0")) {
                            //content-length为0
                            target.addAll(curContent);

                            //reset
                            reset();
                            curContent.clear();
                            //结束当前解析
                            isEnd = true;
                        } else {
                            curState = State.ENCODE_FIXED_LENGTH_CONTENT;
                        }
                    } else if (response.headers().isChunkTransfer()) {
                        curState = State.ENCODE_VARIABLE_LENGTH_CONTENT;
                    } else {
                        target.addAll(curContent);

                        //reset
                        reset();
                        curContent.clear();
                        //结束当前解析
                        isEnd = true;
                    }

                    break;
                }
                case ENCODE_FIXED_LENGTH_CONTENT: {
                    //认定响应行与首部是绑定在一起，已继续判断当前的message为HttpResponse

                    encodeResponseContent((HttpResponse) message, curContent);

                    // buffer.put(getBuffer(curContent));
                    //response已经拿到了全部的内容
                    target.addAll(curContent);

                    //reset
                    reset();
                    curContent.clear();
                    //结束当前解析
                    isEnd = true;
                    break;
                }
                case ENCODE_VARIABLE_LENGTH_CONTENT: {
                    //判断为chunk的方式，先将响应行和响应首部绑定为一块进行发送
                    target.addAll(curContent);
                    //buffer.put(getBuffer(curContent));

                    //清空以放入接下来的块
                    curContent.clear();
                    curState = State.ENCODE_WAIT_CONTENT;

                    //不确定当前已判断为HttpResponse中HttpContent是否有数据
                    //可能已经放入了第一个块
                    HttpResponse response = (HttpResponse) message;
                    if (response.content() != null && !response.content().isEmpty()) {
                        encodeChunkContent(((HttpResponse) message).content(), curContent);

                        target.addAll(curContent);
                        //buffer.put(getBuffer(curContent));

                        //清空以放入接下来的块
                        curContent.clear();
                    }
                    isEnd = true;
                    break;
                }
                case ENCODE_WAIT_CONTENT: {
                    //一旦进入此状态，会一直处于这个状态直到当前对象被销毁
                    //对于最后的空块，会由传入HttpContent来获取长度0和两个"\r\n"

                    if (message instanceof HttpContent) {
                        encodeChunkContent((HttpContent) message, curContent);

                        target.addAll(curContent);
                        //buffer.put(getBuffer(curContent));

                        //清空以放入接下来的块
                        curContent.clear();

                        //判断当前为0块,重置状态
                        if (((HttpContent) message).isEmpty()) {
                            reset();
                        }
                    } else {
                        throw new IllegalArgumentException("data is error");
                    }

                    //当前解析结束
                    isEnd = true;
                    break;
                }
            }
        }
    }

    /**
     * 编码响应行
     *
     * @param response   HttpResponse
     * @param curContent 当前的字节流
     */
    private void encodeResponseLine(HttpResponse response, List<Byte> curContent) {
        StringBuilder builder = new StringBuilder();
        //HTTP协议版本号
        builder.append(response.version().text())
                .append(" ")
                //状态码
                .append(response.status().code())
                .append(" ")
                //状态码消息
                .append(response.status().reasonPhrase())
                .append("\r\n");
        byte[] result = builder.toString().getBytes(StandardCharsets.UTF_8);

        //先写入content，后面依据状态写入下一层
        addContent(result, curContent);
    }

    /**
     * 编码响应首部
     *
     * @param response   HttpResponse
     * @param curContent 当前的字节流
     */
    private void encodeResponseHeaders(HttpResponse response, List<Byte> curContent) {
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
        //先写入content，后面依据状态写入下一层
        addContent(result, curContent);
    }

    /**
     * 编码响应内容
     *
     * @param response   HttpResponse
     * @param curContent 当前的字节流
     */
    private void encodeResponseContent(HttpResponse response, List<Byte> curContent) {
        if (!response.content().isEmpty()) {
            ByteBuffer content = response.content().byteBuffer();
            //先写入content，后面依据状态写入下一层
            addContent(content.array(), curContent);
        }
    }

    /**
     * 包装chunk块
     *
     * @param content    chunk的内容
     * @param curContent 当前的字节流
     */
    private void encodeChunkContent(HttpContent content, List<Byte> curContent) {
        ByteBuffer buffer = content.byteBuffer();
        //size
        this.addContent(BytesUtil.int2Bytes(buffer.limit()), curContent);
        //"\r\n"
        this.addSeparator(curContent);
        //content
        this.addContent(buffer, curContent);
        //"\r\n"
        this.addSeparator(curContent);
    }

    /**
     * 将byte数组写入content
     *
     * @param message 字节数组
     */
    private void addContent(byte[] message, List<Byte> curContent) {
        for (byte e : message) {
            curContent.add(e);
        }
    }

    /**
     * 将byte buffer写入content
     *
     * @param buffer 字节流
     */
    private void addContent(ByteBuffer buffer, List<Byte> curContent) {
        if (buffer.hasArray()) {
            addContent(buffer.array(), curContent);
        } else {
            int limit = buffer.limit();
            for (int i = 0; i < limit; i++) {
                curContent.add(buffer.get(i));
            }
        }
    }

    /**
     * 添加分隔符
     */
    private void addSeparator(List<Byte> curContent) {
        byte[] separator = "\r\n".getBytes(StandardCharsets.UTF_8);
        for (byte e : separator) {
            curContent.add(e);
        }
    }

    /**
     * 将当前encoder的内容写入ByteBuffer中
     *
     * @return ByteBuffer
     */
    private ByteBuffer getBuffer(List<Byte> curContent) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(curContent.size());
        for (Byte b : curContent) {
            byteBuffer.put(b);
        }
        return byteBuffer;
    }

    /**
     * 重置当前encoder的内容
     */
    private void reset() {
        curState = State.ENCODE_INITIAL;
    }

    /**
     * 当前HttpResponse的解析状态
     */
    private enum State {
        ENCODE_INITIAL,
        ENCODE_HEADER,
        ENCODE_VARIABLE_LENGTH_CONTENT,
        ENCODE_FIXED_LENGTH_CONTENT,
        ENCODE_WAIT_CONTENT
    }
}
