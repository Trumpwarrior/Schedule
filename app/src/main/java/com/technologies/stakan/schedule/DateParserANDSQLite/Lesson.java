package com.technologies.stakan.schedule.DateParserANDSQLite;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    long id;

    public String name;

    public String nameOfTeacher;

    public String audienceNumber;

    public String typeOfLesson;

    public String beginningOfCourse;

    public String endingOfCourse;

    public String numberOfPara;

    public String alternation;
//.................................



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(name);
        stringBuilder.append("\n");
        stringBuilder.append(nameOfTeacher);
        stringBuilder.append("\n");
        stringBuilder.append(typeOfLesson);
        stringBuilder.append(" ");
        stringBuilder.append(audienceNumber);
        stringBuilder.append("\n");
        stringBuilder.append(beginningOfCourse);
        stringBuilder.append("-");
        stringBuilder.append(endingOfCourse);
        stringBuilder.append(" ");

        if(alternation.equals("true")) stringBuilder.append("ч.н");
        else stringBuilder.append("к.н");

        return stringBuilder.toString();
    }

}
