package cn.edu.nju.nioserver.core.message;

import cn.edu.nju.nioserver.core.QueueIntFlip;

/**
 * A shared buffer which can contain many messages inside. A message gets a section of the buffer to use. If the
 * message outgrows the section in size, the message requests a larger section and the message is copied to that
 * larger section. The smaller section is then freed again.
 * <p>
 * <p>
 *
 * @author jjenkov
 * @date 18-10-2015
 */
public class MessageBuffer {

    public final static int KB = 1024;
    public final static int MB = 1024 * KB;

    private static final int CAPACITY_SMALL = 4 * KB;
    private static final int CAPACITY_MEDIUM = 128 * KB;
    private static final int CAPACITY_LARGE = 1024 * KB;

    //package scope (default) - so they can be accessed from unit tests.
    /**
     * 1024 x   4KB messages =  4MB.
     */
    public byte[] smallMessageBuffer = new byte[1024 * 4 * KB];
    /**
     * 128 x 128KB messages = 16MB.
     */
    public byte[] mediumMessageBuffer = new byte[128 * 128 * KB];
    /**
     * 16 * 1MB messages = 16MB.
     */
    public byte[] largeMessageBuffer = new byte[16 * MB];

    /**
     * 1024 free sections
     */
    QueueIntFlip smallMessageBufferFreeBlocks = new QueueIntFlip(1024);
    /**
     * 128 free sections
     */
    QueueIntFlip mediumMessageBufferFreeBlocks = new QueueIntFlip(128);
    /**
     * 16 free sections
     */
    QueueIntFlip largeMessageBufferFreeBlocks = new QueueIntFlip(16);

    //todo make all message buffer capacities and block sizes configurable
    //todo calculate free block queue sizes based on capacity and block size of buffers.

    public MessageBuffer() {
        //add all free sections to all free section queues.
        for (int i = 0; i < smallMessageBuffer.length; i += CAPACITY_SMALL) {
            this.smallMessageBufferFreeBlocks.put(i);
        }
        for (int i = 0; i < mediumMessageBuffer.length; i += CAPACITY_MEDIUM) {
            this.mediumMessageBufferFreeBlocks.put(i);
        }
        for (int i = 0; i < largeMessageBuffer.length; i += CAPACITY_LARGE) {
            this.largeMessageBufferFreeBlocks.put(i);
        }
    }

    public Message getMessage() {
        int nextFreeSmallBlock = this.smallMessageBufferFreeBlocks.take();

        if (nextFreeSmallBlock == -1) {
            return null;
        }

        //todo get from Message pool - caps memory usage.
        Message message = new Message(this);

        message.sharedArray = this.smallMessageBuffer;
        message.capacity = CAPACITY_SMALL;
        message.offset = nextFreeSmallBlock;
        message.length = 0;

        return message;
    }

    public boolean expandMessage(Message message) {
        if (message.capacity == CAPACITY_SMALL) {
            return moveMessage(message, this.smallMessageBufferFreeBlocks, this.mediumMessageBufferFreeBlocks, this.mediumMessageBuffer, CAPACITY_MEDIUM);
        } else if (message.capacity == CAPACITY_MEDIUM) {
            return moveMessage(message, this.mediumMessageBufferFreeBlocks, this.largeMessageBufferFreeBlocks, this.largeMessageBuffer, CAPACITY_LARGE);
        } else {
            return false;
        }
    }

    private boolean moveMessage(Message message, QueueIntFlip srcBlockQueue, QueueIntFlip destBlockQueue, byte[] dest, int newCapacity) {
        int nextFreeBlock = destBlockQueue.take();
        if (nextFreeBlock == -1) {
            return false;
        }

        System.arraycopy(message.sharedArray, message.offset, dest, nextFreeBlock, message.length);

        //free smaller block after copy
        srcBlockQueue.put(message.offset);

        message.sharedArray = dest;
        message.offset = nextFreeBlock;
        message.capacity = newCapacity;
        return true;
    }


}
