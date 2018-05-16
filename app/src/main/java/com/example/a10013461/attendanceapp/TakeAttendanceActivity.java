package com.example.a10013461.attendanceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class TakeAttendanceActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<String> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        classList = getIntent().getStringArrayListExtra("classList");
        Log.d("hi",classList.toString());
        textView = (TextView) findViewById(R.id.namez);

        textView.setText(classList.toString());
    }
}
