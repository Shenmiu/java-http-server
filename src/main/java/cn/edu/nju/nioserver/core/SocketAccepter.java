package cn.edu.nju.nioserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;
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

    private Queue<Socket> socketQueue;

    public SocketAccepter(int tcpPort, Queue<Socket> socketQueue) {
        this.tcpPort = tcpPort;
        this.socketQueue = socketQueue;
    }

    @Override
    public void run() {
        ServerSocketChannel serverSocket;
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(tcpPort));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                SocketChannel socketChannel = serverSocket.accept();
                if (socketChannel != null) {
                    System.out.println("Socket accepted: " + socketChannel);
                    this.socketQueue.add(new Socket(socketChannel));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
