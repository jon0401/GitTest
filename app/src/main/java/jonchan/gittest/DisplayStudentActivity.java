package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class DisplayStudentActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mStudentList = new ArrayList<>();
    private ArrayList<String> mStudentUidList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);

        mListView = (ListView) findViewById(R.id.listViewStudent);
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.studentlist_row, mLessonDetail);
        //mListView.setAdapter(arrayAdapter);
        //generateListContent();

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users").child(user_id).child("Student");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.studentlist_row, mStudentList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String value = postSnapshot.getValue(String.class);
                    DatabaseReference mRefName = database.getReference("Users").child(value).child("Name");
                    mRefName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String studentName = dataSnapshot.getValue(String.class);
                            Log.d("String",studentName);
                            mStudentList.add(studentName);
                            listAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mStudentUidList.add(value);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }


    private class MyListAdapter extends ArrayAdapter<String>{


        private int layout;
        private MyListAdapter(Context context, int resource, List <String> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.studentName = (TextView) convertView.findViewById(R.id.txtStudent);
                viewHolder.btnAddLesson = (Button) convertView.findViewById(R.id.btnLesson);
                viewHolder.btnProgression = (Button) convertView.findViewById(R.id.btnProgression);
                viewHolder.btnAddLesson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position",String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), DisplayLessonActivity.class);
                        myIntent.putExtra("STUDENT_ID", mStudentUidList.get(position));
                        try{
                            startActivity(myIntent);
                        }catch(android.content.ActivityNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                });
                viewHolder.btnProgression.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position",String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), DisplayProgressionActivity.class);
                        myIntent.putExtra("STUDENT_ID", mStudentUidList.get(position));
                        try{
                            startActivity(myIntent);
                        }catch(android.content.ActivityNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                });
                convertView.setTag(viewHolder);

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.studentName.setText(getItem(position));
            }

            return convertView;
        }
    }

    public class ViewHolder {

        TextView studentName;
        Button btnAddLesson;
        Button btnProgression;
    }



}
