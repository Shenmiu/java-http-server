package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.example.demo.service.method.HttpMethodServiceFactory;
import cn.edu.nju.example.demo.service.method.HttpMethodServiceInt;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpMethod;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        HttpMethodServiceInt curMethodService = HttpMethodServiceFactory.getMethodService(curMethod);
        boolean contentLengthToModify = curMethodService.process(request, response);

        if (contentLengthToModify) {
            // 设置响应的 content-length
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                    String.valueOf(response.content().byteBuffer().array().length));
        }
    }

}
