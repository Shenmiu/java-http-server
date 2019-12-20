package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public interface HttpMethodServiceInt {
    /**
     * 根据各方法分别处理
     *
     * @return Content-Length 是否需要修改
     */
    boolean process(HttpRequest request, HttpResponse response);
}
