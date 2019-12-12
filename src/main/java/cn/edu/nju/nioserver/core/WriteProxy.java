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

    /**
     * 获取一个空的响应对象
     *
     * @return Message 对象
     */
    public Message newResponse() {
        return this.messageBuffer.newMessage();
    }

    /**
     * 将响应放入 outbound 队列中
     *
     * @param response 响应
     * @return 加入队列是否成功
     */
    public boolean enqueue(Message response) {
        return this.writeQueue.offer(response);
    }

}
