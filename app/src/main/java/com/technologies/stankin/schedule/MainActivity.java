package com.technologies.stankin.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button options;
    private Button shedule;
    private Intent intentOptions;
    private Intent intentSchdule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        options = findViewById(R.id.options);
        options.setOnClickListener(this);

        shedule = findViewById(R.id.shedule);
        shedule.setOnClickListener(this);

        intentOptions = new Intent(this, Options.class);
        intentSchdule = new Intent(this, Schedule.class);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.options) {

            intentOptions .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intentOptions , 0);
        }

        if(view.getId() == R.id.shedule) {
            intentSchdule.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intentSchdule, 0);
        }

    }

}
