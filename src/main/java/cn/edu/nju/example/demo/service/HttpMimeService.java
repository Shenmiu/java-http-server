package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpHeaderValues;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import sun.misc.BASE64Encoder;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HttpMimeService implements HttpService {
    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String path = request.uri();
        String file = path.substring(6);
//        String pre = type.split("/")[0];
        String type = file.split("\\.")[file.split("\\.").length - 1];
        byte[] data = FileUtil.getResource(file);
        response.content().setByteBuffer(ByteBuffer.wrap(data));
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(data.length));
        switch (type){
            case "jpg":
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/jpeg");
                break;
            case "gif":
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/gif");
                break;
            case "html":
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
                break;
            case "plain":
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                break;
            case "mp4":
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "video/mp4");
                break;

        }
    }
}
