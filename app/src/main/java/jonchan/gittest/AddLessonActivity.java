package jonchan.gittest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddLessonActivity extends BaseActivity {

    private Button btnAddLesson;
    private EditText etxtDate;
    private EditText etxtStartTime;
    private EditText etxtEndTime;
    private EditText etxtLocation;

    private TextView add_lesson_title;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtTimeE;
    private TextView txtLocation;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;


    private FirebaseAuth mAuth;
    private ImageView add_lesson_back;
    FirebaseDatabase database;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlesson);

        setTitle("ADD LESSON");

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");

        add_lesson_title = (TextView) findViewById(R.id.add_lesson_title);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTimeE = (TextView) findViewById(R.id.txtTimeE);
        txtLocation = (TextView) findViewById(R.id.txtLocation);

        // add_lesson_back=(ImageView)findViewById(R.id.add_lesson_back);

        etxtDate = (EditText) findViewById(R.id.etxtDate);
        etxtStartTime = (EditText) findViewById(R.id.etxtStartTime);
        etxtEndTime = (EditText) findViewById(R.id.etxtEndTime);
        etxtLocation = (EditText) findViewById(R.id.etxtLocation);
        btnAddLesson = (Button) findViewById(R.id.btnAddLesson);

        add_lesson_title.setTypeface(tfmsb);
        txtDate.setTypeface(tfrb);
        etxtDate.setTypeface(tfrb);
        txtTime.setTypeface(tfrb);
        etxtStartTime.setTypeface(tfrb);
        txtTimeE.setTypeface(tfrb);
        etxtEndTime.setTypeface(tfrb);
        txtLocation.setTypeface(tfrb);
        etxtLocation.setTypeface(tfrb);
        btnAddLesson.setTypeface(tfml);


        Intent myIntent = getIntent();
        final String student_uid = myIntent.getStringExtra("STUDENT_ID");

     /*   add_lesson_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    */
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

                String str = date + " " + startTime + ":00";
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1= null;
                try {
                    date1 = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long epoch = -1 * date1.getTime();
                long epochPos = date1.getTime();

                newLesson.child("Date").setValue(date);
                newLesson.child("StartTime").setValue(startTime);
                newLesson.child("EndTime").setValue(endTime);
                newLesson.child("Teacher").setValue(teacher_id);
                newLesson.child("Student").setValue(student_uid);
                newLesson.child("Location").setValue(location);
                newLesson.child("TimeStamp").setValue(epoch);
                newLesson.child("TimeStampAsc").setValue(epochPos);


                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
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

    @Override
    int getContentViewId() {
        return R.layout.activity_addlesson;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    protected void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddLessonActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int newMonthOfYear = monthOfYear + 1;
                if(newMonthOfYear < 10 && dayOfMonth < 10){
                    etxtDate.setText(year + "-0" + newMonthOfYear + "-0" + dayOfMonth);
                }else if(newMonthOfYear >= 10 && dayOfMonth < 10){
                    etxtDate.setText(year + "-" + newMonthOfYear + "-0" + dayOfMonth);
                }else if(newMonthOfYear < 10 && dayOfMonth >= 10){
                    etxtDate.setText(year + "-0" + newMonthOfYear + "-" + dayOfMonth);
                }else {
                    etxtDate.setText(year + "-" + newMonthOfYear + "-" + dayOfMonth);
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void showTimePickDlgStart(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLessonActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if(hour < 10 && minute < 10){
                    etxtStartTime.setText("0"+hour + ":0" + minute);
                }else if(hour <10 && minute >= 10){
                    etxtStartTime.setText("0"+hour + ":" + minute);
                }else if(hour >=10 && minute < 10){
                    etxtStartTime.setText(hour + ":0" + minute);
                }else{
                    etxtStartTime.setText(hour + ":" + minute);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    protected void showTimePickDlgEnd(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLessonActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if(hour < 10 && minute < 10){
                    etxtEndTime.setText("0"+hour + ":0" + minute);
                }else if(hour <10 && minute >= 10){
                    etxtEndTime.setText("0"+hour + ":" + minute);
                }else if(hour >=10 && minute < 10){
                    etxtEndTime.setText(hour + ":0" + minute);
                }else{
                    etxtEndTime.setText(hour + ":" + minute);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
}
