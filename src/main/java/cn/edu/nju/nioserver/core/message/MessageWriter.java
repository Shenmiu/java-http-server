package cn.edu.nju.nioserver.core.message;

import cn.edu.nju.nioserver.core.Socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责向 SocketChannel 中写消息
 *
 * @author jjenkov
 * @date 21-10-2015
 */
public class MessageWriter {

    /**
     * 保存该 SocketChannel 应该要传送的消息
     */
    private List<Message> writeQueue = new ArrayList<>();
    /**
     * 保存当前正在传送的消息
     */
    private Message messageInProgress = null;
    /**
     * 记录当前已经写了多少个字节了
     */
    private int bytesWritten = 0;

    public MessageWriter() {
    }

    public void enqueue(Message message) {
        if (this.messageInProgress == null) {
            this.messageInProgress = message;
        } else {
            this.writeQueue.add(message);
        }
    }

    public void write(Socket socket, ByteBuffer byteBuffer) throws IOException {
        byteBuffer.put(this.messageInProgress.sharedBuffer, this.messageInProgress.offset + this.bytesWritten, this.messageInProgress.length - this.bytesWritten);
        byteBuffer.flip();

        this.bytesWritten += socket.write(byteBuffer);
        byteBuffer.clear();

        if (bytesWritten >= this.messageInProgress.length) {
            if (this.writeQueue.size() > 0) {
                this.messageInProgress = this.writeQueue.remove(0);
            } else {
                this.messageInProgress = null;
            }
        }
    }

    public boolean isEmpty() {
        return this.writeQueue.isEmpty() && this.messageInProgress == null;
    }

}
