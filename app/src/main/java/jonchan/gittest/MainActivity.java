package jonchan.gittest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    DatabaseReference mRef;
    Query queryT;
    private ListView mListView;
    private Button btnLogout;
    private Button btnDisplayStudent;
    private Button btnMyLesson;
    private Button btnPracticeNote;
    private Button btnViewRequest;
    private Button btnBooking;
    private TextView upcomingteacher;
    private TextView recentteacher;
    private TextView upcomingstudent;
    private TextView recentstudent;

    private Typeface tfrb;
    private Typeface tfrm;

    private MenuItem item;

    private Button btnGame;
    private ArrayList<LessonDetail> mUpcomingTeacherList = new ArrayList<>();
    private ArrayList <String> mLessonIdList = new ArrayList<>();
    private ArrayList <String> mStudentUIDList = new ArrayList<>();

    private String value;

    private BottomNavigationView navigationView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("HOME");
        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){  //has not logined
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try{
                        startActivity(myIntent);
                    }catch(android.content.ActivityNotFoundException e){
                        e.printStackTrace();
                    }

                }else{ //logined ?


                    final String user_id = mAuth.getCurrentUser().getUid();
                    Log.d("user_id",user_id);
                    database = FirebaseDatabase.getInstance();
                    mRef = database.getReference("Users").child(user_id).child("UserType");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mRef = database.getReference("Lesson");

                            value = dataSnapshot.getValue(String.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            Date currentDate = Calendar.getInstance().getTime();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DAY_OF_YEAR, 14);
                            Date dateTwoWeeksAfter = calendar.getTime();
                            String dateAfter = dateFormat.format(dateTwoWeeksAfter);
                            String dateCurrent = dateFormat.format(currentDate);
                            Log.d("dateAfter",dateAfter);
                            Log.d("dateCurrent", dateCurrent);
                            Date dateA= null;
                            try {
                                dateA = dateFormat.parse(dateAfter);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long epochA = dateA.getTime();
                            Log.d("epochA", String.valueOf(epochA));

                            Date dateC= null;
                            try {
                                dateC = dateFormat.parse(dateCurrent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long epochC = dateC.getTime();
                            Log.d("epochC", String.valueOf(epochC));


                            queryT = mRef.orderByChild("TimeStampAsc").startAt(epochC).endAt(epochA);


                            if(value.equals("Teacher")){
                                //teacher homepage

                                setContentView(R.layout.activity_teacher_home_page);


                                navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                                navigationView.setOnNavigationItemSelectedListener(item -> {
                                    navigationView.postDelayed(() -> {
                                        int itemId = item.getItemId();
                                        if (itemId == R.id.navigation_home) {
                                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                                        } else if (itemId == R.id.navigation_booking) {
                                            startActivity(new Intent(MainActivity.this, Facitilies_BookingActivity.class));
                                        } else if (itemId == R.id.navigation_game) {
                                            startActivity(new Intent(MainActivity.this, jonchan.musex.MainActivity.class));
                                        } else if (itemId == R.id.navigation_contact){
                                            startActivity(new Intent(MainActivity.this, DisplayStudentActivity.class));
                                        }
                                        finish();
                                    }, 300);
                                    return true;
                                });

                                updateNavigationBarState();

                                upcomingteacher = (TextView) findViewById(R.id.UpcomingLessonteacher);
                                recentteacher = (TextView) findViewById(R.id.RecentNoteTeacher);

                                upcomingteacher.setTypeface(tfrb);
                                recentteacher.setTypeface(tfrb);

                                Log.d("UserType", value);

                                mListView = (ListView) findViewById(R.id.listUpcomingLesson);


                                final MyListAdapter listAdapter = new MyListAdapter(MainActivity.this, R.layout.mainpage_lesson_list_row_teacher, mUpcomingTeacherList);
                                mListView.setAdapter(listAdapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    }
                                });

                                queryT.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if(dataSnapshot.hasChild("Teacher") && dataSnapshot.hasChild("Student") && dataSnapshot.hasChild("Date") && dataSnapshot.hasChild("StartTime")&& dataSnapshot.hasChild("EndTime")&& dataSnapshot.hasChild("Location")&& dataSnapshot.hasChild("TimeStamp")&& dataSnapshot.hasChild("TimeStampAsc")){
                                            //Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                                            //Log.d("Student", postSnapshot.child("Student").getValue().toString());
                                            if((dataSnapshot.child("Teacher").getValue().toString()).equals(user_id)){

                                                final String id = dataSnapshot.getKey();
                                                final String date = dataSnapshot.child("Date").getValue(String.class);
                                                final String startTime = dataSnapshot.child("StartTime").getValue(String.class);
                                                final String endTime = dataSnapshot.child("EndTime").getValue(String.class);
                                                final String location = dataSnapshot.child("Location").getValue(String.class);
                                                String studentUID = dataSnapshot.child("Student").getValue(String.class);
                                                mStudentUIDList.add(studentUID);
                                                DatabaseReference mRefStudent;
                                                mRefStudent = database.getReference("Users").child(studentUID).child("Name");
                                                mRefStudent.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String studentName = dataSnapshot.getValue(String.class);
                                                        Log.d("DT", date + " " + startTime);
                                                        mUpcomingTeacherList.add(new LessonDetail(date, startTime, endTime, location, studentName));
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


                            }else{

                                //student homepage

                                setContentView(R.layout.activity_student_home_page);
                                navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                                navigationView.setOnNavigationItemSelectedListener(item -> {
                                    navigationView.postDelayed(() -> {
                                        int itemId = item.getItemId();
                                        if (itemId == R.id.navigation_home) {
                                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                                        } else if (itemId == R.id.navigation_booking) {
                                            startActivity(new Intent(MainActivity.this, Facitilies_BookingActivity.class));
                                        } else if (itemId == R.id.navigation_game) {
                                            startActivity(new Intent(MainActivity.this, jonchan.musex.MainActivity.class));
                                        } else if (itemId == R.id.navigation_contact){
                                            startActivity(new Intent(MainActivity.this, DisplayStudentActivity.class));
                                        }
                                        finish();
                                    }, 300);
                                    return true;
                                });

                                updateNavigationBarState();
                                btnMyLesson = (Button) findViewById(R.id.btnMyLesson);
                                btnLogout = (Button) findViewById(R.id.btnLogoutStudent);
                                btnPracticeNote = (Button) findViewById(R.id.btnPracticeNote);
                                btnViewRequest = (Button) findViewById(R.id.btnViewRequest);
                                btnBooking = (Button) findViewById(R.id.btnBookRoomStudent);
                                btnGame = (Button) findViewById(R.id.btnMusicGameStudent);
                                mListView = (ListView) findViewById(R.id.listUpcomingLesson);

                                final MyListAdapterStudent listAdapter = new MyListAdapterStudent (MainActivity.this, R.layout.mainpage_lesson_list_row_student, mUpcomingTeacherList);
                                mListView.setAdapter(listAdapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    }
                                });

                                queryT.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if(dataSnapshot.hasChild("Teacher") && dataSnapshot.hasChild("Student") && dataSnapshot.hasChild("Date") && dataSnapshot.hasChild("StartTime")&& dataSnapshot.hasChild("EndTime")&& dataSnapshot.hasChild("Location")&& dataSnapshot.hasChild("TimeStamp")&& dataSnapshot.hasChild("TimeStampAsc")){
                                            //Log.d("Teacher", postSnapshot.child("Teacher").getValue().toString());
                                            //Log.d("Student", postSnapshot.child("Student").getValue().toString());
                                            if((dataSnapshot.child("Student").getValue().toString()).equals(user_id)){

                                                final String id = dataSnapshot.getKey();
                                                final String date = dataSnapshot.child("Date").getValue(String.class);
                                                final String startTime = dataSnapshot.child("StartTime").getValue(String.class);
                                                final String endTime = dataSnapshot.child("EndTime").getValue(String.class);
                                                final String location = dataSnapshot.child("Location").getValue(String.class);
                                                String studentUID = dataSnapshot.child("Teacher").getValue(String.class);
                                                mStudentUIDList.add(studentUID);
                                                DatabaseReference mRefStudent;
                                                mRefStudent = database.getReference("Users").child(studentUID).child("Name");
                                                mRefStudent.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String studentName = dataSnapshot.getValue(String.class);
                                                        Log.d("DT", date + " " + startTime);
                                                        mUpcomingTeacherList.add(new LessonDetail(date, startTime, endTime, location, studentName));
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

                                btnMyLesson.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), DisplayLessonStudentActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mUpcomingTeacherList.clear();
                                        mLessonIdList.clear();
                                        mStudentUIDList.clear();
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                btnPracticeNote.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), DisplayPracticeNoteStudentActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mUpcomingTeacherList.clear();
                                        mLessonIdList.clear();
                                        mStudentUIDList.clear();
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                btnLogout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //mAuth = FirebaseAuth.getInstance();
                                        mAuth.signOut();
                                        Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                btnViewRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), ViewRequestActivity.class);
                                        mUpcomingTeacherList.clear();
                                        mLessonIdList.clear();
                                        mStudentUIDList.clear();
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                btnBooking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), Facitilies_BookingActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mUpcomingTeacherList.clear();
                                        mLessonIdList.clear();
                                        mStudentUIDList.clear();
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                btnGame.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent myIntent = new Intent (v.getContext(), jonchan.musex.MainActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mUpcomingTeacherList.clear();
                                        mLessonIdList.clear();
                                        mStudentUIDList.clear();
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            }
        };


    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }




    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    protected void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    int getNavigationMenuItemId(){
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
                mainViewHolder.txtDate= (TextView) convertView.findViewById(R.id.txtDate);
                mainViewHolder.txtTimeSlot = (TextView) convertView.findViewById(R.id.txtTimeSlot);
                mainViewHolder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
                mainViewHolder.txtStudent = (TextView) convertView.findViewById(R.id.txtStudent);
                mainViewHolder.btnAddNote = (Button) convertView.findViewById(R.id.btnAddNote);
                mainViewHolder.btnAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), AddTeachingNoteActivity.class);
                        myIntent.putExtra("STUDENT_ID", mStudentUIDList.get(position));
                        myIntent.putExtra("DATE", mUpcomingTeacherList.get(position).getDate());
                        myIntent.putExtra("LESSON_ID", mLessonIdList.get(position));
                        mUpcomingTeacherList.clear();
                        mLessonIdList.clear();
                        mStudentUIDList.clear();
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            mainViewHolder.txtStudent.setText(getItem(position).getStudentName());

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
        TextView txtStudent;
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

    private class MyListAdapterStudent extends ArrayAdapter<LessonDetail> {

        private int layout;
        private MyListAdapterStudent(Context context, int resource, List<LessonDetail> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolderStudent mainViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                mainViewHolder = new ViewHolderStudent();
                mainViewHolder.txtDate= (TextView) convertView.findViewById(R.id.txtDate);
                mainViewHolder.txtTimeSlot = (TextView) convertView.findViewById(R.id.txtTimeSlot);
                mainViewHolder.txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
                mainViewHolder.txtStudent = (TextView) convertView.findViewById(R.id.txtStudent);

                convertView.setTag(mainViewHolder);
            }else {
                mainViewHolder = (ViewHolderStudent) convertView.getTag();
            }

            mainViewHolder.txtDate.setText(getItem(position).getDate());
            mainViewHolder.txtTimeSlot.setText(getItem(position).getStartTime() + "-" + getItem(position).getEndTime());
            mainViewHolder.txtLocation.setText(getItem(position).getLocation());
            mainViewHolder.txtStudent.setText(getItem(position).getStudentName());

            return convertView;
        }
    }

    public class ViewHolderStudent {

        TextView txtTimeSlot;
        TextView txtDate;
        TextView txtLocation;
        TextView txtStudent;
    }

}