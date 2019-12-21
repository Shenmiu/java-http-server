package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class HttpMethodServiceConnect implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        return false;
    }
}
