package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;

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
}
