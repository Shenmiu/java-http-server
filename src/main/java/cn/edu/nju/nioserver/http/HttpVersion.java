package cn.edu.nju.nioserver.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpVersion {

    /**
     * Http1.1
     */
    private static final String HTTP_1_1_STRING = "HTTP/1.1";

    /**
     * 协议名称HTTP
     */
    private final String protocolName;

    /**
     * 主版本号
     */
    private final int majorVersion;

    /**
     * 副版本号
     */
    private final int minorVersion;

    /**
     * 实际version内容（未实现其他的）
     */
    private final String text;

    /**
     * 是否保持长连接（不知道是否要实现，先放在这里）
     */
    private final boolean keepAliveDefault;

    /**
     * HTTP/1.1
     */
    public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true);

    /**
     * 正则匹配version
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");

    public HttpVersion(String text, boolean keepAliveDefault) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }

        text = text.trim().toUpperCase();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("empty text");
        }

        Matcher m = VERSION_PATTERN.matcher(text);
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text);
        }

        protocolName = m.group(1);
        majorVersion = Integer.parseInt(m.group(2));
        minorVersion = Integer.parseInt(m.group(3));
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
    }

    public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
        if (protocolName == null) {
            throw new IllegalArgumentException("protocolName is null");
        }

        protocolName = protocolName.trim().toUpperCase();
        if (protocolName.isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
        }

        for (int i = 0; i < protocolName.length(); i++) {
            if (Character.isISOControl(protocolName.charAt(i)) ||
                    Character.isWhitespace(protocolName.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocolName");
            }
        }

        this.protocolName = protocolName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        text = protocolName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
    }

    /**
     * 根据version内容获取实际对象
     *
     * @param text version内容
     * @return String
     */
    public static HttpVersion valueOf(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }

        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("text is empty");
        }

        HttpVersion version = null;
        if (HTTP_1_1_STRING.equals(text)) {
            version = HTTP_1_1;
        } else {
            version = new HttpVersion(text, true);
        }
        return version;
    }

    public String protocolName() {
        return protocolName;
    }


    public int majorVersion() {
        return majorVersion;
    }


    public int minorVersion() {
        return minorVersion;
    }


    public String text() {
        return text;
    }


    public boolean isKeepAliveDefault() {
        return keepAliveDefault;
    }

    @Override
    public int hashCode() {
        return (protocolName().hashCode() * 31 + majorVersion()) * 31 +
                minorVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HttpVersion)) {
            return false;
        }

        HttpVersion that = (HttpVersion) o;
        return minorVersion() == that.minorVersion() &&
                majorVersion() == that.majorVersion() &&
                protocolName().equals(that.protocolName());
    }
}
