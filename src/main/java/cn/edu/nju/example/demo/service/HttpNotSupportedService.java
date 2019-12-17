package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.http.HttpService;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;

public class HttpNotSupportedService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // 不支持简单返回 404
        response.setStatus(HttpResponseStatus.NOT_FOUND);
    }

}
