package com.technologies.stakan.schedule;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.technologies.stakan.schedule.DateParserANDSQLite.*;

public class Options extends AppCompatActivity implements View.OnClickListener {

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

    private final Integer[] paraData = {1, 2, 3, 4, 5, 6, 7, 8};
    private final String[] typeData = {"Семинар","Лекция","Лабораторная"};

    private boolean buttonAccessBeg;
    private boolean buttonAccessEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //создание всяких нужных штук и нахождение других нужных штук по id
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        ArrayAdapter<Integer> lessons = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paraData);  // Создание выпадающего списка
        lessons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numberOfPara = /*Spinner)*/ findViewById(R.id.numberOfPara);
        numberOfPara.setAdapter(lessons);


        ArrayAdapter<String> type = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeData);  // Создание выпадающего списка
        lessons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeOfLesson = /*Spinner*/ findViewById(R.id.typeOfLesson);
        typeOfLesson.setAdapter(type);

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
                if(!dateMatcher.matches()) {
                    buttonAccessBeg = false;
                    beginningOfCourse.setTextColor(Color.RED);
                } else {
                    buttonAccessBeg = true;
                    beginningOfCourse.setTextColor(Color.GREEN);
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
                if(!dateMatcher.matches()) {
                    buttonAccessEnd = false;
                    endingOfCourse.setTextColor(Color.RED);
                } else {
                    buttonAccessEnd = true;
                    endingOfCourse.setTextColor(Color.GREEN);
                }

                addButton.setClickable(buttonAccessBeg && buttonAccessEnd);
            }
        });

        db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addButton) {
                if (TextUtils.isEmpty(name.getText().toString())|| //проверка текстовых полей на пустоту
                    TextUtils.isEmpty(beginningOfCourse.getText().toString()) ||
                    TextUtils.isEmpty(endingOfCourse.getText().toString()))
                { check.setText("Введите название занятия");
                    check.setTextColor(Color.RED);
                    return;
                }

                DateOfCourse plusBegOfCouse = new DateOfCourse();
                plusBegOfCouse.processMyDate(beginningOfCourse.getText().toString());
                DateOfCourse plusEndOfCouse = new DateOfCourse();
                plusEndOfCouse.processMyDate(endingOfCourse.getText().toString());

                plusBegOfCouse.timeOfCourse.set(Calendar.HOUR_OF_DAY,2);//время начала курса позже, потому что иначе если в 1 день, то не поставится
                plusEndOfCouse.timeOfCourse.set(Calendar.HOUR_OF_DAY,3);//чтобы можно было поставить однодневное занятие

                if(plusEndOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK) != plusBegOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK)) {
                    check.setText("День недели начала и окончания не совпадают");
                    check.setTextColor(Color.RED);
                    return;
                }

                if(plusBegOfCouse.timeOfCourse.after(plusEndOfCouse.timeOfCourse)) {
                    check.setText("Дата начала позже даты окончания");
                    check.setTextColor(Color.RED);
                    return;
                }


                LessonDao lessonDao = db.lessonDao();
                Lesson lesson = new Lesson();

                lesson.name = name.getText().toString();
                lesson.nameOfTeacher = nameOfTeacher.getText().toString();
                lesson.audienceNumber = audienceNumber.getText().toString();
                lesson.typeOfLesson = typeData[(int)typeOfLesson.getSelectedItemId()];
                lesson.beginningOfCourse = beginningOfCourse.getText().toString();
                lesson.endingOfCourse = endingOfCourse.getText().toString();
                lesson.numberOfPara = String.valueOf(numberOfPara.getSelectedItemId());

                if(alternation.isChecked())lesson.alternation = "true";
                else lesson.alternation = "false";

                List<Lesson> allLessons = lessonDao.getAll();
                DateOfCourse dateShallNotPass = new DateOfCourse();//пересекающаяся дата не пройдёт!!!

            for(int i = 0; i < allLessons.size(); i++) {

                dateShallNotPass.processMyDate(allLessons.get(i).endingOfCourse);//дата начала записи должна быть раньше даты конца из базы для пересечения
                dateShallNotPass.timeOfCourse.set(Calendar.HOUR_OF_DAY,3);

                if(plusBegOfCouse.timeOfCourse.before(dateShallNotPass.timeOfCourse)) {
                    if(plusBegOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK) == dateShallNotPass.timeOfCourse.get(Calendar.DAY_OF_WEEK)){
                        if(allLessons.get(i).numberOfPara.equals(lesson.numberOfPara)){
                            check.setText("На это время уже есть занятие");
                            check.setTextColor(Color.RED);
                            return;}
                    }
                }

                dateShallNotPass.processMyDate(allLessons.get(i).beginningOfCourse);//дата окончания записи должна быть позже даты начала из базы для пересечения
                if(plusEndOfCouse.timeOfCourse.before(dateShallNotPass.timeOfCourse)) {
                    if(plusEndOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK) == dateShallNotPass.timeOfCourse.get(Calendar.DAY_OF_WEEK)){
                        if(allLessons.get(i).numberOfPara.equals(lesson.numberOfPara)){
                            check.setText("На это время уже есть занятие");
                            check.setTextColor(Color.RED);
                            return;}
                    }
                }

            }

                lessonDao.insert(lesson);



                check.setText("Все правильно. Занятие добавлено.");
            check.setTextColor(Color.GREEN);//если дошло, значит все cool
        }
        }



    }
