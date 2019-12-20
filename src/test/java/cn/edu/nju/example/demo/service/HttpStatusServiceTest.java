package cn.edu.nju.example.demo.service;


import cn.edu.nju.example.HttpService;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpRequestDecoder;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class HttpStatusServiceTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }


    @Test
    public void service() {

        String httpRequest =
                "POST localhost:8080/status/200 HTTP/1.1\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:40\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        List<Byte> buffer = new ArrayList<>();
        for (byte e : source) {
            buffer.add(e);
        }
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        encoder.decode(buffer, requestList);

        HttpRequest request = requestList.get(0);
        HttpService service = new HttpStatusService();
        HttpResponse response = new HttpResponse();
        service.service(request, response);
        assertEquals(response.status(), HttpResponseStatus.OK);
    }
}
