package com.technologies.stakan.schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button options;
    Button shedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        options = findViewById(R.id.options);
        options.setOnClickListener(this);

        shedule = findViewById(R.id.shedule);
        shedule.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.options) {
                Intent intent = new Intent(this, Options.class);
                startActivity(intent);
        }

        if(view.getId() == R.id.shedule) {
            Intent intent = new Intent(this, Schedule.class);
            startActivity(intent);
        }
    }

}
