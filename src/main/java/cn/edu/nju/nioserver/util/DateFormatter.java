package cn.edu.nju.nioserver.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {

    /**
     * 将LocalDateTime转换为Http协议时间格式
     *
     * @param localDateTime LocalDateTime
     * @return Http协议时间
     */
    public static String date2String(LocalDateTime localDateTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(localDateTime.getYear(), localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond());
        // Locale.US用于将日期区域格式设为美国
        SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        // 时区设为格林尼治
        greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return greenwichDate.format(cal.getTime());
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
