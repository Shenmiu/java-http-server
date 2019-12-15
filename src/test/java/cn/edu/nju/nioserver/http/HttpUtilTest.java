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
    public void testParseHttpRequest() {
        String httpRequest =
                "GET /562f25980001b1b106000338.jpg HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36\r\n" +
                        "Accept:image/webp,image/*,*/*;q=0.8\r\n" +
                        "Referer:www.hostname.com\r\n" +
                        "Accept-Encoding:gzip, deflate, sdch\r\n" +
                        "Accept-Language:zh-CN,zh;q=0.8\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);

        HttpRequest request = new HttpRequest();
        HttpRequestEncoder.encode(source, 0, source.length - 1, request);
        assertEquals(request.method(), HttpMethod.GET);
        assertEquals(request.version(), HttpVersion.HTTP_1_1);
    }

    @Test
    public void testFindContentLength() throws UnsupportedEncodingException {
        @Jailbreak HttpUtil httpUtil = null;
        String contentLength = "Content-Length: 200\r\n";
//        int res = httpUtil.findContentLength(contentLength.getBytes(StandardCharsets.UTF_8), 0, contentLength.length() - 1);
//        assertEquals(res, 200);
    }
}
