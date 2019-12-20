package cn.edu.nju.example;

import cn.edu.nju.nioserver.core.TcpServer;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    private TcpServer tcpServer;

    public HttpServer() {
        this.tcpServer = new TcpServer(new HttpServerInitializer());
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
