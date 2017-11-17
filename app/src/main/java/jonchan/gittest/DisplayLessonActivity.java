package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
    private ArrayList <String> mLessonList = new ArrayList<>();
    private ArrayList <String> mLessonIdList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    String student_uid;
    String lesson_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylesson);

        Intent myIntent = getIntent();
        student_uid = myIntent.getStringExtra("STUDENT_ID");

        btnAddLesson = (Button) findViewById(R.id.btnAddLesson);
        mListView = (ListView) findViewById(R.id.listview);
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLessonDetail);
        //mListView.setAdapter(arrayAdapter);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Lesson");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.lessonlist_row, mLessonList);
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
                        Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                        Log.d("Student", postSnapshot.child("Student").getValue().toString());
                        if((postSnapshot.child("Teacher").getValue().toString()).equals(user_id) && (postSnapshot.child("Student").getValue().toString()).equals(student_uid)){

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


    private class MyListAdapter extends ArrayAdapter<String>{


        private int layout;
        private MyListAdapter(Context context, int resource, List <String> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.lessonName = (TextView) convertView.findViewById(R.id.txtLesson);
                viewHolder.btnNote = (Button) convertView.findViewById(R.id.btnNote);
                viewHolder.btnNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), AddTeachingNoteActivity.class);
                        myIntent.putExtra("STUDENT_ID", student_uid);
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
                mainViewHolder.lessonName.setText(getItem(position));
                DatabaseReference mRefNote = database.getReference("Lesson").child(mLessonIdList.get(position));
                final ViewHolder finalMainViewHolder = mainViewHolder;
                mRefNote.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Note") && !(dataSnapshot.child("Note").getValue().toString()).equals("")) {
                            finalMainViewHolder.btnNote.setText("UPDATE NOTE");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {

        TextView lessonName;
        Button btnNote;
    }
}



