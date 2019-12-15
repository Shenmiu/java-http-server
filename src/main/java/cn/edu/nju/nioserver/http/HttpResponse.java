package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;

public class HttpResponse extends HttpMessage {

    /**
     * 响应状态码
     */
    private HttpResponseStatus status;

    public HttpResponse() {
    }

    public HttpResponse(ByteBuffer content, HttpResponseStatus status, HttpVersion version, HttpHeaders headers) {
        super(version, content, headers);
        this.status = status;
    }

    public HttpResponseStatus status() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }
}
