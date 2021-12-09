package cn.detachment.utils.date;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author haoxp
 * @date 20/9/29
 */
public class DateUtil {

    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.YYYY_MM_DD_HH_MM_SS_24);
        System.out.println(dateTimeFormatter.format(ZonedDateTime.now()));
    }

}
