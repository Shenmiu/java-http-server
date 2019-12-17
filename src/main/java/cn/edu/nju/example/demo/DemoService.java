package cn.edu.nju.example.demo;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

/**
 * demo
 */
public interface DemoService {

    /**
     * 对该请求进行相应处理
     */
    void service(HttpRequest request, HttpResponse response);
}
