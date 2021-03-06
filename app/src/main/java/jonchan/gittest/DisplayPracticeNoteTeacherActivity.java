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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DisplayPracticeNoteTeacherActivity extends BaseActivity {

    private ListView mListView;
    private ArrayList<String> mPracticeNoteList = new ArrayList<>();
    private ArrayList <String> mPracticeNoteIDList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRef;
    Query query;
    private FirebaseAuth mAuth;
    String student_uid;

    private Typeface tfrb;
    private Typeface tfrm;
    private Typeface tfml;
    private Typeface tfmsb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_practice_note_teacher);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(this);
        Intent myIntent = getIntent();

        student_uid = myIntent.getStringExtra("STUDENT_ID");

        setTitle("ALL PRACTICE NOTE");

        tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
        tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");
        tfml = Typeface.createFromAsset(getAssets(),"montserratlight.ttf");
        tfmsb = Typeface.createFromAsset(getAssets(), "montserratsemibold.ttf");

        mListView = (ListView) findViewById(R.id.listViewPracticeNote);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("PracticeNote");
        query = mRef.orderByChild("TimeStamp");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.practicenotelist_teacher_row, mPracticeNoteList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("Student") && dataSnapshot.hasChild("Date") && dataSnapshot.hasChild("Content") && dataSnapshot.hasChild("Teacher")){
                    if((dataSnapshot.child("Student").getValue().toString()).equals(student_uid) && (dataSnapshot.child("Teacher").getValue().toString()).equals(user_id)){

                        String id = dataSnapshot.getKey();
                        String date = dataSnapshot.child("Date").getValue(String.class);
                        mPracticeNoteList.add(date);
                        mPracticeNoteIDList.add(id);
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

    @Override
    int getContentViewId() {
        return R.layout.activity_display_practice_note_teacher;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_contact;
    }

    private class MyListAdapter extends ArrayAdapter<String> {

        private int layout;
        private MyListAdapter(Context context, int resource, List<String> objects) {
            super (context, resource, objects);
            layout = resource;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                mainViewHolder = new ViewHolder();
                mainViewHolder.practiceNoteDate = (TextView) convertView.findViewById(R.id.txtPracticeNote);
                mainViewHolder.viewPracticeNote = (Button) convertView.findViewById(R.id.btnViewPracticeNote);
                mainViewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

                mainViewHolder.viewPracticeNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent();
                        ListView listView = (ListView) parentRow.getParent().getParent().getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));
                        Intent myIntent = new Intent(view.getContext(), ViewPracticeNoteTeacherActivity.class);
                        myIntent.putExtra("PracticeNote_Date", mPracticeNoteList.get(position));
                        myIntent.putExtra("PracticeNote_ID", mPracticeNoteIDList.get(position));
                        myIntent.putExtra("STUDENT_ID", student_uid);
                        Log.d("PracticeNote_Date", mPracticeNoteList.get(position));
                        Log.d("PracticeNote_ID", mPracticeNoteIDList.get(position));
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
            switch(getItem(position).substring(5,7)) {
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

            mainViewHolder.txtDate.setText(getItem(position).substring(8,10));
            mainViewHolder.practiceNoteDate.setText(month);

            mainViewHolder.practiceNoteDate.setTextSize(18);
            mainViewHolder.txtDate.setTextSize(22);


            mainViewHolder.practiceNoteDate.setTypeface(tfmsb);
            mainViewHolder.txtDate.setTypeface(tfmsb);
            mainViewHolder.viewPracticeNote.setTypeface(tfrb);

            mainViewHolder.txtDate.setTextColor(Color.parseColor("#000000"));
            mainViewHolder.practiceNoteDate.setTextColor(Color.parseColor("#ef4c4b"));

            return convertView;
        }
    }

    public class ViewHolder {
        TextView txtDate;
        TextView practiceNoteDate;
        Button viewPracticeNote;
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
