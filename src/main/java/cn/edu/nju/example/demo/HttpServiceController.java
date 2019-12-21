package cn.edu.nju.example.demo;

import cn.edu.nju.example.demo.service.*;
import cn.edu.nju.example.demo.service.intf.HttpService;
import cn.edu.nju.nioserver.core.ChannelHandler;
import cn.edu.nju.nioserver.core.ChannelHandlerContext;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 总控制器
 */
@Log4j2
public class HttpServiceController implements ChannelHandler {

    private static final HttpChunkService chunkService = new HttpChunkService();
    public static Map<DemoServiceName, HttpService> services;

    static {
        HttpServiceController.services = Collections.synchronizedMap(new EnumMap<>(DemoServiceName.class));
        HttpServiceController.services.put(DemoServiceName.NOT_SUPPORTED, new HttpNotSupportedService());
        HttpServiceController.services.put(DemoServiceName.INDEX, new HttpIndexService());
        HttpServiceController.services.put(DemoServiceName.METHOD, new HttpMethodService());
        HttpServiceController.services.put(DemoServiceName.STATUS, new HttpStatusService());
        HttpServiceController.services.put(DemoServiceName.MIME, new HttpMimeService());
        HttpServiceController.services.put(DemoServiceName.CONTENT_ENCODING, new HttpContentEncodingService());
        HttpServiceController.services.put(DemoServiceName.COOKIE, new HttpCookieService());
    }

    public HttpServiceController() {
    }

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpResponse response = new HttpResponse();
            response.setStatus(HttpResponseStatus.OK);
            service(ctx, request, response);
        }
    }

    private void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
        log.info("uri: {}", request.uri());
        log.info("Http Method :{}", request.method().name());

        // 分发路由
        Matcher m = DemoUtil.DEMO_URI_PATTERN.matcher(request.uri());
        if (!m.matches()) {
            services.get(DemoServiceName.NOT_SUPPORTED).service(request, response);
        }

        final DemoServiceName curServiceName = DemoServiceName.getServiceName(m.group(1));
        if (DemoServiceName.CHUNK != curServiceName) {
            services.get(curServiceName).service(request, response);
            ctx.sendDownStream(response);
        } else {
            chunkService.service(ctx, request);
        }
    }
}
