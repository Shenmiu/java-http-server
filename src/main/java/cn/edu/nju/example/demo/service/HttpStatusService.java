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
        switch (status) {
            case "200":
                response.setStatus(HttpResponseStatus.valueOf(200));
                break;
            case "301":
                response.setStatus(HttpResponseStatus.valueOf(301));
                break;
            case "404":
                response.setStatus(HttpResponseStatus.valueOf(404));
                break;
            case "500":
                response.setStatus(HttpResponseStatus.valueOf(500));
                break;
        }
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(response.version().text()).append(" ")
                .append(response.status().codeAsText()).append(" ")
                .append(response.status().reasonPhrase()).append("<br/>");
        HttpHeaders headers = response.headers();
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
            response.content().setContent(result.toString());
            statusChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置响应的 content-length
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                String.valueOf(response.content().byteBuffer().array().length));
    }
}
