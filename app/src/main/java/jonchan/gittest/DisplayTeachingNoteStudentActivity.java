package jonchan.gittest;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DisplayTeachingNoteStudentActivity extends BaseActivity {

    private TextView txtTeacherName;
    private TextView txtLessonGetStudent;
    private TextView mtxtNoteStudent;
    private TextView txtTeacher;
    private TextView txtLessonDateStudent;

    private FirebaseAuth mAuth;
    String student_uid;
    String lessonDate;
    String lessonID;

    FirebaseDatabase database;
    DatabaseReference mRefNote;
    DatabaseReference mRefTeacherID;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_teaching_note_student);
        setTitle("TEACHING NOTE");

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        txtLessonGetStudent = (TextView) findViewById(R.id.txtLessonGetStudent);
        txtTeacherName = (TextView) findViewById(R.id.txtTeacherName);
        mtxtNoteStudent = (TextView) findViewById(R.id.mtxtNoteStudent);
        txtTeacher = (TextView) findViewById(R.id.txtTeacher);
        txtLessonDateStudent = (TextView) findViewById(R.id.txtLessonDateStudent);

        txtLessonGetStudent.setTypeface(tfml);
        txtTeacherName.setTypeface(tfml);
        txtTeacher.setTypeface(tfml);
        txtLessonDateStudent.setTypeface(tfml);
        mtxtNoteStudent.setTypeface(tfrm);

        Intent myIntent = getIntent();
       // student_uid = myIntent.getStringExtra("STUDENT_ID");
        lessonDate = myIntent.getStringExtra("DATE");
        lessonID = myIntent.getStringExtra("LESSON_ID");

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        mRefTeacherID = database.getReference("Lesson").child(lessonID).child("Teacher");
        mRefTeacherID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String teacherID = dataSnapshot.getValue(String.class);
                DatabaseReference mRefTeacherName;
                mRefTeacherName = database.getReference("Users").child(teacherID).child("Name");
                mRefTeacherName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String teacherName = dataSnapshot.getValue(String.class);
                        Log.d("String",teacherName);
                        txtTeacherName.setText(teacherName);
                    }
                    @Override

                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        txtLessonGetStudent.setText(lessonDate);

        mRefNote = database.getReference("Lesson").child(lessonID);
        mRefNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Note")){
                    String noteContent = dataSnapshot.child("Note").getValue().toString();
                    mtxtNoteStudent.setText(noteContent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_display_teaching_note_student;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }


}
