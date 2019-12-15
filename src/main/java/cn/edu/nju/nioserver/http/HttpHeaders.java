package cn.edu.nju.nioserver.http;


import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {

    /**
     * 指定request的header内容
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 设置或者添加header
     *
     * @param name  HttpHeaderNames
     * @param value 对应内容
     */
    public void set(String name, String value) {
        headers.put(name, value);
    }

    /**
     * 根据名字获取header内容
     *
     * @param name HttpHeaderNames
     * @return String
     */
    public String get(String name) {
        return headers.get(name);
    }

    /**
     * 根据名字删除header内容（包括键）
     *
     * @param name HttpHeaderNames
     */
    public void remove(String name) {
        headers.remove(name);
    }

    /**
     * 清空所有header
     */
    public void clear() {
        headers.clear();
    }

    /**
     * 根据指定键值对查找是否存在
     *
     * @param name       HttpHeaderNames
     * @param value      对应内容
     * @param ignoreCase 是否忽略大小写
     * @return boolean
     */
    public boolean containsValue(String name, String value, boolean ignoreCase) {
        if (ignoreCase) {
            for (String key : headers.keySet()) {
                if (name.equalsIgnoreCase(key) && value.equalsIgnoreCase(headers.get(key))) {
                    return true;
                }
            }
        } else {
            for (String key : headers.keySet()) {
                if (name.equals(key) && value.equals(headers.get(key))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断是否存在ContentLength
     *
     * @return boolean
     */
    public boolean containsContentLength() {
        return headers.containsKey(HttpHeaderNames.CONTENT_LENGTH);
    }

    /**
     * 查询是否保持长连接
     *
     * @param request HttpRequest
     * @return boolean
     */
    public static boolean isKeepAlive(HttpRequest request) {
        return !request.headers().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true) &&
                (request.version().isKeepAliveDefault() ||
                        request.headers().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true));
    }

    /**
     * 设置是否保持长连接
     *
     * @param request   HttpRequest
     * @param keepAlive 是否保持长连接
     */
    public static void setKeepAlive(HttpRequest request, boolean keepAlive) {
        HttpHeaders httpHeaders = request.headers();
        if (request.version().isKeepAliveDefault()) {
            if (keepAlive) {
                httpHeaders.remove(HttpHeaderNames.CONNECTION);
            } else {
                httpHeaders.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }
        } else {
            if (keepAlive) {
                httpHeaders.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            } else {
                httpHeaders.remove(HttpHeaderNames.CONNECTION);
            }
        }
    }

    /**
     * 获取指定request中指定name的header
     *
     * @param request HttpRequest
     * @param name    HttpHeaderNames
     * @return header
     */
    public static String getHeader(HttpRequest request, String name) {
        return request.headers().get(name);
    }

    /**
     * 设置指定request中指定name的header内容
     *
     * @param request HttpRequest
     * @param name    HttpHeaderNames
     * @param value   内容
     */
    public static void setHeader(HttpRequest request, String name, String value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name is not exist");
        }

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value is not exist");
        }
        request.headers().set(name.trim(), value.trim());
    }

    /**
     * 添加指定request中指定name的header
     *
     * @param request HttpRequest
     * @param name    HttpHeaderNames
     * @param value   内容
     */
    public static void addHeader(HttpRequest request, String name, String value) {
        HttpHeaders.setHeader(request, name, value);
    }

    /**
     * 删除指定request中指定name的header
     *
     * @param request HttpRequest
     * @param name    HttpHeaderNames
     */
    public static void removeHeader(HttpRequest request, String name) {
        request.headers().remove(name);
    }

    /**
     * 清空指定request所有header
     *
     * @param request HttpRequest
     */
    public static void clearHeaders(HttpRequest request) {
        request.headers().clear();
    }

    /**
     * 判断是否存在ContentLength
     *
     * @param request HttpRequest
     * @return 是否存在ContentLength
     */
    public static boolean hasContentLength(HttpRequest request) {
        return request.headers().containsContentLength();
    }

    /**
     * 获取ContentLength
     *
     * @param request HttpRequest
     * @return 获取ContentLength
     */
    public static int getContentLength(HttpRequest request) {
        String length = request.headers().get(HttpHeaderNames.CONTENT_LENGTH);
        if (length == null) {
            throw new IllegalArgumentException(HttpHeaderNames.CONTENT_LENGTH + " is not exist");
        }
        return Integer.parseInt(length);
    }
}
