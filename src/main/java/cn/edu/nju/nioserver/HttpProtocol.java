package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.ChannelHandler;
import cn.edu.nju.nioserver.core.ChannelHandlerContext;
import cn.edu.nju.nioserver.http.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Use {@link HttpMessageCodec} instead, and remove this one.
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

//    public void process(ChannelBuffer channelBuffer) {
//        ByteBuffer buffer = ByteBuffer.allocate(ChannelBuffer.BUFFER_SIZE);
//        channelBuffer.pollRead(buffer);
//        int prevSize = requests.size();
//        int bytesRead = decoder.decode(0, buffer, requests);
//        // parse fail
//        if (prevSize == requests.size()) {
//            buffer.position(bytesRead);
//            int length = buffer.limit() - bytesRead;
//            ByteBuffer cacheBuffer = ByteBuffer.allocate(length);
//            for (int i = bytesRead; i < buffer.limit(); i++) {
//                cacheBuffer.put(buffer.get(i));
//            }
//
//            //add回去进行缓存
//            channelBuffer.addFirstToRead(cacheBuffer, length);
//        } else {
//
//            HttpResponse response = new HttpResponse();
//            //上层应用调用
//            controller.service(requests.get(requests.size() - 1), response);
//
//            //response encode(暂时不考虑分块)
//            List<Byte> content = new ArrayList<>();
//            encoder.encode(response, content);
//            ByteBuffer responseBuffer = ByteBuffer.allocate(content.size());
//            for (Byte e : content) {
//                responseBuffer.put(e);
//            }
//            channelBuffer.addToWrite(responseBuffer, content.size());
//        }
//
//        byte[] bytesToWrite = ("HTTP/1.1 200 OK\r\n" +
//                "Content-Length: 38\r\n" +
//                "Content-Type: text/html\r\n" +
//                "\r\n" +
//                "<html><body>Hello World!</body></html><br/>").getBytes();
//        for (int i = 0; i < bytesToWrite.length; i += ChannelBuffer.BUFFER_SIZE) {
//            buffer.clear();
//            int nBytes = Math.min(ChannelBuffer.BUFFER_SIZE, bytesToWrite.length - i);
//            buffer.put(bytesToWrite, i, nBytes);
//            channelBuffer.addToWrite(buffer, nBytes);
//        }
//    }
//
//    public void read(Object object) {
//        if (object instanceof ChannelBuffer) {
//            ChannelBuffer channelBuffer = (ChannelBuffer) object;
//            process(channelBuffer);
//        }
//    }

//    public void write(Object object) {
//    }

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void handleDownStream(ChannelHandlerContext ctx, Object msg) {

    }
}
