package jonchan.gittest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddLessonActivity extends AppCompatActivity {

    private Button btnAddLesson;
    private EditText etxtDate;
    private EditText etxtStartTime;
    private EditText etxtEndTime;
    private EditText etxtLocation;
    private FirebaseAuth mAuth;
    private ImageView add_lesson_back;
    FirebaseDatabase database;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlesson);

        etxtDate = (EditText) findViewById(R.id.etxtDate);
        etxtStartTime = (EditText) findViewById(R.id.etxtStartTime);
        etxtEndTime = (EditText) findViewById(R.id.etxtEndTime);
        etxtLocation = (EditText) findViewById(R.id.etxtLocation);
        btnAddLesson = (Button) findViewById(R.id.btnAddLesson);
        add_lesson_back=(ImageView)findViewById(R.id.add_lesson_back);

        Intent myIntent = getIntent();
        final String student_uid = myIntent.getStringExtra("STUDENT_ID");

        add_lesson_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickDlg();

            }
        });

        etxtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimePickDlgStart();

            }
        });

        etxtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTimePickDlgEnd();

            }
        });

        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String teacher_id = mAuth.getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance();
                mRef = database.getReference("Lesson");
                DatabaseReference newLesson = mRef.push();
                String date = etxtDate.getText().toString();
                String location = etxtLocation.getText().toString();
                String startTime = etxtStartTime.getText().toString();
                String endTime = etxtEndTime.getText().toString();
                newLesson.child("Date").setValue(date);
                newLesson.child("StartTime").setValue(startTime);
                newLesson.child("EndTime").setValue(endTime);
                newLesson.child("Teacher").setValue(teacher_id);
                newLesson.child("Student").setValue(student_uid);
                newLesson.child("Location").setValue(location);


                Intent myIntent = new Intent(view.getContext(), DisplayLessonActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

            }
        });



    }

    protected void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddLessonActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etxtDate.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void showTimePickDlgStart(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLessonActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                etxtStartTime.setText(hour + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    protected void showTimePickDlgEnd(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLessonActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                etxtEndTime.setText(hour + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
}
