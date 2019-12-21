package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;

public class HttpMethodServiceNotSupported implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        response.content().setContent("You have send a request with a http method not supported.");
        response.setStatus(HttpResponseStatus.METHOD_NOT_ALLOWED);
        return true;
    }
}
