package com.technologies.stankin.schedule;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.technologies.stankin.schedule.ParsersAndStuff.DateOfCourse;
import com.technologies.stankin.schedule.SQLite.*;
import com.technologies.stankin.schedule.Exceptions.DateException;
import com.technologies.stankin.schedule.Exceptions.EmptyTextField;
import com.technologies.stankin.schedule.Exceptions.IntersectionException;

public class Options extends Activity implements View.OnClickListener {

    private EditText name;
    private EditText nameOfTeacher;
    private EditText audienceNumber;
    private Spinner typeOfLesson;
    private EditText beginningOfCourse;
    private EditText endingOfCourse;
    private Spinner numberOfPara;
    private Switch alternation;
    private Button addButton;
    private TextView check;

    private AppDataBase db;

    private boolean buttonAccessBeg;
    private boolean buttonAccessEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        numberOfPara = /*Spinner*/ findViewById(R.id.numberOfPara);

        typeOfLesson = /*Spinner*/ findViewById(R.id.typeOfLesson);

        addButton = /*Button*/ findViewById(R.id.addButton);
        name = /*EditText*/ findViewById(R.id.name);
        nameOfTeacher = /*EditText*/ findViewById(R.id. nameOfTeacher);
        audienceNumber = /*EditText*/ findViewById(R.id.audienceNumber);
        beginningOfCourse = /*EditText*/ findViewById(R.id.beginningOfCourse);
        endingOfCourse = /*EditText*/ findViewById(R.id.endingOfCourse);
        alternation = /*Switch*/ findViewById(R.id.alternation);
        check = /*TextView*/ findViewById(R.id.check);



        addButton.setOnClickListener(this);
        beginningOfCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Matcher dateMatcher = Pattern.compile("\\d\\d\\.\\d\\d").matcher(s);
                Matcher zerosMatcher = Pattern.compile("00").matcher(s);
                if(!dateMatcher.matches()) {
                    buttonAccessBeg = false;
                    beginningOfCourse.setTextColor(Color.RED);
                } else {
                        buttonAccessBeg = true;
                        beginningOfCourse.setTextColor(Color.GREEN);
                }
                if(zerosMatcher.find()) {
                    buttonAccessBeg = false;
                    beginningOfCourse.setTextColor(Color.RED);
                }
                addButton.setClickable(buttonAccessBeg && buttonAccessEnd);
            }
        });

        endingOfCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Matcher dateMatcher = Pattern.compile("\\d\\d\\.\\d\\d").matcher(s);
                Matcher zerosMatcher = Pattern.compile("00").matcher(s);
                if(!dateMatcher.matches()) {
                    buttonAccessEnd = false;
                    endingOfCourse.setTextColor(Color.RED);
                } else {
                    buttonAccessEnd = true;
                    endingOfCourse.setTextColor(Color.GREEN);
                }
                if(zerosMatcher.find()) {
                    buttonAccessEnd = false;
                    endingOfCourse.setTextColor(Color.RED);
                }

                addButton.setClickable(buttonAccessBeg && buttonAccessEnd);
            }
        });

        db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.addButton) {

            try {

                if(TextUtils.isEmpty(name.getText().toString()) ||
                   TextUtils.isEmpty(beginningOfCourse.getText().toString()) ||
                   TextUtils.isEmpty(endingOfCourse.getText().toString())) {

                    throw new EmptyTextField();
                }

                DateOfCourse plusBegOfCouse = new DateOfCourse();
                plusBegOfCouse.processMyDate(beginningOfCourse.getText().toString());
                DateOfCourse plusEndOfCouse = new DateOfCourse();
                plusEndOfCouse.processMyDate(endingOfCourse.getText().toString());

                plusEndOfCouse.timeOfCourse.set(Calendar.HOUR_OF_DAY,2);

                plusBegOfCouse.isBeginDateOf(plusEndOfCouse); //throws DateException


                LessonDao lessonDao = db.lessonDao();
                Lesson lesson = new Lesson();

                initializeLesson(lesson);

                List<Lesson> allLessons = lessonDao.getAll();

                lesson.isAnyIntersections(allLessons);//throws IntersectionException

                lessonDao.insert(lesson);

                check.setText("Все правильно. Занятие добавлено.");
                check.setTextColor(Color.GREEN);


            }
            catch(EmptyTextField e) {
                check.setText("Не заполнено одно из необходимых полей");
                check.setTextColor(Color.RED);
                return;
            }
            catch (DateException e) {
                check.setText("Вы что-то напутали с датами");
                check.setTextColor(Color.RED);
            }
            catch (IntersectionException e) {
                check.setText("На это время уже есть занятие");
                check.setTextColor(Color.RED);
            }

        }
    }


    void initializeLesson(Lesson lesson) {

        lesson.courseInfo = name.getText().toString() + "\n" +
                nameOfTeacher.getText().toString() + "\n" +
                audienceNumber.getText().toString() + "\n" +
                typeOfLesson.getSelectedItem();

        lesson.beginningOfCourse = beginningOfCourse.getText().toString();
        lesson.endingOfCourse = endingOfCourse.getText().toString();
        lesson.numberOfPara = String.valueOf(numberOfPara.getSelectedItemId());

        if(alternation.isChecked()) lesson.alternation = "true";
        else lesson.alternation = "false";
    }

}
