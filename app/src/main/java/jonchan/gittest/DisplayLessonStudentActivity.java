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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayLessonStudentActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mLessonList = new ArrayList<>();
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

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    if(postSnapshot.hasChild("Teacher") && postSnapshot.hasChild("Student") && postSnapshot.hasChild("Date")){
                        if((postSnapshot.child("Student").getValue().toString()).equals(user_id)){
                            Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                            Log.d("Student", postSnapshot.child("Student").getValue().toString());
                            String id = postSnapshot.getKey();
                            String date = postSnapshot.child("Date").getValue(String.class);
                            mLessonList.add(date);
                            mLessonIdList.add(id);
                            listAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<String> {

        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.lessonNameStudent = (TextView) convertView.findViewById(R.id.txtLessonStudent);
                viewHolder.btnTeachingNote = (Button) convertView.findViewById(R.id.btnTeachingNote);
                viewHolder.btnTeachingNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));

                        Intent myIntent = new Intent(view.getContext(), DisplayTeachingNoteStudentActivity.class);
                        //myIntent.putExtra("STUDENT_ID", student_uid);
                        myIntent.putExtra("DATE", mLessonList.get(position));
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
                mainViewHolder.lessonNameStudent.setText(getItem(position));

            }
            return convertView;
        }
    }

    public class ViewHolder {

        TextView lessonNameStudent;
        Button btnTeachingNote;
    }
}


