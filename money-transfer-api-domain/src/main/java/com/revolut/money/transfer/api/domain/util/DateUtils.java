package com.revolut.money.transfer.api.domain.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final String UTC = "UTC";
    private static String dateFormat = "yyyy-MM-dd";

    private DateUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static int dayDifference(LocalDate from, LocalDate to) {
        return Period.between(from, to.plusDays(1L)).getDays();
    }

    public static int dayDifference(Date from, Date to) {
        LocalDate localFrom = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localTo = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dayDifference(localFrom, localTo);
    }

    public static int dayDifference(String from, String to) {
        return dayDifference(from, to, dateFormat);
    }

    public static int dayDifference(String from, String to, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localFrom = LocalDate.parse(from, formatter);
        LocalDate localTo = LocalDate.parse(to, formatter);
        return dayDifference(localFrom, localTo);
    }

    public static String format(String date, String fromFormat, String toFormat) {
        DateTimeFormatter fromFormatter = DateTimeFormatter.ofPattern(fromFormat);
        DateTimeFormatter toFormatter = DateTimeFormatter.ofPattern(toFormat);
        return LocalDate.parse(date, fromFormatter).format(toFormatter);
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDate fromDDMMYYYYToLocalDate(String dateDDMMYYY, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        formatter = formatter.withLocale(locale);
        return LocalDate.parse(dateDDMMYYY, formatter);
    }

    public static String dateToString(Date date) {
        return localDateToString(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")).toLocalDate());
    }

    public static String dateTimeToString(Date date) {
        return localDateTimeToString(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")));
    }
}
