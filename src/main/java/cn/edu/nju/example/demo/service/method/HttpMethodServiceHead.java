package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

import java.nio.charset.StandardCharsets;

public class HttpMethodServiceHead implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        // 找到真正的 fileName
        String curFileName = request.uri().substring(8);
        int paramMarkIndex = curFileName.indexOf("?");
        if (paramMarkIndex != -1) {
            curFileName = curFileName.substring(0, paramMarkIndex);
        }

        String fileContent = FileUtil.read(curFileName);
        if (fileContent == null) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "0");
        } else {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                    String.valueOf(fileContent.getBytes(StandardCharsets.UTF_8).length));
        }
        return false;
    }
}
