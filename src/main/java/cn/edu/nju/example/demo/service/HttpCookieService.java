package cn.edu.nju.example.demo.service;

import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.example.demo.service.method.util.QueryStringDecoder;
import cn.edu.nju.nioserver.http.HttpHeaderNames;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;
import cn.edu.nju.nioserver.http.cookie.Cookie;
import cn.edu.nju.nioserver.http.cookie.DefaultCookie;
import cn.edu.nju.nioserver.http.cookie.ServerCookieEncoder;
import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Cookie 的场景控制器
 * <p>
 *
 * @author Shenmiu
 * @date 2019/12/20
 */
@Log4j2
public class HttpCookieService implements HttpService {

    /**
     * 匹配会话 session 场景
     */
    private static final String COOKIE_SESSION = "session";

    /**
     * 匹配持久化 session 场景
     */
    private static final String COOKIE_PERMANENT = "permanent";

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // 分发路由
        String uri = request.uri();
        String cookieType = uri.substring(uri.lastIndexOf('/') + 1);

        log.info("cookie type: {}", cookieType);

        if (COOKIE_SESSION.equals(cookieType)) {
            ByteBuffer content = request.content().byteBuffer();
            String str = new String(content.array());
            QueryStringDecoder decoder = new QueryStringDecoder(str, false);
            Map<String, List<String>> params = decoder.parameters();
            List<String> vals = params.get("username");
            assert vals.size() == 1;

            String username = vals.get(0);
            Cookie cookie = new DefaultCookie("username", username);
            cookie.setHttpOnly(false);

            byte[] data = ("Your name is " + username).getBytes();
            response.content().setByteBuffer(ByteBuffer.wrap(data));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "" + data.length);
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("username", vals.get(0)));
        } else if (COOKIE_PERMANENT.equals(cookieType)) {
        } else {
            response.setStatus(HttpResponseStatus.NOT_FOUND);
        }

//        // 获得 request 中的 header
//        Set<Cookie> cookies;
//        String value = request.headers().get(HttpHeaderNames.COOKIE);
//        if (value == null) {
//            cookies = Collections.emptySet();
//        } else {
//            cookies = ServerCookieDecoder.STRICT.decode(value);
//        }
//        if (!cookies.isEmpty()) {
//            // Reset the cookies if necessary.
//            for (Cookie cookie : cookies) {
//                response.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
//            }
//        }
    }
}
