package cn.edu.nju.nioserver.http;

public class HttpResponse extends HttpMessage {

    /**
     * 响应状态码
     */
    private HttpResponseStatus status;

    public HttpResponse() {
        this(new HttpContent(), HttpResponseStatus.OK, HttpVersion.HTTP_1_1, HttpHeaders.defaultHeaders);
    }

    public HttpResponse(HttpContent content) {
        this(content, HttpResponseStatus.OK, HttpVersion.HTTP_1_1, HttpHeaders.defaultHeaders);
    }

    public HttpResponse(HttpContent content, HttpResponseStatus status, HttpVersion version, HttpHeaders headers) {
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
