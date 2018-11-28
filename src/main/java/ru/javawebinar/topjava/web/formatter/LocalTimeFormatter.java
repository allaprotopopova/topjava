package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public LocalTime parse(String s, Locale locale) {
        return StringUtils.isEmpty(s) ? null : LocalTime.parse(s);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime == null ? "" : localTime.toString();
    }
}
