package cn.edu.nju.nioserver.http;

import cn.edu.nju.nioserver.core.Socket;
import cn.edu.nju.nioserver.core.message.IMessageReader;
import cn.edu.nju.nioserver.core.message.Message;
import cn.edu.nju.nioserver.core.message.MessageBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jjenkov
 * @date 18-10-2015
 */
public class HttpMessageReader implements IMessageReader {

    /**
     * 共享缓存
     */
    private MessageBuffer messageBuffer;

    private List<Message> completeMessages = new ArrayList<>();
    private Message nextMessage;

    public HttpMessageReader(MessageBuffer readMessageBuffer) {
        this.messageBuffer = readMessageBuffer;
        this.nextMessage = messageBuffer.newMessage();
        this.nextMessage.metaData = new HttpHeaders();
    }

    @Override
    public void read(Socket socket, ByteBuffer byteBuffer) throws IOException {
        int bytesRead = socket.read(byteBuffer);
        byteBuffer.flip();

        if (byteBuffer.remaining() == 0) {
            byteBuffer.clear();
            return;
        }

        this.nextMessage.writeToMessage(byteBuffer);

        int endIndex = HttpUtil.parseHttpRequest(this.nextMessage.sharedBuffer, this.nextMessage.offset, this.nextMessage.offset + this.nextMessage.length, (HttpHeaders) this.nextMessage.metaData);
        if (endIndex != -1) {
            Message message = this.messageBuffer.newMessage();
            message.metaData = new HttpHeaders();

            message.writePartialMessageToMessage(nextMessage, endIndex);

            completeMessages.add(nextMessage);
            nextMessage = message;
        }
        byteBuffer.clear();
    }


    @Override
    public List<Message> getMessages() {
        return this.completeMessages;
    }

}
