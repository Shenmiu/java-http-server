package cn.edu.nju.example.demo;

import cn.edu.nju.example.demo.service.*;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpService;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 总控制器
 */
public class HttpServiceController implements HttpService {

    public static final HttpServiceController controller = new HttpServiceController();
    private static final Pattern DEMO_URI_PATTERN = Pattern.compile("^/([a-z]+)/?(.*)$");
    private static Map<DemoServiceName, HttpService> services;

    static {
        services = Collections.synchronizedMap(new EnumMap<>(DemoServiceName.class));
        services.put(DemoServiceName.NOT_SUPPORTED, new HttpNotSupportedService());
        services.put(DemoServiceName.INDEX, new HttpIndexService());
        services.put(DemoServiceName.METHOD, new HttpMethodService());
        services.put(DemoServiceName.STATUS, new HttpStatusService());
        services.put(DemoServiceName.MIME, new HttpMimeService());
    }

    private HttpServiceController() {
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        // 分发路由
        Matcher m = DEMO_URI_PATTERN.matcher(request.uri());
        if (!m.matches()) {
            services.get(DemoServiceName.NOT_SUPPORTED).service(request, response);
        }

        final DemoServiceName curServiceName = DemoServiceName.getServiceName(m.group(1));
        services.get(curServiceName).service(request, response);
    }
}
