package jonchan.gittest;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AddTeachingNoteActivity extends BaseActivity {

    private TextView txtStudentName;
    private TextView txtLessonGet;
    private TextView mtxtNote;

    private Button btnSubmitNote;

    private TextView txtStudent;
    private TextView txtLessonDate;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;

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

        setTitle("ADD TEACHING NOTE");
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);


        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");

        txtLessonGet = (TextView) findViewById(R.id.txtLessonGet);
        txtStudentName = (TextView) findViewById(R.id.txtStudentCreatorName);
        mtxtNote = (TextView) findViewById(R.id.mtxtNote);
        btnSubmitNote = (Button) findViewById(R.id.btnSubmitNote);
        txtStudent = (TextView) findViewById(R.id.txtStudent);
        txtLessonDate =  (TextView) findViewById(R.id.txtLessonDate);

        txtLessonGet.setTypeface(tfml);
        txtStudentName.setTypeface(tfml);
        txtStudent.setTypeface(tfml);
        txtLessonDate.setTypeface(tfml);
        btnSubmitNote.setTypeface(tfml);
        mtxtNote.setTypeface(tfrm);


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

                finish();
            }
        });



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

    @Override
    int getContentViewId() {
        return R.layout.activity_add_teaching_note;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }
}
