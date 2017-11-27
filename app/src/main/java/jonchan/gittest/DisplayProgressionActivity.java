package jonchan.gittest;

import android.content.Intent;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DisplayProgressionActivity extends BaseActivity {

    private Button btnProgressionPracticeNoteTeacher;
    private TextView txtPracticeDuration;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    Query query;
    String student_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_progression);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");


        setTitle("PROGRESS");

        btnProgressionPracticeNoteTeacher = (Button) findViewById(R.id.btnProgressionPracticeNoteTeacher);
        txtPracticeDuration = (TextView) findViewById(R.id.txtPracticeDuration);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();


        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("PracticeNote");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date currentDate = Calendar.getInstance().getTime();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date dateWeekBefore = calendar.getTime();
        String dateBefore = dateFormat.format(dateWeekBefore);
        String dateCurrent = dateFormat.format(currentDate);

        query = mRef.orderByChild("Date").startAt(dateBefore).endAt(dateCurrent);
        Log.d("date before",dateBefore);
        Log.d("date current", dateCurrent);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    if(postSnapshot.child("Student").getValue(String.class).equals(student_uid) && postSnapshot.child("Teacher").getValue(String.class).equals(user_id)){

                        long dur = (long) postSnapshot.child("dur").getValue();
                        count += dur;

                    }
                }
                Log.d("total", String.valueOf(count));
                String hour = String.valueOf(count/60);
                String minute = String.valueOf(count%60);
                txtPracticeDuration.setText("Practised " + hour + " hours " + minute + " minutes in the past 7 days!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




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

    @Override
    int getContentViewId() {
        return R.layout.activity_display_progression;
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
