package cn.edu.nju.nioserver;

import cn.edu.nju.example.demo.HttpServiceController;
import cn.edu.nju.nioserver.core.ChannelBuffer;
import cn.edu.nju.nioserver.core.ChannelPipeline;
import cn.edu.nju.nioserver.core.TCPServer;
import cn.edu.nju.nioserver.http.HttpRequestDecoder;
import cn.edu.nju.nioserver.http.HttpResponseEncoder;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        this.tcpServer = new TCPServer(port, () -> new ChannelPipeline(new ChannelBuffer(),
                new HttpProtocol(new HttpRequestDecoder(), new HttpResponseEncoder(),
                        HttpServiceController.controller)));
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
