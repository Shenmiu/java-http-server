package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.nioserver.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

/**
 * 内容编码场景
 * <p>
 *
 * @author Shenmiu
 * @date 2019/12/20
 */
public class HttpContentEncodingService implements HttpService {

    static byte[] compress(final String str) throws IOException {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(str.getBytes(StandardCharsets.UTF_8));
        gzip.flush();
        gzip.close();
        return obj.toByteArray();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod curMethod = request.method();
        assert curMethod == HttpMethod.GET;
        String raw = "This is raw content.";
        byte[] compressed = null;
        try {
            compressed = compress(raw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert compressed != null;
        response.headers().set(HttpHeaderNames.CONTENT_ENCODING, HttpHeaderValues.GZIP);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "" + compressed.length);
        response.content().setByteBuffer(ByteBuffer.wrap(compressed));
    }
}
