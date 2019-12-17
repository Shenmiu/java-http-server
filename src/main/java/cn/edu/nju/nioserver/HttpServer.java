package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.ChannelHandler;
import cn.edu.nju.nioserver.core.TCPServer;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        HttpProtocol httpProtocol = new HttpProtocol();
        this.tcpServer = new TCPServer(port, new ChannelHandler(httpProtocol));
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
