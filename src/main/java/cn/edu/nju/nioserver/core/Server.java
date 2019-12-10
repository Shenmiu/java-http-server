package cn.edu.nju.nioserver.core;

import cn.edu.nju.nioserver.core.message.IMessageProcessor;
import cn.edu.nju.nioserver.core.message.IMessageReaderFactory;
import cn.edu.nju.nioserver.core.message.MessageBuffer;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * java nio server 的主类
 * 1. 第一个线程接受来自 ServerSocketChannel 的传入连接
 * 2. 第二个线程处理接受的连接，这意味着读取消息，处理消息并将响应写回到连接
 *
 * @author jjenkov
 * @date 18-09-2015
 */
public class Server {

    private int tcpPort;
    private IMessageReaderFactory messageReaderFactory;
    private IMessageProcessor messageProcessor;
    private Queue<Socket> socketQueue;

    /**
     * @param tcpPort              tcp 端口
     * @param messageReaderFactory 具体的 message reader 工厂
     * @param messageProcessor     具体的 message 处理器
     */
    public Server(int tcpPort, IMessageReaderFactory messageReaderFactory, IMessageProcessor messageProcessor) {
        this.tcpPort = tcpPort;
        this.messageReaderFactory = messageReaderFactory;
        this.messageProcessor = messageProcessor;
        //move 1024 to ServerConfig
        this.socketQueue = new ArrayBlockingQueue<>(1024);
    }

    public void start() throws IOException {

        SocketAccepter socketAccepter = new SocketAccepter(tcpPort, socketQueue);

        MessageBuffer readBuffer = new MessageBuffer();
        MessageBuffer writeBuffer = new MessageBuffer();

        SocketProcessor socketProcessor = new SocketProcessor(socketQueue, readBuffer, writeBuffer, this.messageReaderFactory, this.messageProcessor);

        Thread accepterThread = new Thread(socketAccepter);
        Thread processorThread = new Thread(socketProcessor);

        accepterThread.start();
        processorThread.start();
    }
}
