package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.http.HttpContent;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class HttpCookieServiceTest {

    private HttpCookieService cookieService = new HttpCookieService();

    @Test
    public void service() {
        HttpRequest request = new HttpRequest();
        request.setUri("/cookie/session");
        request.setContent(new HttpContent(ByteBuffer.wrap("username=test".getBytes())));
        HttpResponse response = new HttpResponse();
        cookieService.service(request, response);
        assertEquals("username=test", response.headers().get(HttpHeaderNames.SET_COOKIE));
    }
}