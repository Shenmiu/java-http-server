package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.example.demo.service.method.util.QueryStringDecoder;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

import java.util.List;
import java.util.Map;

public class HttpMethodServiceGet implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("You have send a get request.")
                .append("The requested url params are: \n");

        QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
        Map<String, List<String>> uriAttributes = decoderQuery.parameters();
        for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
            for (String attrVal : attr.getValue()) {
                responseBuilder.append(attr.getKey() + ": " + attrVal + "\n");
            }
        }

        String curFileName = FileUtil.realFileName(request.uri());
        String fileContent = FileUtil.read(curFileName);
        if (fileContent == null) {
            responseBuilder.append("Requested file ").append(curFileName).append(" does not exist.");
        } else {
            responseBuilder.append("Requested file ").append(curFileName).append("'s content is: \n");
            responseBuilder.append(fileContent);
        }
        response.content().setContent(responseBuilder.toString());
        return true;
    }
}
