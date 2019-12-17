package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.http.HttpMethod;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpService;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Hello, you have accessed a url with ")
                .append(curMethod.name().toLowerCase())
                .append(" method");
        response.content().setByteBuffer(responseBuilder.toString());
    }

}
