package cn.edu.nju.nioserver.core.message;

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

    public static final int KB = 1024;

    /**
     * 小、中、大缓存块大小
     */
    public static final int CAPACITY_SMALL = 4 * KB;
    public static final int CAPACITY_MEDIUM = 128 * KB;
    public static final int CAPACITY_LARGE = 1024 * KB;

    /**
     * 小、中、大缓存块的个数
     */
    private static final int SECTION_SMALL = 1024;
    private static final int SECTION_MEDIUM = 128;
    private static final int SECTION_LARGE = 16;

    /**
     * 1024 * 4KB messages =  4MB.
     */
    public byte[] smallMessageBuffer = new byte[SECTION_SMALL * CAPACITY_SMALL];
    /**
     * 128 * 128KB messages = 16MB.
     */
    public byte[] mediumMessageBuffer = new byte[SECTION_MEDIUM * CAPACITY_MEDIUM];
    /**
     * 16 * 1MB messages = 16MB.
     */
    public byte[] largeMessageBuffer = new byte[SECTION_LARGE * CAPACITY_LARGE];

    /**
     * 1024 free sections
     */
    QueueIntFlip smallMessageBufferFreeBlocks = new QueueIntFlip(SECTION_SMALL);
    /**
     * 128 free sections
     */
    QueueIntFlip mediumMessageBufferFreeBlocks = new QueueIntFlip(SECTION_MEDIUM);
    /**
     * 16 free sections
     */
    QueueIntFlip largeMessageBufferFreeBlocks = new QueueIntFlip(SECTION_LARGE);

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

    /**
     * 在共享缓存区域分配一个新的 message 块
     *
     * @return 新的 Message 对象
     */
    public Message newMessage() {
        int nextFreeSmallBlock = this.smallMessageBufferFreeBlocks.take();

        if (nextFreeSmallBlock == -1) {
            // todo 没读取到 message 应该抛出一个异常
            return null;
        }

        //todo get from Message pool - caps memory usage.
        return new Message(this, nextFreeSmallBlock, 0);
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

        System.arraycopy(message.sharedBuffer, message.offset, dest, nextFreeBlock, message.length);

        //free smaller block after copy
        srcBlockQueue.put(message.offset);

        message.sharedBuffer = dest;
        message.offset = nextFreeBlock;
        message.capacity = newCapacity;
        return true;
    }

}
