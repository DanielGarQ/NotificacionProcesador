package com.notificationprocessor.notificationprocessor.crossCutting.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilDate {
    private static final String DEFAULT_VALUE_DATE_AS_STRING = "0001-01-01"; // Formato yyyy-MM-dd
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final Date DEFAULT_VALUE_DATE;

    static {
        try {
            DEFAULT_VALUE_DATE = FORMATTER.parse(DEFAULT_VALUE_DATE_AS_STRING);
        } catch (ParseException e) {
            throw new RuntimeException("Error al inicializar la fecha predeterminada", e);
        }
    }

    public static Date fromStringToDate(final String dateValue) throws ParseException {
        return FORMATTER.parse(dateValue);
    }

    public static String fromDateToString(final Date date) {
        return FORMATTER.format(date);
    }

    public static Date getDefaultValueDate() {
        return DEFAULT_VALUE_DATE;
    }

    public static String getDefaultValueDateAsString(){
        return DEFAULT_VALUE_DATE_AS_STRING;
    }

    public static boolean isValidDate(String dateValue) {
        String pattern = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(dateValue);
        return m.matches();
    }
}