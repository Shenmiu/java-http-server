package cn.edu.nju.nioserver.http;

public class HttpRequest {

    /**
     * 协议版本对象
     */
    private HttpVersion version;

    /**
     * request操作方法
     */
    private HttpMethod method;

    /**
     * HTTP首部
     */
    private HttpHeaders headers;

    /**
     * uri
     */
    private String uri;

    /**
     * body内容
     */
    private String content;

    public HttpRequest(HttpVersion version, HttpMethod method, HttpHeaders headers, String uri, String content) {
        this.version = version;
        this.method = method;
        this.headers = headers;
        this.uri = uri;
        this.content = content;
    }

    public HttpVersion version() {
        return version;
    }

    public HttpMethod method() {
        return method;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public String uri() {
        return uri;
    }

    public String content() {
        return content;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
