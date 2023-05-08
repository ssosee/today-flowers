package quokka.todayflowers.global.common;

import quokka.todayflowers.web.response.PageDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleConvert {
    public String convertLocalDateTimeToString(LocalDateTime dateTime) {
        // 2021-09-02 14:56:20
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
