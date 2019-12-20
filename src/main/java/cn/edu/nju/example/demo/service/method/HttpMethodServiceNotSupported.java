package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class HttpMethodServiceNotSupported implements HttpMethodServiceInt {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("You have send a request with a http method not supported.\n");
        response.content().setContent(responseBuilder.toString());
    }
}
