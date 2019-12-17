package cn.edu.nju.nioserver.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 21:24
 * @description
 **/
public class Worker implements Runnable {

    private static final int DEFAULT_SELECT_TIMEOUT = 100;

    private ConcurrentLinkedQueue<SocketChannel> socketChannelQueue;

    private ChannelHandler handler;

    private volatile Selector selector;

    public Worker(ConcurrentLinkedQueue<SocketChannel> socketChannelQueue, ChannelHandler handler) {
        this.socketChannelQueue = socketChannelQueue;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // This thread is used for polling new accepted channels from queue.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    SocketChannel channel = socketChannelQueue.poll();
                    if (channel == null) {
                        continue;
                    }
                    try {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ, new ChannelBuffer());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // This thread is used for selecting.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        if (selector.selectNow() == 0) {
                            continue;
                        }
                        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            if (key.isReadable()) {
                                handler.handleRead(key);
                            }
                            if (key.isValid() && key.isWritable()) {
                                handler.handleWrite(key);
                            }
                            keyIterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
