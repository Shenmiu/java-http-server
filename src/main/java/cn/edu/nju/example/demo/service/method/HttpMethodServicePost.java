package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.example.demo.service.method.util.QueryStringDecoder;
import cn.edu.nju.nioserver.http.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpMethodServicePost implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        String curContentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        switch (curContentType) {
            case HttpHeaderValues.TEXT_PLAIN:
                processTextPlain(request, response);
                break;
            case HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED:
                processAppFormUrlencoded(request, response);
                break;
        }
        return true;
    }

    private void processTextPlain(HttpRequest request, HttpResponse response) {
        ByteBuffer contentBuf = request.content().byteBuffer();
        String plainText = new String(contentBuf.array(), 0, contentBuf.array().length, StandardCharsets.UTF_8);
        processData(request, response,
                "You have send a post request with content type = text/plain.\n" +
                        "The plain text is: " + plainText + "\n");
    }

    private void processAppFormUrlencoded(HttpRequest request, HttpResponse response) {
        ByteBuffer contentBuf = request.content().byteBuffer();
        String formStr = new String(contentBuf.array(), 0, contentBuf.array().length, StandardCharsets.UTF_8);

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("You have send a post request with content type = application/x-www-form-urlencoded.\n");
        try {
            QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri() + "?" + formStr);
            Map<String, List<String>> uriAttributes = decoderQuery.parameters();

            responseBuilder.append("The data is: \n");
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    responseBuilder.append(attr.getKey() + ": " + attrVal + "\n");
                }
            }
        } catch (RuntimeException e) {
            responseBuilder.append("The body cannot be resolved.\n");
        }
        processData(request, response, responseBuilder.toString());
    }

    private void processData(HttpRequest request, HttpResponse response, String content) {
        String curFileName = FileUtil.realFileName(request.uri());
        String wantedFilePreContent = FileUtil.read(curFileName);
        if (wantedFilePreContent == null) {
            // 新资源被创建
            FileUtil.write(curFileName, content, true);
            response.setStatus(HttpResponseStatus.CREATED);
        } else {
            // 现有资源已被更改
            FileUtil.write(curFileName, content, false);
            response.setStatus(HttpResponseStatus.OK);
        }

        String nowContent = FileUtil.read(curFileName);
        response.content().setContent(nowContent);
    }
}
