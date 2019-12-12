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

    /**
     * 通过 socketChannel 构造 Socket
     *
     * @param socketChannel 新接受的链接
     */
    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    /**
     * 将数据从 SocketChannel 读取到 ByteBuffer 中
     *
     * @param byteBuffer 存数据的地方
     * @return 读取的数据总字节数
     * @throws IOException 读取失败
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

    /**
     * 将数据从 ByteBuffer 写入到 SocketChannel 中
     *
     * @param byteBuffer 保存数据的 ByteBuffer
     * @return 写入的数据总字节数
     * @throws IOException 写入失败
     */
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
