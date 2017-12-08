package jonchan.gittest;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewPracticeNoteTeacherActivity extends BaseActivity {

    private TextView txtStudentCreatorName;
    private TextView eTxtPracticeNoteDateEnter;
    private TextView mtxtPracticeNote;
    private TextView eTxtPracticeNoteDurationEnter;

    private TextView txttile;
    private TextView txtName;
    private TextView txtDuration;
    private TextView txtDate;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;

    private ImageView add_practiceNote_back;
    private FirebaseAuth mAuth;
    String student_uid;
    String practiceNoteID;
    String practiceNoteDate;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_practice_note_teacher);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");
        practiceNoteID = myIntent.getStringExtra("PracticeNote_ID");
        practiceNoteDate = myIntent.getStringExtra("PracticeNote_Date");

        setTitle("VIEW PRACTICE NOTE");

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");


        txtStudentCreatorName = (TextView) findViewById(R.id.txtStudentCreatorName);
        eTxtPracticeNoteDateEnter = (TextView) findViewById(R.id.etxtPracticeNoteDateEnter);
        eTxtPracticeNoteDurationEnter = (TextView) findViewById(R.id.etxtPracticeNoteDurationEnter);
        mtxtPracticeNote = (TextView) findViewById(R.id.mtxtPracticeNote);

        txttile = (TextView) findViewById(R.id.add_practiceNote_title);
        txtName = (TextView) findViewById(R.id.txtStudentCreator);
        txtDuration = (TextView) findViewById(R.id.txtPracticeNoteDate);
        txtDate = (TextView) findViewById(R.id.txtPracticeDurationL);

        mtxtPracticeNote.setTypeface(tfrm);

        txtStudentCreatorName.setTypeface(tfml);
        eTxtPracticeNoteDateEnter.setTypeface(tfml);
        eTxtPracticeNoteDurationEnter.setTypeface(tfml);
        txttile.setTypeface(tfmsb);
        txtName.setTypeface(tfml);
        txtDuration.setTypeface(tfml);
        txtDate.setTypeface(tfml);
        //    add_practiceNote_back = (ImageView)findViewById(R.id.add_practiceNote_back);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

   //     add_practiceNote_back.setOnClickListener(new View.OnClickListener() {
     //       @Override
       //     public void onClick(View view) {
         //       finish();
          //  }
        // });

        DatabaseReference mRefStudent;
        mRefStudent = database.getReference("Users").child(student_uid).child("Name");
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

        DatabaseReference mRefDate;
        mRefDate = database.getReference("PracticeNote").child(practiceNoteID).child("Date");
        mRefDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = dataSnapshot.getValue(String.class);
                eTxtPracticeNoteDateEnter.setText(date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mRefDuration;
        mRefDuration = database.getReference("PracticeNote").child(practiceNoteID).child("Duration");
        mRefDuration.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String duration = dataSnapshot.getValue(String.class);
                eTxtPracticeNoteDurationEnter.setText(duration);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mRefNote;
        mRefNote = database.getReference("PracticeNote").child(practiceNoteID).child("Content");
        mRefNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String content = dataSnapshot.getValue(String.class);
                mtxtPracticeNote.setText(content);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    int getContentViewId() {
        return R.layout.activity_view_practice_note_teacher;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_contact;
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
