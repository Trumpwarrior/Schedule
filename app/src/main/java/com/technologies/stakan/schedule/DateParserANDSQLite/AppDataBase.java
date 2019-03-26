package com.technologies.stakan.schedule.DateParserANDSQLite;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Lesson.class}, version = 1)

public abstract class AppDataBase extends RoomDatabase {
    public abstract LessonDao lessonDao();
}
