package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.http.*;

import java.nio.charset.StandardCharsets;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Hello, you have accessed a url with ")
                .append(curMethod.name().toLowerCase())
                .append(" method");
        //后面此处需要考虑如何设计，无法默认长度OPTIONS（可以是chunk的方式）
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "" + responseBuilder.toString().getBytes(StandardCharsets.UTF_8).length);
        response.content().setContent(responseBuilder.toString());
    }

}
