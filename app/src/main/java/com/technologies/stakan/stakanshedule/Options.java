package com.technologies.stakan.stakanshedule;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.technologies.stakan.stakanshedule.dataStructures.*;

public class Options extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText nameOfTeacher;
    EditText audienceNumber;
    Spinner typeOfLesson;
    EditText beginningOfCourse;
    EditText endingOfCourse;
    Spinner numberOfPara;
    Switch alternation;
    Button addButton;
    TextView check;

    AppDataBase db;

    Integer[] paraData = {1, 2, 3, 4, 5, 6, 7, 8};
    String[] typeData = {"Семинар","Лекция","Лабораторная"};

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

        db =  Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database").allowMainThreadQueries().build();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addButton) {
                if (TextUtils.isEmpty(name.getText().toString())|| //проверка текстовых полей на пустоту
                    TextUtils.isEmpty(nameOfTeacher.getText().toString()) ||
                    TextUtils.isEmpty(audienceNumber.getText().toString()) ||
                    TextUtils.isEmpty(beginningOfCourse.getText().toString()) ||
                    TextUtils.isEmpty(endingOfCourse.getText().toString()))
                { return; }

                DateOfCourse plusBegOfCouse = new DateOfCourse(); //создает объекты DateOfCourse, чтобы они все обработали и выдали Calendar в случае годной даты
                plusBegOfCouse.processMyDate(beginningOfCourse.getText().toString());
                DateOfCourse plusEndOfCouse = new DateOfCourse();
                plusEndOfCouse.processMyDate(endingOfCourse.getText().toString());

                Calendar thisTime = new GregorianCalendar();//нужно для того, чтобы взять текущее время и сравнить с началом курса
                thisTime.set(Calendar.HOUR_OF_DAY,1);//ставим время на начало дня
                plusBegOfCouse.timeOfCourse.set(Calendar.HOUR_OF_DAY,2);//время начала курса позже, потому что иначе если в 1 день, то не поставится(костыль, но удобный)
                plusEndOfCouse.timeOfCourse.set(Calendar.HOUR_OF_DAY,3);//чтобы можно было поставить однодневное занятие(еще веселее на 2 микрокостылях)

                if(plusBegOfCouse.getYear() < 1000){check.setText("Некорректно введённая дата начала");return;}//вот эти штуки проверяют дату!! Они молодцы, хоть и логика странная тут
                if(plusEndOfCouse.getYear() < 1000){check.setText("Некорректно введённая дата окончания");return;}
                if(plusEndOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK) != plusBegOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK)){check.setText("День недели начала и окончания не совпадают");return;}
                if(plusBegOfCouse.timeOfCourse.after(plusEndOfCouse.timeOfCourse)) {check.setText("Дата начала позже даты окончания");return;}
                if(plusBegOfCouse.timeOfCourse.before(thisTime)) {check.setText("Дата начала раньше сегоднешнего дня");return;}


                LessonDao lessonDao = db.lessonDao();
                Lesson lesson = new Lesson();

                lesson.name = name.getText().toString();
                lesson.nameOfTeacher = nameOfTeacher.getText().toString();
                lesson.audienceNumber = audienceNumber.getText().toString();
                lesson.typeOfLesson = String.valueOf(typeOfLesson.getSelectedItemId());
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
                        if(allLessons.get(i).numberOfPara.equals(lesson.numberOfPara)){check.setText("На это время уже есть занятие");return;}
                    }
                }

                dateShallNotPass.processMyDate(allLessons.get(i).beginningOfCourse);//дата окончания записи должна быть позже даты начала из базы для пересечения
                if(plusEndOfCouse.timeOfCourse.before(dateShallNotPass.timeOfCourse)) {
                    if(plusEndOfCouse.timeOfCourse.get(Calendar.DAY_OF_WEEK) == dateShallNotPass.timeOfCourse.get(Calendar.DAY_OF_WEEK)){
                        if(allLessons.get(i).numberOfPara.equals(lesson.numberOfPara)){check.setText("На это время уже есть занятие");return;}
                    }
                }

            }

                lessonDao.insert(lesson);



                check.setText("Все правильно. Занятие добавлено.");//если дошло, значит все cool
        }
    }
}
