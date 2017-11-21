package jonchan.gittest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayProgressionActivity extends AppCompatActivity {

    private Button btnProgressionPracticeNoteTeacher;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    String student_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_progression);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

        btnProgressionPracticeNoteTeacher = (Button) findViewById(R.id.btnProgressionPracticeNoteTeacher);
        btnProgressionPracticeNoteTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), DisplayPracticeNoteTeacherActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
