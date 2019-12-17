package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.DemoService;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class DemoNotSupportedService implements DemoService {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // TODO fjj 讨论不支持返回什么
    }
}
