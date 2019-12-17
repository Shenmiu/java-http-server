package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.ChannelBuffer;
import cn.edu.nju.nioserver.core.Protocol;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:31
 * @description
 **/
public class HttpProtocol implements Protocol {

    public HttpProtocol() {
    }

    @Override
    public void process(ChannelBuffer channelBuffer) {
        ByteBuffer buffer = ByteBuffer.allocate(ChannelBuffer.BUFFER_SIZE);
        channelBuffer.pollRead(buffer);
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
}
