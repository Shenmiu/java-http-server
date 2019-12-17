package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.ChannelBuffer;
import cn.edu.nju.nioserver.core.ChannelPipeline;
import cn.edu.nju.nioserver.core.ChannelPipelineFactory;
import cn.edu.nju.nioserver.core.TCPServer;

import java.nio.channels.Channel;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        this.tcpServer = new TCPServer(port, () -> new ChannelPipeline(new ChannelBuffer(), new HttpProtocol()));
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
