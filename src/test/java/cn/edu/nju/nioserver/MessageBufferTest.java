package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.core.message.Message;
import cn.edu.nju.nioserver.core.message.MessageBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jjenkov on 18-10-2015.
 */
public class MessageBufferTest {

    @Test
    public void testGetMessage() {

        MessageBuffer messageBuffer = new MessageBuffer();

        Message message = messageBuffer.newMessage();

        assertNotNull(message);
        assertEquals(0, message.offset);
        assertEquals(0, message.length);
        assertEquals(4 * 1024, message.capacity);

        Message message2 = messageBuffer.newMessage();

        assertNotNull(message2);
        assertEquals(4096, message2.offset);
        assertEquals(0, message2.length);
        assertEquals(4 * 1024, message2.capacity);

        //todo test what happens if the small buffer space is depleted of messages.

    }


    @Test
    public void testExpandMessage() {
        MessageBuffer messageBuffer = new MessageBuffer();

        Message message = messageBuffer.newMessage();

        byte[] smallSharedArray = message.sharedBuffer;

        assertNotNull(message);
        assertEquals(0, message.offset);
        assertEquals(0, message.length);
        assertEquals(4 * 1024, message.capacity);

        messageBuffer.expandMessage(message);
        assertEquals(0, message.offset);
        assertEquals(0, message.length);
        assertEquals(128 * 1024, message.capacity);

        byte[] mediumSharedArray = message.sharedBuffer;
        assertNotSame(smallSharedArray, mediumSharedArray);

        messageBuffer.expandMessage(message);
        assertEquals(0, message.offset);
        assertEquals(0, message.length);
        assertEquals(1024 * 1024, message.capacity);

        byte[] largeSharedArray = message.sharedBuffer;
        assertNotSame(smallSharedArray, largeSharedArray);
        assertNotSame(mediumSharedArray, largeSharedArray);

        //next expansion should not be possible.
        assertFalse(messageBuffer.expandMessage(message));
        assertEquals(0, message.offset);
        assertEquals(0, message.length);
        assertEquals(1024 * 1024, message.capacity);
        assertSame(message.sharedBuffer, largeSharedArray);


    }
}
