package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.DemoService;
import cn.edu.nju.nioserver.http.HttpMethod;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DemoMethodService implements DemoService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Hello, you have accessed a url with ")
                .append(curMethod.name().toLowerCase())
                .append(" method");
        response.setContent(ByteBuffer.wrap(responseBuilder.toString().getBytes(StandardCharsets.UTF_8)));
    }

}
