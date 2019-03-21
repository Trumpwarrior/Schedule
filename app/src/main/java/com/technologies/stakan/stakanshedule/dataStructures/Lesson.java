package com.technologies.stakan.stakanshedule.dataStructures;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public String nameOfTeacher;

    public String audienceNumber;

    public String typeOfLesson;

    public String beginningOfCourse;

    public String endingOfCourse;

    public String numberOfPara;

    public String alternation;
}
