package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

public class DisplayLessonActivity extends BaseActivity {

    private Button btnAddLesson;
    private ListView mListView;
    private ArrayList <LessonDetail> mLessonList = new ArrayList<>();
    private ArrayList <String> mLessonIdList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    Query query;
    private FirebaseAuth mAuth;
    String student_uid;
    String lesson_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylesson);
        setTitle("LESSONS");
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");

        btnAddLesson = (Button) findViewById(R.id.btnAddStudent);
        mListView = (ListView) findViewById(R.id.listViewLesson);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Lesson");
        query = mRef.orderByChild("TimeStamp");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.lessonlist_row, mLessonList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.hasChild("Teacher") && postSnapshot.hasChild("Student") && postSnapshot.hasChild("Date")) {
                        //Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                        //Log.d("Student", postSnapshot.child("Student").getValue().toString());
                        if ((postSnapshot.child("Teacher").getValue().toString()).equals(user_id) && (postSnapshot.child("Student").getValue().toString()).equals(student_uid)) {

                            final String id = postSnapshot.getKey();
                            final String date = postSnapshot.child("Date").getValue(String.class);
                            final String startTime = postSnapshot.child("StartTime").getValue(String.class);
                            final String endTime = postSnapshot.child("EndTime").getValue(String.class);
                            final String location = postSnapshot.child("Location").getValue(String.class);
                            String studentUID = postSnapshot.child("Student").getValue(String.class);
                            DatabaseReference mRefStudent;
                            mRefStudent = database.getReference("Users").child(studentUID).child("Name");
                            mRefStudent.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String studentName = dataSnapshot.getValue(String.class);
                                    Log.d("DT", date + " " + startTime);
                                    mLessonList.add(new LessonDetail(date, startTime, endTime, location, studentName));
                                    mLessonIdList.add(id);
                                    listAdapter.notifyDataSetChanged();
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                }

            }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }


        });


        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddLessonActivity.class);
                myIntent.putExtra("STUDENT_ID", student_uid);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mLessonIdList.clear();
                mLessonList.clear();
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
        return R.layout.activity_displaylesson;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_contact;
    }


    private class MyListAdapter extends ArrayAdapter<LessonDetail>{

        private int layout;
        private MyListAdapter(Context context, int resource, List <LessonDetail> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                mainViewHolder = new ViewHolder();
                mainViewHolder.txtDate= (TextView) convertView.findViewById(R.id.txtDate);
                mainViewHolder.txtTimeSlot = (TextView) convertView.findViewById(R.id.txtTimeSlot);
                mainViewHolder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
                mainViewHolder.btnAddNote = (Button) convertView.findViewById(R.id.btnAddNote);
                mainViewHolder.btnAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), AddTeachingNoteActivity.class);
                        myIntent.putExtra("STUDENT_ID", student_uid);
                        myIntent.putExtra("DATE", mLessonList.get(position).getDate());
                        myIntent.putExtra("LESSON_ID", mLessonIdList.get(position));
                       // mLessonIdList.clear();
                        //mLessonList.clear();
                        try {
                            startActivity(myIntent);
                        } catch (android.content.ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });


                convertView.setTag(mainViewHolder);
            }else {
                mainViewHolder = (ViewHolder) convertView.getTag();
            }

            mainViewHolder.txtDate.setText(getItem(position).getDate());
            mainViewHolder.txtTimeSlot.setText(getItem(position).getStartTime() + "-" + getItem(position).getEndTime());
            mainViewHolder.txtLocation.setText(getItem(position).getLocation());

            DatabaseReference mRefNote = database.getReference("Lesson").child(mLessonIdList.get(position));
            final ViewHolder finalMainViewHolder = mainViewHolder;
            mRefNote.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Note") && !(dataSnapshot.child("Note").getValue().toString()).equals("")) {
                        finalMainViewHolder.btnAddNote.setText("UPDATE NOTE");
                    } else{
                        finalMainViewHolder.btnAddNote.setText("ADD NOTE");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return convertView;
        }
    }

    public class ViewHolder {

        TextView txtTimeSlot;
        TextView txtDate;
        TextView txtLocation;
        Button btnAddNote;
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



