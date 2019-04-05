package com.technologies.stakan.schedule.DateParserANDSQLite;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.technologies.stakan.schedule.Exceptions.IntersectionException;

import java.util.Calendar;
import java.util.List;

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

    public void isAnyIntersections(List<Lesson> lessons) throws IntersectionException {

        DateOfCourse dateFromList = new DateOfCourse();
        DateOfCourse thisDate = new DateOfCourse();

        for(int i = 0; i < lessons.size(); i++) {

            thisDate.processMyDate(beginningOfCourse);

            dateFromList.processMyDate(lessons.get(i).endingOfCourse);//дата начала записи должна быть раньше даты конца из базы для пересечения
            dateFromList.timeOfCourse.set(Calendar.HOUR_OF_DAY,3);

            if(thisDate.timeOfCourse.before(dateFromList.timeOfCourse)) {
                if(thisDate.timeOfCourse.get(Calendar.DAY_OF_WEEK) == dateFromList.timeOfCourse.get(Calendar.DAY_OF_WEEK)){
                    if(lessons.get(i).numberOfPara.equals(numberOfPara)){

                        throw new IntersectionException();
                    }
                }
            }

            thisDate.processMyDate(endingOfCourse);

            dateFromList.processMyDate(lessons.get(i).beginningOfCourse);//дата окончания записи должна быть позже даты начала из базы для пересечения
            if(thisDate.timeOfCourse.before(dateFromList.timeOfCourse)) {
                if(thisDate.timeOfCourse.get(Calendar.DAY_OF_WEEK) == dateFromList.timeOfCourse.get(Calendar.DAY_OF_WEEK)){
                    if(lessons.get(i).numberOfPara.equals(numberOfPara)){

                        throw new IntersectionException();
                    }
                }
            }

        }

    }

}
