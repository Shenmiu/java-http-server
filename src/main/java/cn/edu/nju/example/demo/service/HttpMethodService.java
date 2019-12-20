package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.method.HttpMethodServiceFactory;
import cn.edu.nju.example.demo.service.method.HttpMethodServiceInt;
import cn.edu.nju.nioserver.http.*;

import java.nio.charset.StandardCharsets;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        HttpMethodServiceInt curMethodService = HttpMethodServiceFactory.getMethodService(curMethod);
        curMethodService.process(request, response);

        // 设置响应的 content-length
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                new String(response.content().byteBuffer().array(), StandardCharsets.UTF_8));
    }

}
