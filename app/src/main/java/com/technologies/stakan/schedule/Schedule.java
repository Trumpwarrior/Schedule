package com.technologies.stakan.schedule;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        AppDataBase db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
        LessonDao lessonDao = db.lessonDao();

        List<Lesson> allLessons = lessonDao.getAll();
        List<Lesson> lessons = new ArrayList<Lesson>();

        DateOfCourse date = new DateOfCourse();

        final TableLayout table = /*TableLayout*/ findViewById(R.id.table);
        TextView infoText;

        TextView first = findViewById(R.id.first);
        TextView second = findViewById(R.id.second);
        TextView third = findViewById(R.id.third);
        TextView fourth = findViewById(R.id.fourth);
        TextView fifth = findViewById(R.id.fifth);
        TextView sixth = findViewById(R.id.sixth);
        TextView seventh = findViewById(R.id.seventh);
        TextView eighth = findViewById(R.id.eighth);

        first.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        second.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        third.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        fourth.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        fifth.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        sixth.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        seventh.setMinimumWidth(metrics.widthPixels/(LESSONS+1));
        eighth.setMinimumWidth(metrics.widthPixels/(LESSONS+1));

        TextView monday = findViewById(R.id.monday);
        TextView tuesday = findViewById(R.id.tuesday);
        TextView wednesday = findViewById(R.id.wednesday);
        TextView thursday = findViewById(R.id.thursday);
        TextView friday = findViewById(R.id.friday);
        TextView saturday = findViewById(R.id.saturday);

        monday.setMinimumHeight(metrics.heightPixels/(DAYS+1));
        tuesday.setMinimumHeight(metrics.heightPixels/(DAYS+1));
        wednesday.setMinimumHeight(metrics.heightPixels/(DAYS+1));
        thursday.setMinimumHeight(metrics.heightPixels/(DAYS+1));
        friday.setMinimumHeight(metrics.heightPixels/(DAYS+1));
        saturday.setMinimumHeight(metrics.heightPixels/(DAYS+1));


        TableRow firstDay = findViewById(R.id.firstDay);
        TableRow secondDay = findViewById(R.id.secondDay);
        TableRow thirdDay = findViewById(R.id.thirdDay);
        TableRow fourthDay = findViewById(R.id.fourthDay);
        TableRow fifthDay = findViewById(R.id.fifthDay);
        TableRow sixthDay = findViewById(R.id.sixthDay);


//КАКОЙ-ТО НЕПОНЯТНЫЙ ФОР, ТРОГАТЬ ЕГО КОНЕЧНО ЖЕ НЕ СТОИТ (ПОПРАВЛЮ МЕЙБИ)


        for (int day = 0; day < DAYS; day++) {

            for (int i = 0; i < allLessons.size();i++) {
                date.processMyDate(allLessons.get(i).beginningOfCourse);
                if((date.timeOfCourse.get(Calendar.DAY_OF_WEEK)-2) == day) {
                    lessons.add(allLessons.get(i));
                }
            }

            for (int lesson = 0; lesson < LESSONS; lesson++) {

                infoText = new TextView(this);
                infoText.setGravity(Gravity.TOP);

                for (int i = 0; i < lessons.size();i++) {
                    if (Integer.parseInt(lessons.get(i).numberOfPara) == lesson) {
                        infoText.setText(lessons.get(i).toString());
                    }
                }

                switch (day) {
                    case 0:firstDay.addView(infoText);break;
                    case 1:secondDay.addView(infoText);break;
                    case 2:thirdDay.addView(infoText);break;
                    case 3:fourthDay.addView(infoText);break;
                    case 4:fifthDay.addView(infoText);break;
                    case 5:sixthDay.addView(infoText);break;
                }

            }

            lessons.clear();
        }

    }

}



