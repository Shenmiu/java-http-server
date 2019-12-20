package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpService;

public class HttpIndexService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String indexContent = FileUtil.read("index.html");
        response.content().setContent(indexContent == null ? "Read file failed." : indexContent);
    }

}
