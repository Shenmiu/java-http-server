package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.nioserver.http.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class HttpStatusService implements HttpService {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // TODO hgc
        String[] split = request.uri().split("/");
        String status = split[split.length - 1];
        HttpHeaders headers = response.headers();
        switch (status) {
            case "200":
                headers.set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
                response.setStatus(HttpResponseStatus.valueOf(200));
                break;
            case "301":
                response.setStatus(HttpResponseStatus.valueOf(301));
                headers.set(HttpHeaderNames.LOCATION, "http://localhost:8080/status/200");
                break;
            case "404":
                headers.set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
                response.setStatus(HttpResponseStatus.valueOf(404));
                break;
            case "500":
                headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                headers.set("Error Message", "An error has occurred on the server");
                response.setStatus(HttpResponseStatus.valueOf(500));
                break;
        }
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(response.version().text()).append(" ")
                .append(response.status().codeAsText()).append(" ")
                .append(response.status().reasonPhrase()).append("<br/>");
        headers.set(HttpHeaderNames.CONTENT_LANGUAGE, "zh-CN");
        headers.set(HttpHeaderNames.CONTENT_LENGTH, "0");
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        Iterator<Map.Entry<String, String>> headersIterator = headers.headersIterator();
        while (headersIterator.hasNext()) {
            Map.Entry<String, String> entry = headersIterator.next();
            responseBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append("<br/>");
        }
        StringBuilder result = new StringBuilder();
        try {
            RandomAccessFile statusFile = new RandomAccessFile
                    (getClass().getClassLoader().getResource("status.html").getPath(), "r");
            FileChannel statusChannel = statusFile.getChannel();
            ByteBuffer indexBuf = ByteBuffer.allocate(512);
            int bytesRead;
            while ((bytesRead = statusChannel.read(indexBuf)) != -1) {
                result.append(new String(indexBuf.array(), 0, bytesRead, StandardCharsets.UTF_8));
                indexBuf.rewind();
            }
            result.insert(result.indexOf("</div>"), responseBuilder.toString());
            int contextLength = result.toString().getBytes().length;
            String contextLengthStr = String.valueOf(contextLength);
            contextLength += contextLengthStr.getBytes().length - "0".getBytes().length;
            result.replace(result.indexOf("0<"), result.indexOf("<br/>D"), String.valueOf(contextLength));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(contextLength));
            response.content().setContent(result.toString());
            statusChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
