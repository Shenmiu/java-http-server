package cn.edu.nju.nioserver.core;

import cn.edu.nju.nioserver.core.message.IMessageReader;
import cn.edu.nju.nioserver.core.message.MessageWriter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author jjenkov
 * @date 16-10-2015
 */
public class Socket {

    public long socketId;

    public SocketChannel socketChannel;
    public IMessageReader messageReader = null;
    public MessageWriter messageWriter = null;

    public boolean endOfStreamReached = false;

    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    /**
     * 将数据从 Channel 读取到 buffer 中
     * @param byteBuffer 存数据的地方
     * @return 读取的数据长度
     * @throws IOException read 失败
     */
    public int read(ByteBuffer byteBuffer) throws IOException {
        int bytesRead = this.socketChannel.read(byteBuffer);
        int totalBytesRead = bytesRead;

        while (bytesRead > 0) {
            bytesRead = this.socketChannel.read(byteBuffer);
            totalBytesRead += bytesRead;
        }
        if (bytesRead == -1) {
            this.endOfStreamReached = true;
        }

        return totalBytesRead;
    }

    public int write(ByteBuffer byteBuffer) throws IOException {
        int bytesWritten = this.socketChannel.write(byteBuffer);
        int totalBytesWritten = bytesWritten;

        while (bytesWritten > 0 && byteBuffer.hasRemaining()) {
            bytesWritten = this.socketChannel.write(byteBuffer);
            totalBytesWritten += bytesWritten;
        }

        return totalBytesWritten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Socket socket = (Socket) o;

        return socketId == socket.socketId;
    }

    @Override
    public int hashCode() {
        return (int) (socketId ^ (socketId >>> 32));
    }
}
