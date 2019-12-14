package cn.edu.nju.nioserver.http;

import manifold.ext.api.Jailbreak;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class HttpUtilTest {

    @Jailbreak HttpUtil httpUtil = new HttpUtil();

    @Test
    public void testResolveHttpMethod() throws UnsupportedEncodingException {
//        assertHttpMethod("GET / HTTP/1.1\r\n", HttpHeaders.HTTP_METHOD_GET);
//        assertHttpMethod("POST / HTTP/1.1\r\n", HttpHeaders.HTTP_METHOD_POST);
//        assertHttpMethod("PUT / HTTP/1.1\r\n", HttpHeaders.HTTP_METHOD_PUT);
//        assertHttpMethod("HEAD / HTTP/1.1\r\n", HttpHeaders.HTTP_METHOD_HEAD);
//        assertHttpMethod("DELETE / HTTP/1.1\r\n", HttpHeaders.HTTP_METHOD_DELETE);
    }

    private void assertHttpMethod(String httpRequest, int httpMethod) {
        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpUtil.resolveHttpMethod(source, 0, httpHeaders);
//        assertEquals(httpMethod, httpHeaders.httpMethod);
    }


    @Test
    public void testParseHttpRequest() {
        String httpRequest =
                "GET / HTTP/1.1\r\n\r\n";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        HttpHeaders httpHeaders = new HttpHeaders();

//        HttpUtil.parseHttpRequest(source, 0, source.length, httpHeaders);

//        assertEquals(0, httpHeaders.contentLength);

        httpRequest =
                "GET / HTTP/1.1\r\n" +
                        "Content-Length: 38\r\n" +
                        "\r\n1234";
        source = httpRequest.getBytes(StandardCharsets.UTF_8);

//        assertEquals(-1, HttpUtil.parseHttpRequest(source, 0, source.length, httpHeaders));
//        assertEquals(38, httpHeaders.contentLength);



        httpRequest =
                "GET / HTTP/1.1\r\n" +
                        "Content-Length: 5\r\n" +
                        "\r\n12345";
        source = httpRequest.getBytes(StandardCharsets.UTF_8);

//        assertEquals(42, HttpUtil.parseHttpRequest(source, 0, source.length, httpHeaders));
//        assertEquals(5, httpHeaders.contentLength);


        httpRequest =
                "GET / HTTP/1.1\r\n" +
                        "Content-Length: 5\r\n" +
                        "\r\n12345" +
                        "GET / HTTP/1.1\r\n" +
                        "Content-Length: 5\r\n" +
                        "\r\n12345";

        source = httpRequest.getBytes(StandardCharsets.UTF_8);

//        assertEquals(42, HttpUtil.parseHttpRequest(source, 0, source.length, httpHeaders));
//        assertEquals(5, httpHeaders.contentLength);
//        assertEquals(37, httpHeaders.bodyStartIndex);
//        assertEquals(42, httpHeaders.bodyEndIndex);
    }

    @Test
    public void testFindContentLength() throws UnsupportedEncodingException {
        @Jailbreak HttpUtil httpUtil = null;
        String contentLength = "Content-Length: 200\r\n";
        int res = httpUtil.findContentLength(contentLength.getBytes(StandardCharsets.UTF_8), 0, contentLength.length() - 1);
        assertEquals(res, 200);
    }


}
