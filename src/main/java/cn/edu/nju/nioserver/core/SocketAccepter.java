package cn.edu.nju.nioserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * 通过 ServerSocketChannel 监听 TCP 连接请求
 *
 * @author jjenkov
 * @date 16-10-2015
 */
public class SocketAccepter implements Runnable {

    private int tcpPort;
    private ServerSocketChannel serverSocket = null;

    private Queue<Socket> socketQueue;

    public SocketAccepter(int tcpPort, Queue<Socket> socketQueue) {
        this.tcpPort = tcpPort;
        this.socketQueue = socketQueue;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = ServerSocketChannel.open();
            this.serverSocket.bind(new InetSocketAddress("localhost", tcpPort));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                SocketChannel socketChannel = this.serverSocket.accept();
                if (socketChannel != null) {
                    System.out.println("Socket accepted: " + socketChannel);

                    //todo check if the queue can even accept more sockets.
                    // 通过 socketChannel 构造 Socket
                    this.socketQueue.add(new Socket(socketChannel));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
