package cn.edu.nju.nioserver.http;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class HttpRequestDecoderTest {

    @Test
    public void testParseHttpRequest() {
        String httpRequest =
                "POST / HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:40\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley" +
                        "POST / HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:40\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley";


        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(source.length);
        byteBuffer.put(source);
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        encoder.decode(0, byteBuffer, requestList);
        assertEquals(requestList.size(), 2);
    }

    @Test
    public void testParseHttpRequest1() {
        String httpRequest =
                "POST / HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(source.length);
        byteBuffer.put(source);
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        int result = encoder.decode(0, byteBuffer, requestList);
        assertEquals(requestList.size(), 0);
    }

    @Test
    public void testParseHttpRequestChunk() {
        String httpRequest =
                "POST / HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Transfer-Encoding:chunked\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "8\r\n" +
                        "area=2 a\r\n" +
                        "5\r\n" +
                        "nd ex\r\n" +
                        "4\r\n" +
                        "ists\r\n" +
                        "5\r\n" +
                        "(sele\r\n" +
                        "5\r\n" +
                        "ct 1)\r\n" +
                        "0\r\n" +
                        "\r\n";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(source.length);
        byteBuffer.put(source);
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        encoder.decode(0, byteBuffer, requestList);
        assertEquals(requestList.size(), 1);
    }

    @Test
    public void testParseHttpRequestChunk1() {
        String httpRequest =
                "POST / HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Transfer-Encoding:chunked\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "8\r\n";
        String anotherChunk = "area=2 a\r\n" +
                "5\r\n" +
                "nd ex\r\n" +
                "4\r\n" +
                "ists\r\n" +
                "5\r\n" +
                "(sele\r\n" +
                "5\r\n" +
                "ct 1)\r\n" +
                "0\r\n" +
                "\r\n";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        byte[] another = anotherChunk.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.allocate(source.length + another.length);
        byteBuffer.put(source);
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();

        encoder.decode(0, byteBuffer, requestList);

        byteBuffer.put(another);
        encoder.decode(source.length, byteBuffer, requestList);

        assertEquals(requestList.size(), 1);
    }
}
