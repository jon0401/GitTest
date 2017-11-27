package jonchan.gittest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mRequestList = new ArrayList<>();
    private ArrayList <String> mRequestIDList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference mRefTeacherID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        //Intent myIntent = getIntent();
        //student_uid = myIntent.getStringExtra("STUDENT_ID");
        mListView = (ListView) findViewById(R.id.listViewRequest);

        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        mRefTeacherID = database.getReference("Request");
        final MyListAdapter listAdapter = new MyListAdapter(this, R.layout.requestlist_row, mRequestList);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mRefTeacherID.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user_id)){

                    final String teacherIDThatSentRequest = dataSnapshot.getValue(String.class);
                    DatabaseReference mRefTeacherName;
                    mRefTeacherName = database.getReference().child("Users").child(teacherIDThatSentRequest).child("Name");
                    mRefTeacherName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String teacherName = dataSnapshot.getValue(String.class);
                            mRequestList.add(teacherName);
                            mRequestIDList.add(teacherIDThatSentRequest);
                            listAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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

    private class MyListAdapter extends ArrayAdapter<String> {

        final String user_id = mAuth.getCurrentUser().getUid();
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
                viewHolder.txtTeacher = (TextView) convertView.findViewById(R.id.txtTeacher);
                viewHolder.btnAccept = (Button) convertView.findViewById(R.id.btnAccept);
                viewHolder.btnDecline = (Button) convertView.findViewById(R.id.btnDecline);
                viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent().getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));

                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewRequestActivity.this);
                        alert.setTitle("Confirmation");
                        alert.setMessage("Are you sure you want to add " + mRequestList.get(position) + " as teacher?");

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                final DatabaseReference mRefAddTeacher;
                                mRefAddTeacher = database.getReference().child("Users").child(user_id).child("Teacher").push();
                                mRefAddTeacher.setValue(mRequestIDList.get(position));
                                final DatabaseReference mRefAddStudent;
                                mRefAddStudent = database.getReference().child("Users").child(mRequestIDList.get(position)).child("Student").push();
                                mRefAddStudent.setValue(user_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mRefTeacherID.child(user_id).removeValue();
                                    }
                                });

                                Intent myIntent = new Intent(ViewRequestActivity.this, MainActivity.class);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try {
                                    startActivity(myIntent);
                                } catch (android.content.ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    }

                });

                viewHolder.btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentRow = (View) view.getParent().getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        final int position = listView.getPositionForView(parentRow);
                        Log.d("Position", String.valueOf(position));

                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewRequestActivity.this);
                        alert.setTitle("Confirmation");
                        alert.setMessage("Are you sure you want to decline the request from " + mRequestList.get(position));

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                mRefTeacherID.child(user_id).removeValue();


                                Intent myIntent = new Intent(ViewRequestActivity.this, MainActivity.class);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try {
                                    startActivity(myIntent);
                                } catch (android.content.ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    }
                });
                convertView.setTag(viewHolder);

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.txtTeacher.setText(getItem(position));

            }
            return convertView;
        }
    }

    public class ViewHolder {

        TextView txtTeacher;
        Button btnAccept;
        Button btnDecline;

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
