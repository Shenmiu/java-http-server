package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.*;

public class HttpMethodServiceTrace implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.MESSAGE_HTTP);
        response.setStatus(HttpResponseStatus.OK);
        return false;
    }
}
