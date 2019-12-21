package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.example.demo.service.method.util.FileUtil;
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

/**
 * DemoMethodService Tester.
 *
 * @author cuihua
 * @version 1.0
 * @since <pre>12月 17, 2019</pre>
 */
public class HttpMethodServiceTest {

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void testServicePost1() {
        String httpRequest =
                "DELETE /method/post_file.txt HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:40\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley" +
                        "POST /method/post_file.txt HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:40\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=Professional%20Ajax&publisher=Wiley" +
                        "POST /method/post_file.txt HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:text/plain\r\n" +
                        "Content-Length:20\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "name=hello&kitty=biu";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        List<Byte> buffer = new ArrayList<>();
        for (byte e : source) {
            buffer.add(e);
        }
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        encoder.decode(buffer, requestList);

        HttpService service = new HttpMethodService();

        // delete an exist file
        HttpRequest request0 = requestList.get(0);
        HttpResponse response0 = new HttpResponse();
        service.service(request0, response0);
        assertEquals(response0.status(), HttpResponseStatus.NO_CONTENT);

        // delete a not exist file
        HttpResponse response01 = new HttpResponse();
        service.service(request0, response01);
        assertEquals(response01.status(), HttpResponseStatus.NOT_FOUND);

        HttpRequest request1 = requestList.get(1);
        HttpResponse response1 = new HttpResponse();
        service.service(request1, response1);
        String result1 = new String(response1.content().byteBuffer().array(), StandardCharsets.UTF_8);
        assertEquals(response1.status(), HttpResponseStatus.CREATED);
        assertEquals(result1, "You have send a post request with content type = application/x-www-form-urlencoded.\n" +
                "The data is: \n" +
                "name: Professional Ajax\n" +
                "publisher: Wiley\n");

        HttpRequest request2 = requestList.get(2);
        HttpResponse response2 = new HttpResponse();
        service.service(request2, response2);
        String result2 = new String(response2.content().byteBuffer().array(), StandardCharsets.UTF_8);
        assertEquals(response2.status(), HttpResponseStatus.OK);
        assertEquals(result2, "You have send a post request with content type = application/x-www-form-urlencoded.\n" +
                "The data is: \n" +
                "name: Professional Ajax\n" +
                "publisher: Wiley\n" +
                "You have send a post request with content type = text/plain.\n" +
                "The plain text is: name=hello&kitty=biu");
    }

    @Test
    public void testServiceGet1() {
        String httpRequest =
                "GET /method/hello HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
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
        HttpService service = new HttpMethodService();
        HttpResponse response = new HttpResponse();
        service.service(request, response);
        String result = new String(response.content().byteBuffer().array(), StandardCharsets.UTF_8);
        assertEquals(result, "You have send a get request.The requested url params are: \n" +
                "Requested file hello does not exist.");
    }

    @Test
    public void testServiceGet2() {
        String httpRequest =
                "GET /method/get_file.txt?a=1&b=2&a=3 HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
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
        HttpService service = new HttpMethodService();
        HttpResponse response = new HttpResponse();
        service.service(request, response);
        String result = new String(response.content().byteBuffer().array(), StandardCharsets.UTF_8);
        assertEquals(result, "You have send a get request." +
                "The requested url params are: \n" +
                "a: 1\n" +
                "a: 3\n" +
                "b: 2\n" +
                "Requested file get_file.txt's content is: \n" +
                "HELLO get_file's content!!!");
    }

    @Test
    public void testServicePut() {
        String httpRequest =
                "PUT /method/put_file.txt HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:28\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "HELLO put_file.txt, content1" +
                        "PUT /method/put_file.txt HTTP/1.1\r\n" +
                        "Host:www.hostname.com\r\n" +
                        "User-Agent:Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022)\r\n" +
                        "Content-Type:application/x-www-form-urlencoded\r\n" +
                        "Content-Length:28\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n" +
                        "HELLO put_file.txt, content3";

        byte[] source = httpRequest.getBytes(StandardCharsets.UTF_8);
        List<Byte> buffer = new ArrayList<>();
        for (byte e : source) {
            buffer.add(e);
        }
        List<HttpRequest> requestList = new ArrayList<>();
        HttpRequestDecoder encoder = new HttpRequestDecoder();
        encoder.decode(buffer, requestList);

        HttpService service = new HttpMethodService();

        HttpRequest request1 = requestList.get(0);
        HttpResponse response1 = new HttpResponse();
        service.service(request1, response1);
        String result1 = FileUtil.read("put_file.txt");
        assertEquals(result1, "HELLO put_file.txt, content1");

        HttpRequest request2 = requestList.get(1);
        HttpResponse response2 = new HttpResponse();
        service.service(request2, response2);
        String result2 = FileUtil.read("put_file.txt");
        assertEquals(result2, "HELLO put_file.txt, content3");
    }

} 
