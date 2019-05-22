package com.technologies.stankin.schedule;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.technologies.stankin.schedule.Exceptions.DateException;
import com.technologies.stankin.schedule.SQLite.AppDataBase;
import com.technologies.stankin.schedule.ParsersAndStuff.DateOfCourse;
import com.technologies.stankin.schedule.SQLite.Lesson;
import com.technologies.stankin.schedule.SQLite.LessonDao;

public class Schedule extends AppCompatActivity {

    private TextView infoText;
    private LinearLayout linLayout;
    private List<LinearLayout> layouts = new ArrayList<>();
    private LessonDao lessonDao;

    private final int DAYS = 6;
    private final int LESSONS = 8;

    private TableRow firstDay;
    private TableRow secondDay;
    private TableRow thirdDay;
    private TableRow fourthDay;
    private TableRow fifthDay;
    private TableRow sixthDay;

    private DisplayMetrics metrics;
    private LinearLayout.LayoutParams lpView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        AppDataBase db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
        lessonDao = db.lessonDao();

        lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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


        firstDay = findViewById(R.id.firstDay);
        secondDay = findViewById(R.id.secondDay);
        thirdDay = findViewById(R.id.thirdDay);
        fourthDay = findViewById(R.id.fourthDay);
        fifthDay = findViewById(R.id.fifthDay);
        sixthDay = findViewById(R.id.sixthDay);

        layouts = createTable();

        updateTable(layouts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateTable(layouts);
    }

    private List<LinearLayout> createTable() {
        List<LinearLayout> layouts = new ArrayList<>();

        for (int day = 0; day < DAYS; day++) {

            for (int lesson = 0; lesson < LESSONS; lesson++) {
                linLayout = new LinearLayout(this);
                linLayout.setBackgroundColor(0);
                layouts.add(linLayout);

                switch (day) {
                    case 0:firstDay.addView(linLayout);break;
                    case 1:secondDay.addView(linLayout);break;
                    case 2:thirdDay.addView(linLayout);break;
                    case 3:fourthDay.addView(linLayout);break;
                    case 4:fifthDay.addView(linLayout);break;
                    case 5:sixthDay.addView(linLayout);break;
                }
            }
        }

        return layouts;
    }



    private void updateTable(List<LinearLayout> layouts) {

        DateOfCourse date = new DateOfCourse();
        List<Lesson> allLessons = lessonDao.getAll();
        List<Lesson> lessons = new ArrayList<>();

        for (int day = 0; day < DAYS; day++) {
            try {
                for (int i = 0; i < allLessons.size();i++) {
                    date.processMyDate(allLessons.get(i).beginningOfCourse);
                    if((date.timeOfCourse.get(Calendar.DAY_OF_WEEK)-2) == day) {
                        lessons.add(allLessons.get(i));
                    }
                }
            }catch (DateException e){}

            for (int lesson = 0; lesson < LESSONS; lesson++) {

                infoText = new TextView(this);
                infoText.setLayoutParams(lpView);
                infoText.setMinimumHeight(metrics.heightPixels/(DAYS+1));

                for (int i = 0; i < lessons.size();i++) {
                    if (Integer.parseInt(lessons.get(i).numberOfPara) == lesson) {
                        infoText.setText(lessons.get(i).toString());


                        infoText.setTextColor(Color.BLACK);
                        infoText.setBackgroundColor(Color.WHITE);

                        layouts.get(((day) * LESSONS) + (lesson)).setBackgroundResource(R.drawable.customborder);
                        layouts.get(((day) * LESSONS) + (lesson)).removeAllViews();
                        layouts.get(((day) * LESSONS) + (lesson)).addView(infoText);
                        layouts.get(((day) * LESSONS) + (lesson)).updateViewLayout(infoText, lpView);
                    }
                }
            }

            lessons.clear();
        }

    }

}



