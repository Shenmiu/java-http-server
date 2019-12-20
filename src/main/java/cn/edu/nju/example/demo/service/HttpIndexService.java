package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.HttpService;
import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

public class HttpIndexService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String indexContent = FileUtil.readResourceFile("index.html");
        response.content().setContent(indexContent == null ? "Read file failed." : indexContent);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                String.valueOf(response.content().byteBuffer().array().length));
    }

}
