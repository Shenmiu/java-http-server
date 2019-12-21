package cn.edu.nju.example.demo.service.intf;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

/**
 * demo
 */
public interface HttpService {

    /**
     * 对该请求进行相应处理
     */
    void service(HttpRequest request, HttpResponse response);
}
