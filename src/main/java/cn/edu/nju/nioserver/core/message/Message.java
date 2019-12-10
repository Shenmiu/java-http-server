package cn.edu.nju.nioserver.core.message;

import java.nio.ByteBuffer;

/**
 * @author jjenkov
 * @date 16-10-2015
 */
public class Message {

    /**
     * the id of source socket or destination socket, depending on whether is going in or out.
     */
    public long socketId = 0;
    public byte[] sharedArray = null;
    /**
     * offset into sharedArray where this message data starts.
     */
    public int offset = 0;
    /**
     * the size of the section in the sharedArray allocated to this message.
     */
    public int capacity = 0;
    /**
     * the number of bytes used of the allocated section.
     */
    public int length = 0;
    public Object metaData = null;
    private MessageBuffer messageBuffer;

    public Message(MessageBuffer messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    /**
     * 将来自 ByteBuffer 的数据写入 Message 的缓冲区（此时可能还不是一个完整的消息）
     *
     * @param byteBuffer The ByteBuffer containing the message data to write.
     * @return 写入 Message 的数据长度
     */
    public int writeToMessage(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();

        while (this.length + remaining > capacity) {
            if (!this.messageBuffer.expandMessage(this)) {
                return -1;
            }
        }

        int bytesToCopy = Math.min(remaining, this.capacity - this.length);
        byteBuffer.get(this.sharedArray, this.offset + this.length, bytesToCopy);
        this.length += bytesToCopy;

        return bytesToCopy;
    }


    /**
     * Writes data from the byte array into this message - meaning into the buffer backing this message.
     *
     * @param byteArray The byte array containing the message data to write.
     * @return 写入 Message 的数据长度
     */
    public int writeToMessage(byte[] byteArray) {
        return writeToMessage(byteArray, 0, byteArray.length);
    }


    /**
     * Writes data from the byte array into this message - meaning into the buffer backing this message.
     *
     * @param byteArray The byte array containing the message data to write.
     * @return 写入 Message 的数据长度
     */
    public int writeToMessage(byte[] byteArray, int offset, int length) {
        int remaining = length;

        while (this.length + remaining > capacity) {
            if (!this.messageBuffer.expandMessage(this)) {
                return -1;
            }
        }

        int bytesToCopy = Math.min(remaining, this.capacity - this.length);
        System.arraycopy(byteArray, offset, this.sharedArray, this.offset + this.length, bytesToCopy);
        this.length += bytesToCopy;
        return bytesToCopy;
    }


    /**
     * In case the buffer backing the nextMessage contains more than one HTTP message, move all data after the first
     * message to a new Message object.
     *
     * @param message  The message containing the partial message (after the first message).
     * @param endIndex The end index of the first message in the buffer of the message given as parameter.
     */
    public void writePartialMessageToMessage(Message message, int endIndex) {
        int startIndexOfPartialMessage = message.offset + endIndex;
        int lengthOfPartialMessage = (message.offset + message.length) - endIndex;

        System.arraycopy(message.sharedArray, startIndexOfPartialMessage, this.sharedArray, this.offset, lengthOfPartialMessage);
    }

    public int writeToByteBuffer(ByteBuffer byteBuffer) {
        return 0;
    }

}
