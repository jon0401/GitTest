package jonchan.gittest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddPracticeNoteActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    private TextView txtStudentCreatorName;
    private EditText eTxtPracticeNoteDateEnter;
    private EditText eTxtPracticeNoteDurationEnter;
    private TextView mtxtPracticeNote;
    private Button btnSubmitPracticeNote;
    private Spinner spinnerChooseTeacher;

    private ArrayList <String> teacherList;
    private ArrayList <String> teacherIDList;
    private FirebaseAuth mAuth;
    static Dialog dialog ;
    String student_uid;
    String teacherName;
    String teacherIDGet;
    String noteContent;
    String date;
    String duration;
    long dur;
    FirebaseDatabase database;
    protected BottomNavigationView navigationView;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practice_note);

        setTitle("ADD PRACTICE NOTE");

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");


        txtStudentCreatorName = (TextView) findViewById(R.id.txtStudentCreatorName);
        eTxtPracticeNoteDateEnter = (EditText) findViewById(R.id.etxtPracticeNoteDateEnter);
        eTxtPracticeNoteDurationEnter = (EditText) findViewById(R.id.etxtPracticeNoteDurationEnter);
        mtxtPracticeNote = (TextView) findViewById(R.id.mtxtPracticeNote);
        btnSubmitPracticeNote = (Button) findViewById(R.id.btnSubmitPracticeNote);
        spinnerChooseTeacher = (Spinner) findViewById(R.id.spinnerChooseTeacher);

        // add_practiceNote_back = (ImageView)findViewById(R.id.add_practiceNote_back);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

    /*    add_practiceNote_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */

        txtStudentCreatorName.setText(user_id);
        DatabaseReference mRefStudent;
        mRefStudent = database.getReference("Users").child(user_id).child("Name");
        mRefStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String studentName = dataSnapshot.getValue(String.class);
                txtStudentCreatorName.setText(studentName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        teacherList = new ArrayList<String>();
        teacherIDList = new ArrayList<String>();
        teacherList.add("None");
        teacherIDList.add("None");
        final ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teacherList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseTeacher.setAdapter(arrayAdapter);
        DatabaseReference mRefTeacherID;
        mRefTeacherID = database.getReference("Users").child(user_id).child("Teacher");
        mRefTeacherID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String teacherID = postSnapshot.getValue(String.class);
                    teacherIDList.add(teacherID);

                    DatabaseReference mRefTeacher;
                    mRefTeacher = database.getReference("Users").child(teacherID).child("Name");
                    mRefTeacher.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            teacherList.add(dataSnapshot.getValue(String.class));
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinnerChooseTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teacherName = adapterView.getSelectedItem().toString();
                teacherIDGet = teacherIDList.get(adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        eTxtPracticeNoteDateEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDlg();
            }
        });

        eTxtPracticeNoteDurationEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNumberPickDlg();
            }
        });

        btnSubmitPracticeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noteContent = mtxtPracticeNote.getText().toString();
                date = eTxtPracticeNoteDateEnter.getText().toString();
                duration = eTxtPracticeNoteDurationEnter.getText().toString();

                String str = date;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                Date date1= null;
                try {
                    date1 = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long epoch = -1 * date1.getTime();

                DatabaseReference mRef;
                mRef = database.getReference("PracticeNote");
                DatabaseReference newPracticeNote = mRef.push();

                newPracticeNote.child("Date").setValue(date);
                newPracticeNote.child("Duration").setValue(duration);
                newPracticeNote.child("Student").setValue(user_id);
                newPracticeNote.child("Content").setValue(noteContent);
                newPracticeNote.child("TimeStamp").setValue(epoch);
                newPracticeNote.child("dur").setValue(dur);
                if(!teacherName.equals("None")){
                    newPracticeNote.child("Teacher").setValue(teacherIDGet);
                }else{
                    newPracticeNote.child("Teacher").setValue("None");
                }
                Intent myIntent = new Intent(view.getContext(), DisplayPracticeNoteStudentActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

            }
        });


        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationView.postDelayed(() -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_home) {
                        startActivity(new Intent(AddPracticeNoteActivity.this, MainActivity.class));
                    } else if (itemId == R.id.navigation_booking) {
                        startActivity(new Intent(AddPracticeNoteActivity.this, Facitilies_BookingActivity.class));
                    } else if (itemId == R.id.navigation_game) {
                        startActivity(new Intent(AddPracticeNoteActivity.this, jonchan.musex.MainActivity.class));
                    } else if (itemId == R.id.navigation_contact){
                        startActivity(new Intent(AddPracticeNoteActivity.this, DisplayStudentActivity.class));
                    }
                    finish();
                }, 300);
                return true;
            }
        });

        updateNavigationBarState();

    }


    protected void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }
    int getContentViewId(){
        return R.layout.activity_add_practice_note;
    };

    int getNavigationMenuItemId(){
        return R.id.navigation_home;
    };
    protected void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPracticeNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int newMonthOfYear = monthOfYear + 1;
                if(newMonthOfYear < 10 && dayOfMonth < 10){
                   eTxtPracticeNoteDateEnter.setText(year + "-0" + newMonthOfYear + "-0" + dayOfMonth);
                }else if(newMonthOfYear >= 10 && dayOfMonth < 10){
                    eTxtPracticeNoteDateEnter.setText(year + "-" + newMonthOfYear + "-0" + dayOfMonth);
                }else if(newMonthOfYear < 10 && dayOfMonth >= 10){
                    eTxtPracticeNoteDateEnter.setText(year + "-0" + newMonthOfYear + "-" + dayOfMonth);
                }else {
                    eTxtPracticeNoteDateEnter.setText(year + "-" + newMonthOfYear + "-" + dayOfMonth);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    protected void showNumberPickDlg(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddPracticeNoteActivity.this);
        dialogBuilder.setTitle("Duration");

        LayoutInflater inflater = AddPracticeNoteActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        dialogBuilder.setView(dialogView);



        final NumberPicker numberPickerHour = (NumberPicker) dialogView.findViewById(R.id.numberPickerHour);
        final NumberPicker numberPickerMinute = (NumberPicker) dialogView.findViewById(R.id.numberPickerMinute);

        dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                eTxtPracticeNoteDurationEnter.setText(String.valueOf(numberPickerHour.getValue()) + " hour " + numberPickerMinute.getValue() + " minute");
                numberPickerHour.setValue(numberPickerHour.getValue());
                numberPickerMinute.setValue(numberPickerMinute.getValue());
                dur = Long.valueOf(numberPickerHour.getValue() * 60 + numberPickerMinute.getValue());

                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        numberPickerHour.setMaxValue(24);
        numberPickerHour.setMinValue(0);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setMinValue(0);

        numberPickerHour.setOnValueChangedListener(this);
        numberPickerMinute.setOnValueChangedListener(this);

        dialogBuilder.create();
        dialogBuilder.show();

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                Intent myIntent = new Intent(this, LoginActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
