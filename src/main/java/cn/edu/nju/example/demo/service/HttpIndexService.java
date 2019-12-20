package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.HttpService;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class HttpIndexService implements HttpService {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            StringBuilder result = new StringBuilder();
            RandomAccessFile indexFile = new RandomAccessFile
                    (getClass().getClassLoader().getResource("index.html").getPath(), "r");
            FileChannel indexChannel = indexFile.getChannel();

            ByteBuffer indexBuf = ByteBuffer.allocate(512);
            int bytesRead;
            while ((bytesRead = indexChannel.read(indexBuf)) != -1) {
                result.append(new String(indexBuf.array(), 0, bytesRead, StandardCharsets.UTF_8));
                indexBuf.rewind();
            }
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(result.toString().getBytes().length));
            response.content().setContent(result.toString());
            indexChannel.close();
        } catch (IOException e) {
            response.content().setContent(("Read file failed."));
        }
    }

}
