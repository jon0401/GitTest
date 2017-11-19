package jonchan.gittest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdatePracticeNoteActivity extends AppCompatActivity {

    private TextView txtStudentCreatorName;
    private EditText eTxtPracticeNoteDateEnter;
    private TextView mtxtPracticeNote;
    private Button btnSubmitPracticeNote;
    private Spinner spinnerChooseTeacher;
    private ArrayList <String> teacherList;
    private ArrayList <String> teacherIDList;
    private FirebaseAuth mAuth;
    String teacherName;
    String teacherIDGet;
    String noteContent;
    String practiceNoteID;
    String date;
    int pos;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_practice_note);

        Intent myIntent = getIntent();
        date = myIntent.getStringExtra("PracticeNote_Date");
        practiceNoteID = myIntent.getStringExtra("PracticeNote_ID");


        txtStudentCreatorName = (TextView) findViewById(R.id.txtStudentCreatorName);
        eTxtPracticeNoteDateEnter = (EditText) findViewById(R.id.etxtPracticeNoteDateEnter);
        mtxtPracticeNote = (TextView) findViewById(R.id.mtxtPracticeNote);
        btnSubmitPracticeNote = (Button) findViewById(R.id.btnSubmitPracticeNote);
        spinnerChooseTeacher = (Spinner) findViewById(R.id.spinnerChooseTeacher);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

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

        DatabaseReference mRefDefaultTeacher;
        mRefDefaultTeacher = database.getReference("PracticeNote").child(practiceNoteID);
        mRefDefaultTeacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Teacher").getValue().equals("None")){
                    String teacherID = dataSnapshot.child("Teacher").getValue(String.class);
                    DatabaseReference mRefDefaultTeacherName;
                    mRefDefaultTeacherName = database.getReference("Users").child(teacherID).child("Name");
                    mRefDefaultTeacherName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String teacherName = dataSnapshot.getValue(String.class);
                            Log.d("TeacherName",teacherName);
                            pos = arrayAdapter.getPosition(teacherName);
                            Log.v("pos",String.valueOf(pos));
                            spinnerChooseTeacher.setSelection(pos);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    pos = 0;
                    Log.v("pos",String.valueOf(pos));
                    spinnerChooseTeacher.setSelection(pos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference mRefDate;
        mRefDate = database.getReference("PracticeNote").child(practiceNoteID).child("Date");
        mRefDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dateGet = dataSnapshot.getValue(String.class);
                eTxtPracticeNoteDateEnter.setText(dateGet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mRefContent;
        mRefContent = database.getReference("PracticeNote").child(practiceNoteID).child("Content");
        mRefContent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String contentGet = dataSnapshot.getValue(String.class);
                mtxtPracticeNote.setText(contentGet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSubmitPracticeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noteContent = mtxtPracticeNote.getText().toString();
                date = eTxtPracticeNoteDateEnter.getText().toString();
                DatabaseReference mRef;
                mRef = database.getReference("PracticeNote").child(practiceNoteID);

                mRef.child("Date").setValue(date);
                mRef.child("Student").setValue(user_id);
                mRef.child("Content").setValue(noteContent);
                if(!teacherName.equals("None")){
                    mRef.child("Teacher").setValue(teacherIDGet);
                }else{
                    mRef.child("Teacher").setValue("None");
                }
                Intent myIntent = new Intent(view.getContext(), DisplayPracticeNoteStudentActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

            }
        });


    }
}
