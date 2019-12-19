package cn.edu.nju.example;

import cn.edu.nju.nioserver.ByteToMessageCodec;
import cn.edu.nju.nioserver.HttpServer;
import cn.edu.nju.nioserver.core.ChannelPipeline;
import cn.edu.nju.nioserver.core.TcpServer;

import java.util.List;

/**
 * @author jjenkov
 * @date 19-10-2015
 * <p>
 * http example
 */
public class Main {

    public static void main(String[] args) {
        TcpServer server = new TcpServer(() -> new ChannelPipeline(new ByteToMessageCodec() {
            @Override
            protected boolean decode(List<Byte> in, Object out) {
                return false;
            }

            @Override
            protected boolean encode(Object in, List<Byte> out) {
                byte[] bytesToWrite = ("HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 38\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "<html><body>Hello World!</body></html><br/>").getBytes();
                for (byte b : bytesToWrite) {
                    out.add(b);
                }
                return true;
            }
        }));
        server.startServer();
    }

}
