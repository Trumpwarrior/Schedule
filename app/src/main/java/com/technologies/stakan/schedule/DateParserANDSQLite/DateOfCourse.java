package com.technologies.stakan.schedule.DateParserANDSQLite;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateOfCourse {

    private Integer day = 0;
    private Integer month = 0;

    public Calendar timeOfCourse = new GregorianCalendar(1,1,1);

    public int getActuallyYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public Integer getDay() { return day; }

    public Integer getMonth() { return month; }


    public void processMyDate(String date) {

        Matcher dateMatcher = Pattern.compile("\\d\\d").matcher(date);

            if (dateMatcher.find()) { //забираем из строки день
                day = Integer.parseInt(dateMatcher.group());
            }
            if (dateMatcher.find()) { //забираем из строки месяц
                month = Integer.parseInt(dateMatcher.group());
            }


        timeOfCourse.setLenient(false);
        timeOfCourse.set(getActuallyYear(),month -1 , day); // -1 нужен, потому что этот класс почему-то прибавляет единицу (хз)

        try { //проверка даты на действительность
            timeOfCourse.getTime();
        }catch (Exception e) {
            day = 0;
            month = 0;
        }


    }
}
