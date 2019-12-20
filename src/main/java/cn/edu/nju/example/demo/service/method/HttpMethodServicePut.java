package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpMethodServicePut implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        HttpContent content = request.content();
        ByteBuffer contentBuf = content.byteBuffer();
        String contentStr = new String(contentBuf.array(), 0, contentBuf.array().length, StandardCharsets.UTF_8);

        String curFileName = FileUtil.realFileName(request.uri());
        String wantedFilePreContent = FileUtil.read(curFileName);
        if (wantedFilePreContent == null) {
            // 目标资源不存在，并且PUT方法成功创建了一份，那么源头服务器必须返回201 (Created) 来通知客户端资源已创建。
            response.setStatus(HttpResponseStatus.CREATED);
        } else {
            // 目标资源已经存在，并且依照请求中封装的表现形式成功进行了更新，
            // 源头服务器必须返回200 (OK) 或者204 (No Content) 来表示请求的成功完成。
            response.setStatus(HttpResponseStatus.OK);
        }
        FileUtil.write(curFileName, contentStr, true);
        response.headers().set(HttpHeaderNames.CONTENT_LOCATION, curFileName);
        return false;

    }
}
