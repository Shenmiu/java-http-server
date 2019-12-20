package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.QueryStringDecoder;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpHeaderValues;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

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
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("You have send a post request with content type = text/plain.\n")
                .append("The plain text is: ")
                .append(plainText);
        response.content().setContent(responseBuilder.toString());
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
        response.content().setContent(responseBuilder.toString());
    }
}
