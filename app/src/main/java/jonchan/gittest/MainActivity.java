package jonchan.gittest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private Button btnLogout;
    private Button btnDisplayStudent;
    private Button btnMyLesson;
    private Button btnPracticeNote;
    private Button btnViewRequest;
    private Button btnBooking;

    private Button btnGame;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                    mAuth = FirebaseAuth.getInstance();
                    String user_id = mAuth.getCurrentUser().getUid();
                    Log.d("user_id",user_id);
                    database = FirebaseDatabase.getInstance();
                    mRef = database.getReference("Users").child(user_id).child("UserType");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            if(value.equals("Teacher")){
                                //teacher homepage

                                setContentView(R.layout.activity_teacher_home_page);
                                Log.d("UserType", value);
                                btnLogout = (Button) findViewById(R.id.btnLogoutTeacher);
                                btnDisplayStudent = (Button) findViewById(R.id.btnDisplayStudent);
                                btnBooking = (Button) findViewById(R.id.btnBookRoomTeacher);
                                btnGame = (Button) findViewById(R.id.btnMusicGameTeacher);


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

                                btnDisplayStudent.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), DisplayStudentActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                        try{
                                            startActivity(myIntent);
                                        }catch(android.content.ActivityNotFoundException e){
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }else{

                                //student homepage

                                setContentView(R.layout.activity_student_home_page);
                                btnMyLesson = (Button) findViewById(R.id.btnMyLesson);
                                btnLogout = (Button) findViewById(R.id.btnLogoutStudent);
                                btnPracticeNote = (Button) findViewById(R.id.btnPracticeNote);
                                btnViewRequest = (Button) findViewById(R.id.btnViewRequest);
                                btnBooking = (Button) findViewById(R.id.btnBookRoomStudent);
                                btnGame = (Button) findViewById(R.id.btnMusicGameStudent);



                                btnMyLesson.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent myIntent = new Intent(view.getContext(), DisplayLessonStudentActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

}