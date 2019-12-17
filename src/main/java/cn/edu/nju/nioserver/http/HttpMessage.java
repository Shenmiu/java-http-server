package cn.edu.nju.nioserver.http;

public class HttpMessage {
    /**
     * 协议版本对象
     */
    private HttpVersion version;

    /**
     * 响应具体内容
     */
    private HttpContent content;

    /**
     * HTTP首部
     */
    private HttpHeaders headers;

    public HttpMessage() {
    }

    public HttpMessage(HttpVersion version, HttpContent content, HttpHeaders headers) {
        this.version = version;
        this.content = content;
        this.headers = headers;
    }

    public HttpVersion version() {
        return version;
    }

    public HttpContent content() {
        return content;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
}
