package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpMethod;

public class HttpMethodServiceFactory {

    public static HttpMethodServiceInt getMethodService(HttpMethod method) {
        // 不支持 CONNECT 方法
        if (method == HttpMethod.DELETE) {
            return new HttpMethodServiceDelete();
        } else if (method == HttpMethod.GET) {
            return new HttpMethodServiceGet();
        } else if (method == HttpMethod.HEAD) {
            return new HttpMethodServiceHead();
        } else if (method == HttpMethod.OPTIONS) {
            return new HttpMethodServiceOptions();
        } else if (method == HttpMethod.POST) {
            return new HttpMethodServicePost();
        } else if (method == HttpMethod.PUT) {
            return new HttpMethodServicePut();
        } else if (method == HttpMethod.TRACE) {
            return new HttpMethodServiceTrace();
        } else if (method == HttpMethod.PATCH) {
            // HTTP/1.1 不包含 patch
            return new HttpMethodServiceNotSupported();
        } else {
            return new HttpMethodServiceNotSupported();
        }
    }
}
