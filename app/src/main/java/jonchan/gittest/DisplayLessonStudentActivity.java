package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayLessonStudentActivity extends BaseActivity {

    private ListView mListView;
    private ArrayList<LessonDetail> mLessonList = new ArrayList<>();
    private ArrayList <String> mLessonIdList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    Query query;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lesson_student);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        mListView = (ListView) findViewById(R.id.listViewLessonStudent);

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");


        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        Log.d("StudentID", user_id);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Lesson");
        query = mRef.orderByChild("TimeStamp");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.lessonlist_student_row, mLessonList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("Teacher") && dataSnapshot.hasChild("Student") && dataSnapshot.hasChild("Date")){
                    if((dataSnapshot.child("Student").getValue().toString()).equals(user_id)){
                        Log.d("Teacher", dataSnapshot.child("Teacher").getValue().toString());
                        Log.d("Student", dataSnapshot.child("Student").getValue().toString());
                        final String id = dataSnapshot.getKey();
                        final String date = dataSnapshot.child("Date").getValue(String.class);
                        final String startTime = dataSnapshot.child("StartTime").getValue(String.class);
                        final String endTime = dataSnapshot.child("EndTime").getValue(String.class);
                        final String location = dataSnapshot.child("Location").getValue(String.class);
                        String studentUID = dataSnapshot.child("Student").getValue(String.class);
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

    @Override
    int getContentViewId() {
        return R.layout.activity_display_lesson_student;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    private class MyListAdapter extends ArrayAdapter<LessonDetail> {

        private int layout;
        private MyListAdapter(Context context, int resource, List<LessonDetail> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                mainViewHolder = new ViewHolder();
                mainViewHolder.txtMonth= (TextView) convertView.findViewById(R.id.txtMonth);
                mainViewHolder.txtDate= (TextView) convertView.findViewById(R.id.txtDate);
                mainViewHolder.txtTimeSlot = (TextView) convertView.findViewById(R.id.txtTimeSlot);
                mainViewHolder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
                mainViewHolder.btnTeachingNote = (Button) convertView.findViewById(R.id.btnTeachingNote);
                mainViewHolder.btnTeachingNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent().getParent().getParent();
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
                convertView.setTag(mainViewHolder);

            }else {
                mainViewHolder = (ViewHolder) convertView.getTag();
            }

            String month = null;
            switch(getItem(position).getDate().substring(5,7)) {
                case "01":
                    month = "JAN";
                    break;
                case "02":
                    month = "FEB";
                    break;
                case "04":
                    month = "MAR";
                    break;
                case "05":
                    month = "MAY";
                    break;
                case "06":
                    month = "JUN";
                    break;
                case "07":
                    month = "JUL";
                    break;
                case "08":
                    month = "AUG";
                    break;
                case "09":
                    month = "SEP";
                    break;
                case "10":
                    month = "SEP";
                    break;
                case "11":
                    month = "NOV";
                    break;
                case "12":
                    month = "DEC";
                    break;
            }

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.txtMonth.setText(month);
            mainViewHolder.txtDate.setText(getItem(position).getDate().substring(8,10));
            mainViewHolder.txtTimeSlot.setText("TIME: " + getItem(position).getStartTime());
            mainViewHolder.txtLocation.setText("LOCATION: " + getItem(position).getLocation());

            mainViewHolder.txtLocation.setTypeface(tfmsb);
            mainViewHolder.txtMonth.setTypeface(tfmsb);
            mainViewHolder.txtTimeSlot.setTypeface(tfmsb);
            mainViewHolder.txtDate.setTypeface(tfmsb);
            mainViewHolder.btnTeachingNote.setTypeface(tfml);

            mainViewHolder.txtMonth.setTextSize(18);
            mainViewHolder.txtDate.setTextSize(24);


            mainViewHolder.txtMonth.setTextColor(Color.parseColor("#ef4c4b"));
            mainViewHolder.txtDate.setTextColor(Color.parseColor("#000000"));
            return convertView;
        }
    }

    public class ViewHolder {

        TextView txtTimeSlot;
        TextView txtDate;
        TextView txtLocation;
        TextView txtMonth;
        Button btnTeachingNote;
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


