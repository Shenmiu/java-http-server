package cn.edu.nju.nioserver.http;

import java.nio.ByteBuffer;

public class HttpRequest extends HttpMessage {

    /**
     * request操作方法
     */
    private HttpMethod method;

    /**
     * uri
     */
    private String uri;

    public HttpRequest(HttpVersion version, HttpMethod method, HttpHeaders headers, String uri, ByteBuffer content) {
        super(version, content, headers);
        this.method = method;
        this.uri = uri;
    }

    public HttpMethod method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
