package jonchan.gittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                String user_id = mAuth.getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance();
                mRef = database.getReference("Users").child(user_id);

                String value = mValueField.getText().toString();

                mRef.child("Lesson").push().setValue(value);
            }
        });



    }
}
