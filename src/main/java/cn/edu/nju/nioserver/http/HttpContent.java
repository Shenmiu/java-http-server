package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpContent {

    /**
     * 内容
     */
    private ByteBuffer content;

    public HttpContent() {
    }

    public HttpContent(ByteBuffer content) {
        this.content = content;
    }

    public ByteBuffer byteBuffer() {
        return content;
    }

    public void setByteBuffer(ByteBuffer content) {
        this.content = content;
    }

    public void setByteBuffer(String content) {
        this.content = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
    }
}
