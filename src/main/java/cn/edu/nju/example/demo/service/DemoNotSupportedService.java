package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.DemoService;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;

public class DemoNotSupportedService implements DemoService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // 不支持简单返回 404
        response.setStatus(HttpResponseStatus.NOT_FOUND);
    }

}
