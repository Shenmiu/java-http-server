package cn.edu.nju.nioserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpMethod {

    /**
     * OPTIONS
     */
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");

    /**
     * GET
     */
    public static final HttpMethod GET = new HttpMethod("GET");

    /**
     * HEAD
     */
    public static final HttpMethod HEAD = new HttpMethod("HEAD");

    /**
     * POST
     */
    public static final HttpMethod POST = new HttpMethod("POST");

    /**
     * PUT
     */
    public static final HttpMethod PUT = new HttpMethod("PUT");

    /**
     * PATCH
     */
    public static final HttpMethod PATCH = new HttpMethod("PATCH");

    /**
     * DELETE
     */
    public static final HttpMethod DELETE = new HttpMethod("DELETE");

    /**
     * TRACE
     */
    public static final HttpMethod TRACE = new HttpMethod("TRACE");

    /**
     * CONNECT
     */
    public static final HttpMethod CONNECT = new HttpMethod("CONNECT");
    /**
     * string <-> HttpMethod
     */
    private static final Map<String, HttpMethod> methodMap;

    static {
        methodMap = new HashMap<>();
        methodMap.put("OPTIONS", OPTIONS);
        methodMap.put("GET", GET);
        methodMap.put("HEAD", HEAD);
        methodMap.put("POST", POST);
        methodMap.put("PUT", PUT);
        methodMap.put("PATCH", PATCH);
        methodMap.put("DELETE", DELETE);
        methodMap.put("TRACE", TRACE);
        methodMap.put("CONNECT", CONNECT);
    }

    /**
     * 实际的名字
     */
    private final String name;

    public HttpMethod(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }

        this.name = name;
    }

    /**
     * 根据名字获取HttpMethod对象
     *
     * @param name String 名字
     * @return HttpMethod
     */
    public static HttpMethod valueOf(String name) {
        HttpMethod result = methodMap.get(name);
        // TODO djy 不在此范围内的 HttpMethod 还需要支持？我看调用的地方没处理啊
        return result != null ? result : new HttpMethod(name);
    }

    /**
     * 获取当前method的名字
     *
     * @return String
     */
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpMethod)) {
            return false;
        }

        HttpMethod that = (HttpMethod) o;
        return name().equals(that.name());
    }

}
