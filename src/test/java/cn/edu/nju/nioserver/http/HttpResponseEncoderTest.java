package cn.edu.nju.nioserver.http;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class HttpResponseEncoderTest {

    @Test
    public void test1() {
        HttpResponseEncoder encoder = new HttpResponseEncoder();

        HttpResponseStatus status = HttpResponseStatus.OK;
        HttpVersion version = HttpVersion.HTTP_1_1;

        String responseContent = "<html><body>Hello World!</body></html><br/>";
        byte[] bytes = responseContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        HttpContent content = new HttpContent(buffer);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaderNames.CONTENT_LENGTH, "38");
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html");

        HttpResponse response = new HttpResponse();
        response.setStatus(status);
        response.setContent(content);
        response.setHeaders(headers);
        response.setVersion(version);

        List<Byte> result = new ArrayList<>();
        encoder.encode(response, result);

        assertNotEquals(result.size(), 0);
        byte[] resultInfo = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultInfo[i] = result.get(i);
        }
        String info = new String(resultInfo, StandardCharsets.UTF_8);
        assertNotEquals(info, null);

    }

    @Test
    public void test2() {
        HttpResponseEncoder encoder = new HttpResponseEncoder();

        HttpResponseStatus status = HttpResponseStatus.OK;
        HttpVersion version = HttpVersion.HTTP_1_1;

        String responseContent = "<html><body>Hello World!</body></html><br/>";
        byte[] bytes = responseContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        HttpContent content = new HttpContent(buffer);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaderNames.TRANSFER_ENCODING, "chunked");
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html");

        HttpResponse response = new HttpResponse();
        response.setStatus(status);
        //response.setContent(content);
        response.setHeaders(headers);
        response.setVersion(version);

        List<Byte> result = new ArrayList<>();
        encoder.encode(response, result);

        assertNotEquals(result.size(), 0);
        byte[] resultInfo = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultInfo[i] = result.get(i);
        }
        String info = new String(resultInfo, StandardCharsets.UTF_8);

        encoder.encode(content, result);

        byte[] resultInfo1 = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultInfo1[i] = result.get(i);
        }
        info = new String(resultInfo1, StandardCharsets.UTF_8);
        assertNotEquals(info, null);

    }

}
