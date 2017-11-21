package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayLessonStudentActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<LessonDetail> mLessonList = new ArrayList<>();
    private ArrayList <String> mLessonIdList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lesson_student);

        //Intent myIntent = getIntent();
        //student_uid = myIntent.getStringExtra("STUDENT_ID");

        mListView = (ListView) findViewById(R.id.listViewLessonStudent);
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLessonDetail);
        //mListView.setAdapter(arrayAdapter);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        Log.d("StudentID", user_id);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Lesson");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.lessonlist_student_row, mLessonList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("Teacher") && dataSnapshot.hasChild("Student") && dataSnapshot.hasChild("Date")){
                    if((dataSnapshot.child("Student").getValue().toString()).equals(user_id)){
                        Log.d("Teacher", dataSnapshot.child("Teacher").getValue().toString());
                        Log.d("Student", dataSnapshot.child("Student").getValue().toString());
                        String id = dataSnapshot.getKey();
                        String date = dataSnapshot.child("Date").getValue(String.class);
                        String startTime = dataSnapshot.child("StartTime").getValue(String.class);
                        String endTime = dataSnapshot.child("EndTime").getValue(String.class);
                        String location = dataSnapshot.child("Location").getValue(String.class);

                        mLessonList.add(new LessonDetail(date, startTime, endTime, location));
                        mLessonIdList.add(id);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private class MyListAdapter extends ArrayAdapter<LessonDetail> {

        private int layout;
        private MyListAdapter(Context context, int resource, List<LessonDetail> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.txtDate= (TextView) convertView.findViewById(R.id.txtDate);
                viewHolder.txtTimeSlot = (TextView) convertView.findViewById(R.id.txtTimeSlot);
                viewHolder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
                viewHolder.btnTeachingNote = (Button) convertView.findViewById(R.id.btnTeachingNote);
                viewHolder.btnTeachingNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));

                        Intent myIntent = new Intent(view.getContext(), DisplayTeachingNoteStudentActivity.class);
                        //myIntent.putExtra("STUDENT_ID", student_uid);
                        myIntent.putExtra("DATE", mLessonList.get(position).getDate());
                        myIntent.putExtra("LESSON_ID", mLessonIdList.get(position));
                        try {
                            startActivity(myIntent);
                        } catch (android.content.ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                convertView.setTag(viewHolder);

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.txtDate.setText(getItem(position).getDate());
                mainViewHolder.txtTimeSlot.setText(getItem(position).getStartTime() + "-" + getItem(position).getEndTime());
                mainViewHolder.txtLocation.setText(getItem(position).getLocation());



            }
            return convertView;
        }
    }

    public class ViewHolder {

        TextView txtTimeSlot;
        TextView txtDate;
        TextView txtLocation;
        Button btnTeachingNote;
    }
}


