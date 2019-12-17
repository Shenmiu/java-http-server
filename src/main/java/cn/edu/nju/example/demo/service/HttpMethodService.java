package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.http.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Hello, you have accessed a url with ")
                .append(curMethod.name().toLowerCase())
                .append(" method");
        response.setContent(new HttpContent(ByteBuffer.wrap(responseBuilder.toString().getBytes(StandardCharsets.UTF_8))));
    }

}
