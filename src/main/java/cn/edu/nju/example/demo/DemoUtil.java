package cn.edu.nju.example.demo;

import java.util.regex.Pattern;

/**
 * <p>
 * <p>
 *
 * @author Shenmiu
 * @date 2019/12/20
 */
public class DemoUtil {
    public static final Pattern DEMO_URI_PATTERN = Pattern.compile("^/([a-z]+)/?(.*)$");
}
