package cn.edu.nju.nioserver.http;

/**
 * demo
 */
public interface HttpService {

    /**
     * 对该请求进行相应处理
     */
    void service(HttpRequest request, HttpResponse response);
}
