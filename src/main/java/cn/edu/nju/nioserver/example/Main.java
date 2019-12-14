package cn.edu.nju.nioserver.example;

import cn.edu.nju.nioserver.core.Server;
import cn.edu.nju.nioserver.core.message.IMessageProcessor;
import cn.edu.nju.nioserver.core.message.Message;
import cn.edu.nju.nioserver.http.HttpHeaders;
import cn.edu.nju.nioserver.http.HttpMessageReaderFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author jjenkov
 * @date 19-10-2015
 * <p>
 * http example
 */
public class Main {

    public static void main(String[] args) throws IOException {

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 38\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body>Hello World!</body></html>";

        byte[] httpResponseBytes = httpResponse.getBytes(StandardCharsets.UTF_8);

        IMessageProcessor messageProcessor = (request, writeProxy) -> {
            System.out.println("Message Received from socket: " + request.socketId);

            Message response = writeProxy.newResponse();
            response.socketId = request.socketId;
            response.writeToMessage(httpResponseBytes);

            writeProxy.enqueue(response);
        };

        Server server = new Server(9999, new HttpMessageReaderFactory(), messageProcessor);

        server.start();

    }

}
