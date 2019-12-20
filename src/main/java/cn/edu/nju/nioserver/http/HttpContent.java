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

    public void setContent(String content) {
        this.content = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取buffer的length
     *
     * @return int
     */
    public int size() {
        if (content == null || content.limit() == 0) {
            return 0;
        }
        return content.limit();
    }

    /**
     * 判断buffer是否为空
     *
     * @return int
     */
    public boolean isEmpty() {
        return size() == 0;
    }
}
