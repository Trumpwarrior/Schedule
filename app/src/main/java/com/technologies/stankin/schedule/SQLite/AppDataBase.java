package com.technologies.stankin.schedule.SQLite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Lesson.class}, version = 1)

public abstract class AppDataBase extends RoomDatabase {
    public abstract LessonDao lessonDao();
}
