package com.technologies.stakan.schedule;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.technologies.stakan.schedule.DateParserANDSQLite.AppDataBase;
import com.technologies.stakan.schedule.DateParserANDSQLite.DateOfCourse;
import com.technologies.stakan.schedule.DateParserANDSQLite.Lesson;
import com.technologies.stakan.schedule.DateParserANDSQLite.LessonDao;

public class Schedule extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);

        final int DAYS = 6;
        final int LESSONS = 8;

        AppDataBase db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
        LessonDao lessonDao = db.lessonDao();

        List<Lesson> allLessons = lessonDao.getAll();
        List<Lesson> lessons = new ArrayList<Lesson>();

        DateOfCourse date = new DateOfCourse();

        final TableLayout table = /*TableLayout*/ findViewById(R.id.table);
        TextView infoText;

//КАКОЙ-ТО НЕПОНЯТНЫЙ ФОР, ТРОГАТЬ ЕГО КОНЕЧНО ЖЕ НЕ СТОИТ (ПОПРАВЛЮ МЕЙБИ)

        for (int day = 0; day < DAYS; day++) {

            for (int i = 0; i < allLessons.size();i++) {
                date.processMyDate(allLessons.get(i).beginningOfCourse);
                if(date.timeOfCourse.get(Calendar.DAY_OF_WEEK) == day) {
                    lessons.add(allLessons.get(i));
                }
            }

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int lesson = 0; lesson < LESSONS; lesson++) {
                infoText = /*TextView*/ new TextView(this);

                for (int i = 0; i < lessons.size();i++) {
                    if (Integer.parseInt(lessons.get(i).numberOfPara) == lesson) {
                        infoText.setText(lessons.get(i).toString());
                    }
                }


                tableRow.addView(infoText, lesson);
            }

            lessons.clear();
            table.addView(tableRow, day);
        }

    }

}



