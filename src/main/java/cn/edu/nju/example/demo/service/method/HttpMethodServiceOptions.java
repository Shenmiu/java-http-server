package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpHeaders;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class HttpMethodServiceOptions implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        // 可以使用 OPTIONS 方法对服务器发起请求，以检测服务器支持哪些 HTTP 方法
        HttpHeaders curHeaders = response.headers();
        curHeaders.set(HttpHeaderNames.ALLOW, "CONNECT, DELETE, GET, HEAD, OPTIONS, POST, PUT, TRACE");
        return true;
    }
}
