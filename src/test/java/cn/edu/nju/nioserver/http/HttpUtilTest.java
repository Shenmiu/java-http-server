package cn.edu.nju.nioserver.http;

import manifold.ext.api.Jailbreak;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class HttpUtilTest {

    @Jailbreak HttpUtil httpUtil = new HttpUtil();

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
        HttpRequestEncoder encoder = new HttpRequestEncoder();
        encoder.encode(byteBuffer, requestList);
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
        HttpRequestEncoder encoder = new HttpRequestEncoder();
        int result = encoder.encode(byteBuffer, requestList);
        assertEquals(requestList.size(), 0);
    }

    @Test
    public void testFindContentLength() throws UnsupportedEncodingException {
        @Jailbreak HttpUtil httpUtil = null;
        String contentLength = "Content-Length: 200\r\n";
//        int res = httpUtil.findContentLength(contentLength.getBytes(StandardCharsets.UTF_8), 0, contentLength.length() - 1);
//        assertEquals(res, 200);
    }
}
