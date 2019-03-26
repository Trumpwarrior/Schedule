package com.technologies.stakan.schedule.DateParserANDSQLite;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateOfCourse {

    private Integer day = 0;
    private Integer month = 0;
    private Integer year = 0;

    private Matcher dateMatcher;

    public Integer getDay() {
        return day;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public Calendar timeOfCourse = new GregorianCalendar(1,1,1);

    public void processMyDate(String date) {

        String dateCopy = date;
        dateMatcher = Pattern.compile("\\d\\d").matcher(dateCopy);

        try {
            if(dateMatcher.find()) { //забираем из строки день
                day = Integer.parseInt(dateMatcher.group());
            }
            if(dateMatcher.find()) { //забираем из строки месяц
                month = Integer.parseInt(dateMatcher.group());
            }

            if(dateMatcher.find()) { //забираем год
                year = Integer.parseInt(dateMatcher.group());
            }

            if(dateMatcher.find()) { //проверяем, нет ли лишних цифр, если есть, кидаем ошибку
                throw new Exception();
            }

            year += 2000; //даём 2000, чтобы свести к нормальной дате

        }catch (Exception e) {
            day = 0;
            month = 0;
            year = 0;
        }


        timeOfCourse.setLenient(false);
        timeOfCourse.set(year,month -1 , day); // -1 нужен, потому что этот класс почему-то прибавляет единицу (хз)

        try { //проверка даты на действительность
            timeOfCourse.getTime();
        }catch (Exception e) {
            day = 0;
            month = 0;
            year = 0;
        }


    }
}
