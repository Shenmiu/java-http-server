package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.ChannelBuffer;
import cn.edu.nju.nioserver.core.ChannelHandler;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpRequestDecoder;
import cn.edu.nju.nioserver.http.HttpResponseEncoder;
import cn.edu.nju.nioserver.http.HttpService;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:31
 * @description
 **/
public class HttpProtocol implements ChannelHandler {

    private HttpRequestDecoder decoder;
    private HttpResponseEncoder encoder;
    private HttpService controller;
    private List<HttpRequest> requests;

    public HttpProtocol(HttpRequestDecoder decoder, HttpResponseEncoder encoder, HttpService controller) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.controller = controller;
        this.requests = new ArrayList<>();
    }

    public void process(ChannelBuffer channelBuffer) {
        ByteBuffer buffer = ByteBuffer.allocate(ChannelBuffer.BUFFER_SIZE);
        channelBuffer.pollRead(buffer);

        int prevSize = requests.size();
        int bytesRead = decoder.decode(0, buffer, requests);
        // parse fail
        if (prevSize == requests.size()) {
            buffer.position(bytesRead);
            int length = buffer.limit() - bytesRead - 1;
        } else {

        }

        byte[] bytesToWrite = ("HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html><br/>").getBytes();
        for (int i = 0; i < bytesToWrite.length; i += ChannelBuffer.BUFFER_SIZE) {
            buffer.clear();
            int nBytes = Math.min(ChannelBuffer.BUFFER_SIZE, bytesToWrite.length - i);
            buffer.put(bytesToWrite, i, nBytes);
            channelBuffer.addToWrite(buffer, nBytes);
        }
    }

    @Override
    public void read(Object object) {
        if (object instanceof ChannelBuffer) {
            ChannelBuffer channelBuffer = (ChannelBuffer) object;
            process(channelBuffer);
        }
    }

    @Override
    public void write(Object object) {
    }
}
