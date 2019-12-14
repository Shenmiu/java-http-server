package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.message.Message;
import cn.edu.nju.nioserver.core.message.MessageBuffer;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


/**
 * Created by jjenkov on 18-10-2015.
 */
public class MessageTest {


    @Test
    public void testWriteToMessage() {
        MessageBuffer messageBuffer = new MessageBuffer();

        Message message = messageBuffer.newMessage();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);

        fill(byteBuffer, 4096);

        int written = message.writeToMessage(byteBuffer);
        assertEquals(4096, written);
        assertEquals(4096, message.length);
        assertSame(messageBuffer.smallMessageBuffer, message.sharedBuffer);

        fill(byteBuffer, 124 * 1024);
        written = message.writeToMessage(byteBuffer);
        assertEquals(124 * 1024, written);
        assertEquals(128 * 1024, message.length);
        assertSame(messageBuffer.mediumMessageBuffer, message.sharedBuffer);

        fill(byteBuffer, (1024 - 128) * 1024);
        written = message.writeToMessage(byteBuffer);
        assertEquals(896 * 1024, written);
        assertEquals(1024 * 1024, message.length);
        assertSame(messageBuffer.largeMessageBuffer, message.sharedBuffer);

        fill(byteBuffer, 1);
        written = message.writeToMessage(byteBuffer);
        assertEquals(-1, written);

    }

    private void fill(ByteBuffer byteBuffer, int length) {
        byteBuffer.clear();
        for (int i = 0; i < length; i++) {
            byteBuffer.put((byte) (i % 128));
        }
        byteBuffer.flip();
    }
}