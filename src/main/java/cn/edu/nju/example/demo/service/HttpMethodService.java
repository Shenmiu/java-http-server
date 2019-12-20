package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.method.HttpMethodServiceFactory;
import cn.edu.nju.example.demo.service.method.HttpMethodServiceInt;
import cn.edu.nju.nioserver.http.HttpMethod;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpService;

public class HttpMethodService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        HttpMethodServiceInt curMethodService = HttpMethodServiceFactory.getMethodService(curMethod);
        curMethodService.process(request, response);
    }

}
