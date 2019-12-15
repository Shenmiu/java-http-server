package cn.edu.nju.nioserver.http;

public class HttpResponse {

    /**
     * 响应具体内容
     */
    private String content;

    /**
     * 响应状态码
     */
    private HttpResponseStatus status;

    /**
     * 协议版本（HTTP）
     */
    private HttpVersion version;

    /**
     * 响应首部
     */
    private HttpHeaders headers;

    public HttpResponse(String content, HttpResponseStatus status, HttpVersion version, HttpHeaders headers) {
        this.content = content;
        this.status = status;
        this.version = version;
        this.headers = headers;
    }

    public String content() {
        return content;
    }

    public HttpResponseStatus status() {
        return status;
    }

    public HttpVersion version() {
        return version;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
}
