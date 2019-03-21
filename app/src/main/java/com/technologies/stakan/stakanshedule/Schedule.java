package com.technologies.stakan.stakanshedule;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.List;

import com.technologies.stakan.stakanshedule.dataStructures.AppDataBase;
import com.technologies.stakan.stakanshedule.dataStructures.Lesson;
import com.technologies.stakan.stakanshedule.dataStructures.LessonDao;

public class Schedule extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);

        editText = (EditText) findViewById(R.id.editText);

        AppDataBase db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
        LessonDao lessonDao = db.lessonDao();
        Lesson lesson = new Lesson();

        List<Lesson> allLessons = lessonDao.getAll();

        for(int i = 0; i < allLessons.size(); i++) {

            lesson = allLessons.get(i);

            editText.setText(editText.getText() + " " +
                    lesson.name + " " +
                    lesson.nameOfTeacher + " " +
                    lesson.audienceNumber + " " +
                    lesson.typeOfLesson + " " +
                    lesson.beginningOfCourse + " " +
                    lesson.endingOfCourse + " " +
                    lesson.numberOfPara + " " +
                    lesson.alternation + "\n");
        }
    }


}
