package jonchan.gittest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddLessonActivity extends AppCompatActivity {

    private Button mAddBtn;
    private EditText mValueField;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlesson);

        mValueField = (EditText) findViewById(R.id.valueField);
        mAddBtn = (Button) findViewById(R.id.addBtn);

        Intent myIntent = getIntent();
        final String student_uid = myIntent.getStringExtra("STUDENT_ID");

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String teacher_id = mAuth.getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance();
                mRef = database.getReference("Lesson");
                DatabaseReference newLesson = mRef.push();
                String date = mValueField.getText().toString();
                newLesson.child("Date").setValue(date);
                newLesson.child("Teacher").setValue(teacher_id);
                newLesson.child("Student").setValue(student_uid);

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
