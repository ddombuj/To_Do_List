package com.ddombuj.to_do_list.addNote;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ddombuj.to_do_list.R;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    TextView uid_user, user_email, actual_date_time, date, state;
    EditText title, description;
    Button btn_calendar;

    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initializeVariables();
        getData();
        getActualDateTime();

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNote.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String dayFormatted, monthFormatted;
                        //Get day
                        if(selectedDay < 10){
                            dayFormatted = "0" + String.valueOf(selectedDay);
                        }  else {
                            dayFormatted = String.valueOf(selectedDay);
                        }

                        //Get month
                        int month = selectedMonth + 1;

                        if(month < 10){
                            monthFormatted = "0" + String.valueOf(month);
                        }else {
                            monthFormatted = String.valueOf(month);
                        }

                        //Set date in textview
                        date.setText(dayFormatted + "/" + monthFormatted + "/" + selectedYear);
                    }
                }
                , year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void getActualDateTime() {
    }

    private void getData() {
        
    }

    private void initializeVariables() {
    }
}