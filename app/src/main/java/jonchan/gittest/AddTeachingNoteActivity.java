package jonchan.gittest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AddTeachingNoteActivity extends AppCompatActivity {

    private TextView txtStudentName;
    private TextView txtLessonGet;
    private TextView mtxtNote;
    private Button btnSubmitNote;
    private FirebaseAuth mAuth;
    String student_uid;
    String lessonDate;
    String lessonID;

    FirebaseDatabase database;
    DatabaseReference mRefStudentName;
    DatabaseReference mRefNote;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teaching_note);

        txtLessonGet = (TextView) findViewById(R.id.txtLessonGet);
        txtStudentName = (TextView) findViewById(R.id.txtStudentName);
        mtxtNote = (TextView) findViewById(R.id.mtxtNote);
        btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");
        lessonDate = myIntent.getStringExtra("DATE");
        lessonID = myIntent.getStringExtra("LESSON_ID");

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRefStudentName = database.getReference("Users").child(student_uid).child("Name");
        mRefStudentName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String studentName = dataSnapshot.getValue(String.class);
                txtStudentName.setText(studentName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtLessonGet.setText(lessonDate);

        mRefNote = database.getReference("Lesson").child(lessonID);
        mRefNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Note")){
                    String noteContent = dataSnapshot.child("Note").getValue().toString();
                    mtxtNote.setText(noteContent, TextView.BufferType.EDITABLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSubmitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String note = mtxtNote.getText().toString();
                Log.d("LessonID",lessonID);
                mRefNote = database.getReference("Lesson").child(lessonID);
                mRefNote.child("Note").setValue(note);

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
}
