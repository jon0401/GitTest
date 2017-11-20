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

public class DisplayPracticeNoteStudentActivity extends AppCompatActivity {

    private Button btnAddPracticeNote;
    private ListView mListView;
    private ArrayList<String> mPracticeNoteList = new ArrayList<>();
    private ArrayList <String> mPracticeNoteIDList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    String lesson_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_practice_note_student);

        //Intent myIntent = getIntent();
        //student_uid = myIntent.getStringExtra("STUDENT_ID");

        btnAddPracticeNote = (Button) findViewById(R.id.btnAddPracticeNote);
        mListView = (ListView) findViewById(R.id.listViewPracticeNote);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("PracticeNote");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.practicenotelist_row, mPracticeNoteList);
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
                    if(postSnapshot.hasChild("Student") && postSnapshot.hasChild("Date") && postSnapshot.hasChild("Content") && postSnapshot.hasChild("Teacher")){
                        if((postSnapshot.child("Student").getValue().toString()).equals(user_id)){

                            String id = postSnapshot.getKey();
                            String date = postSnapshot.child("Date").getValue(String.class);
                            mPracticeNoteList.add(date);
                            mPracticeNoteIDList.add(id);
                            listAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAddPracticeNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddPracticeNoteActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

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
                viewHolder.practiceNoteDate = (TextView) convertView.findViewById(R.id.txtPracticeNote);
                viewHolder.viewPracticeNote = (Button) convertView.findViewById(R.id.btnViewPracticeNote);
                viewHolder.viewPracticeNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), UpdatePracticeNoteActivity.class);
                        myIntent.putExtra("PracticeNote_Date", mPracticeNoteList.get(position));
                        myIntent.putExtra("PracticeNote_ID", mPracticeNoteIDList.get(position));
                        Log.d("PracticeNote_Date", mPracticeNoteList.get(position));
                        Log.d("PracticeNote_ID", mPracticeNoteIDList.get(position));
                        try {
                            startActivity(myIntent);
                        } catch (android.content.ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                convertView.setTag(viewHolder);

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.practiceNoteDate.setText(getItem(position));
                /*DatabaseReference mRefNote = database.getReference("Lesson").child(mPracticeNoteList.get(position));
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
                });*/

            }
            return convertView;
        }
    }

    public class ViewHolder {

        TextView practiceNoteDate;
        Button viewPracticeNote;
    }
}
