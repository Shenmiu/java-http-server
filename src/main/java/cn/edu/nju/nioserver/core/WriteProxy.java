package cn.edu.nju.nioserver.core;

import cn.edu.nju.nioserver.core.message.Message;
import cn.edu.nju.nioserver.core.message.MessageBuffer;

import java.util.Queue;

/**
 * @author jjenkov
 * @date 22-10-2015
 */
public class WriteProxy {

    private MessageBuffer messageBuffer;
    private Queue<Message> writeQueue;

    public WriteProxy(MessageBuffer messageBuffer, Queue<Message> writeQueue) {
        this.messageBuffer = messageBuffer;
        this.writeQueue = writeQueue;
    }

    public Message getMessage() {
        return this.messageBuffer.getMessage();
    }

    public boolean enqueue(Message message) {
        return this.writeQueue.offer(message);
    }

}
