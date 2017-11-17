package jonchan.gittest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chung on 11/11/2017.
 */

public class DisplayLessonActivity extends AppCompatActivity {

    private Button btnAddLesson;
    private ListView mListView;
    private ArrayList <String> mLessonDetail = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylesson);

        Intent myIntent = getIntent();
        final String student_uid = myIntent.getStringExtra("STUDENT_ID");

        btnAddLesson = (Button) findViewById(R.id.btnAddLesson);
        mListView = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLessonDetail);
        mListView.setAdapter(arrayAdapter);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Lesson");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(postSnapshot.hasChild("Teacher") && postSnapshot.hasChild("Student") && postSnapshot.hasChild("Date")){
                        Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                        Log.d("Student", postSnapshot.child("Student").getValue().toString());
                        if((postSnapshot.child("Teacher").getValue().toString()).equals(user_id) && (postSnapshot.child("Student").getValue().toString()).equals(student_uid)){

                            String value = postSnapshot.child("Date").getValue(String.class);
                            mLessonDetail.add(value);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddLessonActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

            } });
    }
}
