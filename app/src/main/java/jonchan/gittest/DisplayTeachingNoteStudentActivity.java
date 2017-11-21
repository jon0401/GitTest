package jonchan.gittest;

import android.content.Intent;
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

import java.util.ArrayList;

public class DisplayTeachingNoteStudentActivity extends AppCompatActivity {

    private TextView txtTeacherName;
    private TextView txtLessonGetStudent;
    private TextView mtxtNoteStudent;
    private FirebaseAuth mAuth;
    String student_uid;
    String lessonDate;
    String lessonID;

    FirebaseDatabase database;
    DatabaseReference mRefNote;
    DatabaseReference mRefTeacherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_teaching_note_student);

        txtLessonGetStudent = (TextView) findViewById(R.id.txtLessonGetStudent);
        txtTeacherName = (TextView) findViewById(R.id.txtTeacherName);
        mtxtNoteStudent = (TextView) findViewById(R.id.mtxtNoteStudent);

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


}
