package cn.edu.nju.nioserver.core.message;

import cn.edu.nju.nioserver.core.Socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author jjenkov
 * @date 16-10-2015
 */
public interface IMessageReader {

    void read(Socket socket, ByteBuffer byteBuffer) throws IOException;

    List<Message> getMessages();

}
