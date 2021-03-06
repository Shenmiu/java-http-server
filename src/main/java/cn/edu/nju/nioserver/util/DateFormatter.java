package cn.edu.nju.nioserver.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.US);

    /**
     * 将LocalDateTime转换为Http协议时间格式
     *
     * @param localDateTime LocalDateTime
     * @return Http协议时间
     */
    public static String date2String(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new NullPointerException("localDateTime is null");
        }
        ZonedDateTime utcTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        return utcTime.format(formatter);
    }

    /**
     * 将格式Sun, 27 Nov 2016 19:37:15 GMT转换为LocalDateTime
     *
     * @param text String
     * @return LocalDateTime
     */
    public static LocalDateTime parseString2Date(String text) {
        //暂时不实现
        return null;
    }

}
