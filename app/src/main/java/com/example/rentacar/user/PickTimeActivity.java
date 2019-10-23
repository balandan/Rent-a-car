package com.example.rentacar.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentacar.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PickTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button startDay, finishDay;
    Boolean clicked = false;
    String firstDate, lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);
        Button startDay = (Button) findViewById(R.id.startDay_calendarBtn);
        Button finishDay = (Button) findViewById(R.id.lastDay_calendarBtn);
        Button ok = (Button) findViewById(R.id.ok_calendarBtn);

        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Prima zi");
                clicked = false;
            }
        });

        finishDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Ultima zi");
                clicked = true;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickTimeActivity.this,UserMainActivity.class);
                intent.putExtra("firstDay",firstDate);
                intent.putExtra("lastDay", lastDate);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        Calendar calCurrentDay = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM,dd,yyyy");
        String saveCurrentDate = currentDate.format(calCurrentDay.getTime()).toString();
        String[] currentDay = saveCurrentDate.split(",");

        if(Integer.parseInt(currentDay[0]) > month + 1 && Integer.parseInt(currentDay[2]) == year)
        {
            Toast.makeText(PickTimeActivity.this,"Data aleasa este in trecut", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(currentDay[0]) == month + 1 && Integer.parseInt(currentDay[2]) == year && Integer.parseInt(currentDay[1]) > dayOfMonth){
            Toast.makeText(PickTimeActivity.this,"Data aleasa este in trecut", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(currentDay[2]) > year){
            Toast.makeText(PickTimeActivity.this,"Data aleasa este in trecut", Toast.LENGTH_SHORT).show();
        }
        else {

            String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

            if (clicked == false) {
                TextView textView = findViewById(R.id.firstDay_TextView);
                textView.setText(date);
                firstDate = (String.valueOf(year) + "," + String.valueOf(month) + "," + String.valueOf(dayOfMonth));
                clicked = true;
                Toast.makeText(PickTimeActivity.this, firstDate, Toast.LENGTH_SHORT).show();
            } else {
                TextView firstDayTextView = findViewById(R.id.LastDay_TextView);
                firstDayTextView.setText(date);
                lastDate = (String.valueOf(year) + "," + String.valueOf(month) + ',' + String.valueOf(dayOfMonth));
                clicked = false;
            }
        }
    }
}
