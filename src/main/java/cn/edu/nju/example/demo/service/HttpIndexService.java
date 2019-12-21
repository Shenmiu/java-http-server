package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

import java.nio.ByteBuffer;

public class HttpIndexService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
//        String indexContent = FileUtil.readResourceFile("index.html");
        byte[] data = FileUtil.getResource("index.html");
//        assert indexContent != null;
        response.content().setByteBuffer(ByteBuffer.wrap(data));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(data.length));
    }

}
