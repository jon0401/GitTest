package jonchan.gittest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;


import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import jonchan.musex.*;

/**
 * Created by jonchan on 27/11/2017.
 */

public class MusicGameActivity extends AppCompatActivity {

    /** firebase **/
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef;
    private String value;
    private BottomNavigationView navigationView;

    /** for teacher **/
    ArrayList<MQues> mExer;  // for both teacher and student
    MExer mexerObject;       // for both teacher and student
    //return stuff!!!!

    int selectedNOQ;
    String mid_selectedDifficulty;
    Difficulty selectedDifficulty;
    ArrayList <QType> selectedQT = new ArrayList<>();
    QType [] selectedQT_arr;

    List<String> studentIDList;
    List<String> studentNameList;
    List<String> selectedStudentList;
    List<String> selectedStudentIDList;

    List<Integer> uploadGameContentList;

    /** for Student **/
    TextView gameScore, gamePercent, gameAT, overAllT, incompleteGT, pointEarnedT, correctPerT, averageTT;
    ImageButton selfPracticeButton;
    boolean getResult = false;
    List<GameItem> allGame;
    ArrayAdapter theAdpater;

    int returnScore;
    int returnCorrectWrongPercentage;
    double returnAverageTime;
    String returnGameID;
    public final int CHILD_RESULT_OBJECT = 1;


    Intent playExerIntent;
    Typeface robotoMed;

    final String TAG = "MyActivity";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("GAME");
        getResult = false;
        robotoMed = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");



        playExerIntent = new Intent(this, jonchan.musex.PlayExer.class);

        //playExerIntent = new Intent();
        //playExerIntent.setClassName("jonchan.musex","jonchan.musex.PlayExer.class");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){  //has not logined

                    Intent myIntent = new Intent(MusicGameActivity.this, LoginActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try{
                        startActivity(myIntent);
                    }catch(android.content.ActivityNotFoundException e){
                        e.printStackTrace();
                    }

                }else{ //logined ?

                    final String user_id = mAuth.getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    mRef = database.getReference("Users").child(user_id).child("UserType");
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mRef = database.getReference("Lesson"); ///???
                            value = dataSnapshot.getValue(String.class);
                            if(value.equals("Teacher")){
                                //teacher
                                setContentView(R.layout.mus_game_activity_main);
                                navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                                navigationView.setOnNavigationItemSelectedListener(item -> {
                                    navigationView.postDelayed(() -> {
                                        int itemId = item.getItemId();
                                        if (itemId == R.id.navigation_home) {
                                            startActivity(new Intent(MusicGameActivity.this, MainActivity.class));
                                        } else if (itemId == R.id.navigation_booking) {
                                            startActivity(new Intent(MusicGameActivity.this, Facitilies_BookingActivity.class));
                                        } else if (itemId == R.id.navigation_game) {
                                            startActivity(new Intent(MusicGameActivity.this, MusicGameActivity.class));
                                        } else if (itemId == R.id.navigation_contact){
                                            startActivity(new Intent(MusicGameActivity.this, DisplayStudentActivity.class));
                                        }
                                        finish();
                                    },10);
                                    return true;
                                });
                                updateNavigationBarState();

                                // Firebase retrieve student list //
                                studentIDList = new ArrayList<>();
                                studentNameList = new ArrayList<>();

                                mAuth = FirebaseAuth.getInstance();
                                database = FirebaseDatabase.getInstance();
                                final String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference studentRefID = database.getReference("Users").child(user_id).child("Student");
                                studentRefID.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            String studentID = snapshot.getValue(String.class);
                                            studentIDList.add(studentID);

                                            DatabaseReference studentNameRef = database.getReference("Users").child(studentID).child("Name");
                                            studentNameRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    studentNameList.add(dataSnapshot.getValue(String.class));
                                                    //Log.d(TAG, dataSnapshot.getValue(String.class) + "Just checking ..............");
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            }else{
                                //student
                                setContentView(R.layout.mus_game_student_activity_main);
                                navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                                navigationView.setOnNavigationItemSelectedListener(item -> {
                                    navigationView.postDelayed(() -> {
                                        int itemId = item.getItemId();
                                        if (itemId == R.id.navigation_home) {
                                            startActivity(new Intent(MusicGameActivity.this, MainActivity.class));
                                        } else if (itemId == R.id.navigation_booking) {
                                            startActivity(new Intent(MusicGameActivity.this, Facitilies_BookingActivity.class));
                                        } else if (itemId == R.id.navigation_game) {
                                            startActivity(new Intent(MusicGameActivity.this, MusicGameActivity.class));
                                        } else if (itemId == R.id.navigation_contact){
                                            startActivity(new Intent(MusicGameActivity.this, DisplayTeacherActivity.class));
                                        }
                                        finish();
                                    }, 10);
                                    return true;
                                });
                                updateNavigationBarState();

                                /** Dummy value for list **/
                                //List<Integer> content = new ArrayList<>();
                                /*
                                for (int i = 1001; i < 1011; i ++){
                                    content.add(i);
                                }

                                for (int i = 0 ; i < 10 ; i ++){
                                    allGame.add(new GameItem("Mixed","Teacher X","EASY", content));
                                }

                                GameItem[] gameArray = allGame.toArray(new GameItem[allGame.size()]);
                                Log.d(TAG,"gameArray size: *** " + gameArray.length);
                                Log.d(TAG,"All Game size:  *** " + allGame.size());
                                */

                                //GameItem[] gameArray = allGame.toArray();
                                /** Dummy value for list **/

                                // if student id is me && status = 0
                                // get content
                                // get type
                                // get difficulty
                                // get creator
                                allGame = new ArrayList<>();
                                theAdpater = new myAdapter(MusicGameActivity.this, allGame);

                                ListView theListView = (ListView) findViewById(R.id.gameListView);
                                theListView.setAdapter (theAdpater);

                                theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        GameItem game = (GameItem) adapterView.getAdapter().getItem(position);
                                        List<Integer> c = new ArrayList<>();

                                        for (int i = 0 ; i < game.content.size(); i++){
                                            Log.d(TAG," adding content !!!!!***");
                                            c.add(game.content.get(i));
                                        }


                                        //click inside and play
                                        mexerObject = new MExer(getApplicationContext());
                                        try {
                                            mExer = mexerObject.createExer_id(c);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        Bundle bundle = new Bundle();
                                        for (int i = 0 ; i < mExer.size(); i++){
                                            //Log.d(TAG,mExer.size() + " onStudentplayGameClock!!!!!!!!!!!!!!");
                                            bundle.putSerializable("extras"+i, mExer.get(i));
                                        }
                                        bundle.putString("gameID", game.gameID);
                                        bundle.putInt("size", mExer.size());
                                        playExerIntent.putExtras(bundle);
                                        startActivityForResult(playExerIntent, CHILD_RESULT_OBJECT);
                                    }
                                });

                                DatabaseReference gameRef = database.getReference("Game");
                                gameRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot snapshot, String s) {
                                        if ((snapshot.child("Status").getValue(Integer.class) == 0) && (snapshot.child("Student").getValue(String.class).equals(mAuth.getCurrentUser().getUid()))){ //&& ((snapshot.child("Status").getValue(Integer.class)) == 0)
                                            //Log.d(TAG, "time time time time time time");
                                            String gameID = snapshot.getKey();
                                            List<Integer> content = new ArrayList<>();
                                            DatabaseReference contentRef = database.getReference("Game").child(gameID).child("Content");
                                            contentRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot contentSnapshot : dataSnapshot.getChildren()){
                                                        Log.d(TAG,"i have contentttttt " + contentSnapshot.getValue(Integer.class));
                                                        content.add(contentSnapshot.getValue(Integer.class));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            String gType = snapshot.child("Type").getValue(String.class);
                                            String gDiff = snapshot.child("Difficulty").getValue(String.class);
                                            String createdBy = snapshot.child("Created").getValue(String.class);
                                            DatabaseReference teacherIDRef = database.getReference("Users").child(createdBy).child("Name");
                                            teacherIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String creatorName = dataSnapshot.getValue(String.class);
                                                    allGame.add(new GameItem(gameID, gType, "Created by " + creatorName, gDiff, content));
                                                    theAdpater.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot snapshot, String s) {
                                        if ((snapshot.child("Status").getValue(Integer.class) == 0) && (snapshot.child("Student").getValue(String.class).equals(mAuth.getCurrentUser().getUid()))){ //&& ((snapshot.child("Status").getValue(Integer.class)) == 0)
                                            //Log.d(TAG, "time time time time time time");
                                            String gameID = snapshot.getKey();
                                            List<Integer> content = new ArrayList<>();
                                            DatabaseReference contentRef = database.getReference("Game").child(gameID).child("Content");
                                            contentRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot contentSnapshot : dataSnapshot.getChildren()){
                                                        Log.d(TAG,"i have contentttttt " + contentSnapshot.getValue(Integer.class));
                                                        content.add(contentSnapshot.getValue(Integer.class));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            String gType = snapshot.child("Type").getValue(String.class);
                                            String gDiff = snapshot.child("Difficulty").getValue(String.class);
                                            String createdBy = snapshot.child("Created").getValue(String.class);
                                            DatabaseReference teacherIDRef = database.getReference("Users").child(createdBy).child("Name");
                                            teacherIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String creatorName = dataSnapshot.getValue(String.class);
                                                    allGame.add(new GameItem(gameID, gType, "Created by " + creatorName, gDiff, content));
                                                    theAdpater.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
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
                                gameScore = (TextView) findViewById(R.id.gameScoreText);
                                gamePercent = (TextView) findViewById(R.id.gameCorrectPercentageText);
                                gameAT = (TextView) findViewById(R.id.gameCorrectAverageTimeText);
                                overAllT = (TextView) findViewById(R.id.textView2);
                                incompleteGT = (TextView) findViewById(R.id.textView3);
                                pointEarnedT = (TextView) findViewById(R.id.textView5);
                                correctPerT = (TextView) findViewById(R.id.textView6);
                                averageTT = (TextView) findViewById(R.id.textView7);
                                selfPracticeButton = (ImageButton) findViewById(R.id.selfPractice);
                                gameScore.setTypeface(robotoMed);
                                gamePercent.setTypeface(robotoMed);
                                gameAT.setTypeface(robotoMed);

                                Handler handler = new Handler();
                                int delayS;
                                if (getResult){
                                    delayS = 600;
                                } else {
                                    delayS = 0;
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DatabaseReference checkAttributeExistRef = database.getReference("Users").child(user_id);
                                        checkAttributeExistRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("Score")){  // set text as score profile
                                                    DatabaseReference scoreRef = database.getReference("Users").child(user_id).child("Score");
                                                    scoreRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            int scoreTemp = 0;
                                                            int countT = 0;
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                scoreTemp += snapshot.getValue(Integer.class);
                                                                countT ++;
                                                            }
                                                            //Log.d(TAG, scoreTemp + "  scoreTEMP*********");
                                                            gameScore.setText(String.valueOf(scoreTemp));
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    DatabaseReference percentageRef = database.getReference("Users").child(user_id).child("CorrectWrongPercentage");
                                                    percentageRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            int percentageTemp = 0;
                                                            int countT = 0;
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                percentageTemp += snapshot.getValue(Integer.class);
                                                                countT ++;
                                                            }
                                                            percentageTemp = percentageTemp / countT;
                                                            //Log.d(TAG, percentageTemp + "  percentageTEMP*********");
                                                            gamePercent.setText(String.valueOf(percentageTemp) + "%");
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    DatabaseReference timeRef = database.getReference("Users").child(user_id).child("AverageTime");
                                                    timeRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            double aTemp = 0;
                                                            int countT = 0;
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                                aTemp += snapshot.getValue(Double.class);
                                                                countT ++;
                                                            }
                                                            aTemp = aTemp / countT;
                                                            //Log.d(TAG, aTemp + "  averageTimeTEMP*********");
                                                            gameAT.setText(jonchan.musex.Utility.secondsToMinutes(aTemp));
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                } else {   // set text default as 0
                                                    gameScore.setText("N/A");
                                                    gamePercent.setText("N/A");
                                                    gameAT.setText("N/A");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }, delayS);



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
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,  resultCode + "  can i get back my result????????????????");
        if (resultCode == RESULT_OK){
            getResult = true;
            returnScore = data.getIntExtra("score",0);
            returnCorrectWrongPercentage = data.getIntExtra("percentage",0);
            returnAverageTime = data.getDoubleExtra("averageTime", 0);
            returnGameID = data.getStringExtra("gameID");
            Log.d(TAG, returnScore + "  RETURN RESULT!!!!!!!!!");
            Log.d(TAG, returnCorrectWrongPercentage + "  RETURN PERCENTAGE!!!!!!!!!");
            Log.d(TAG, returnAverageTime + "  RETURN AVERAGE TIME!!!!!!!!!");
            Log.d(TAG, returnGameID + "  RETURN GAME ID *_* ^_^ *_*");
        }
        else {
            getResult = false;
            Log.d(TAG,  "i cant get back TTTTTTT");
        }


        // update profile result //  retrieve first
        if (getResult == true){
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            final String user_id = mAuth.getCurrentUser().getUid();
            mRef = database.getReference("Users").child(user_id).child("UserType");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    value = dataSnapshot.getValue(String.class);
                    if(value.equals("Student")) {   // Only for student
                        DatabaseReference newScoreRef = database.getReference("Users").child(user_id).child("Score").push();
                        DatabaseReference newPerRef = database.getReference("Users").child(user_id).child("CorrectWrongPercentage").push();
                        DatabaseReference newTimeRef = database.getReference("Users").child(user_id).child("AverageTime").push();
                        newScoreRef.setValue(returnScore);
                        newPerRef.setValue(returnCorrectWrongPercentage);
                        newTimeRef.setValue(returnAverageTime);

                        DatabaseReference gameStatusRef = database.getReference("Game").child(returnGameID).child("Status");
                        gameStatusRef.setValue(1);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    public void onSelfPracticeClick(View view){    // button on click for student
        startActivity(new Intent(MusicGameActivity.this,MusicGameGenerateActivity.class));
    }


    public void onCustomGenerateClick(View view) {  // button on click for teacher
        // check whether teacher has students first
        if (studentNameList == null || studentNameList.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning!!!!");
            alert.setMessage("You need to have at least one student before you can customize a game");
            alert.setPositiveButton("OK",null);
            alert.show();

            return;
        }


        /** initialize variables for populating spinner list **/

        String [] noq_array, diff_array, type_array;
        noq_array = new String [] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        diff_array = new String [] {"EASY", "MEDIUM", "HARD", "COMBINE ALL DIFFICULTY"};
        LinkedHashMap lhm = new LinkedHashMap();
        lhm.put("Scale - Treble Clef", false);
        lhm.put("Scale - Bass Clef", false);
        lhm.put("Interval - Treble Clef", false);
        lhm.put("Interval - Bass Clef", false); /** Not yet supported!!!!!**/
        //lhm.put("Arpeggio", false);  /** Not yet supported!!!!!**/

        selectedQT.clear();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(jonchan.musex.R.layout.custom_generate_exer, null);
        TextView title = new TextView(this);
        title.setText("Custom Game");
        int color = Color.parseColor("#223017");
        title.setBackgroundColor(color);
        title.setPadding(10, 20, 10, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        mBuilder.setCustomTitle(title);
        final Spinner NoOfQuestionSpinner = (Spinner) mView.findViewById(jonchan.musex.R.id.spinner_noq);
        final Spinner DifficultySpinner = (Spinner) mView.findViewById(jonchan.musex.R.id.spinner_difficulty);

        /*DifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DifficultySpinner.setBackground(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        ArrayAdapter<String> noq_arrAdapter = new ArrayAdapter<String>(this, jonchan.musex.R.layout.spinner_layout,noq_array);
        noq_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        NoOfQuestionSpinner.setPrompt("Number of Questions");
        NoOfQuestionSpinner.setAdapter(noq_arrAdapter);
        NoOfQuestionSpinner.setSelection(0);

        ArrayAdapter<String> diff_arrAdapter = new ArrayAdapter<String>(this, jonchan.musex.R.layout.spinner_layout,diff_array);
        diff_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        DifficultySpinner.setPrompt("Difficulty");
        DifficultySpinner.setAdapter(diff_arrAdapter);
        DifficultySpinner.setSelection(0);
        DifficultySpinner.setBackground(null);

        final List<String> keylist = new ArrayList<>(lhm.keySet());
        MultiSpinner simpleSpinner = (MultiSpinner) mView.findViewById(jonchan.musex.R.id.spinner_type);

        simpleSpinner.setItems(lhm, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=0; i<selected.length; i++) {
                    if(selected[i]) {
                        Log.i("TAG", i + " : "+ keylist.get(i));
                        if (keylist.get(i).equals("Scale - Treble Clef")){
                            selectedQT.add(QType.SCALE_T);
                        } else if (keylist.get(i).equals("Scale - Bass Clef")) {
                            selectedQT.add(QType.SCALE_B);
                        }else if (keylist.get(i).equals("Interval - Treble Clef")) {
                            selectedQT.add(QType.INTERVAL_T);
                        } else if (keylist.get(i).equals("Interval - Bass Clef")) {
                            selectedQT.add(QType.INTERVAL_B);
                        } else if (keylist.get(i).equals("Arpeggio")){   // Not yet supported
                            selectedQT.add(QType.ARPEGGIO_T);
                            selectedQT.add(QType.ARPEGGIO_B);
                        }
                    }
                }
                selectedQT_arr = new QType[selectedQT.size()];
                selectedQT_arr = selectedQT.toArray(selectedQT_arr);
            }
        });

        selectedStudentList = new ArrayList<>();
        selectedStudentIDList = new ArrayList<>();
        //List<Integer> getSelectedStudentIndexList = new ArrayList<>();
        final List<KeyPairBoolData> studentKeyPairList = new ArrayList<>();
        for (int i = 0; i < studentNameList.size(); i++){
            Log.d(TAG, "Studentnamelist*********");
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i+1);
            h.setName(studentNameList.get(i));
            h.setSelected(false);
            studentKeyPairList.add(h);
        }
        MultiSpinnerSearch studentMultiSpinner = (MultiSpinnerSearch) mView.findViewById(R.id.spinner_type2);
        studentMultiSpinner.setItems(studentKeyPairList, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        //Log.d(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        //Log.d(TAG, ".&S*A&D*SAD******8   " + studentKeyPairList.get(i).isSelected());
                        //selectedStudentList.add(items.get(i).getName());
                    }
                }
            }
        });

        playExerIntent = new Intent(this, jonchan.musex.PlayExer.class);
        AlertDialog.Builder noGameBuild = new AlertDialog.Builder(this);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int counterTemp = 0;
                for (int i = 0; i < studentKeyPairList.size(); i++){
                    if (studentKeyPairList.get(i).isSelected()){
                        selectedStudentIDList.add(studentIDList.get(i));
                        //selectedStudentList.add(studentNameList.get(i));
                        Log.d(TAG, "selected student id:         " + selectedStudentIDList.get(counterTemp));
                        counterTemp++;
                    }
                }

                //NoOfQuestionSpinner.setSelection(which);
                selectedNOQ = Integer.parseInt(NoOfQuestionSpinner.getSelectedItem().toString());
                mid_selectedDifficulty = DifficultySpinner.getSelectedItem().toString();
                if (mid_selectedDifficulty == "EASY"){
                    selectedDifficulty = Difficulty.EASY;
                } else if (mid_selectedDifficulty == "MEDIUM"){
                    selectedDifficulty = Difficulty.MEDIUM;
                } else if (mid_selectedDifficulty == "HARD"){
                    selectedDifficulty = Difficulty.HARD;
                } else if (mid_selectedDifficulty == "COMBINE ALL DIFFICULTY"){
                    selectedDifficulty = Difficulty.ALL;
                }

                if (selectedStudentIDList.isEmpty() || selectedQT.isEmpty()){
                    noGameBuild.setTitle("Warning");
                    noGameBuild.setMessage("Please do not leave any attribute blank! ");
                    noGameBuild.setPositiveButton("OK",null);
                    noGameBuild.show();
                    return;
                }

                dialog.dismiss();
                ////////
                //mExer = MExer.createExer(5, Difficulty.EASY, new QType[] {QType.SCALE_B,QType.SCALE_T});
                ////////

                Log.d(TAG,"*********selected No. of Quesiton" + selectedNOQ );
                Log.d(TAG,"*********selected Difficulty" + selectedDifficulty);
                Log.d(TAG,"*********selected Types" + selectedQT_arr[0]);

                mexerObject = new MExer(getApplicationContext());
                try {
                    //mExer = mexerObject.createExer(selectedNOQ, selectedDifficulty, selectedQT_arr);
                    uploadGameContentList = new ArrayList<>();
                    uploadGameContentList = mexerObject.createdExer_toID(selectedNOQ, selectedDifficulty, selectedQT_arr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                Bundle bundle = new Bundle();
                for (int i = 0 ; i < mExer.size(); i++){
                    bundle.putSerializable("extras"+i, mExer.get(i));
                }
                playExerIntent.putExtras(bundle);
                Log.d(TAG,mExer.size() + " onCustomGenerateClick!!!!!!!!!!!!!!");
                startActivityForResult(playExerIntent, CHILD_RESULT_OBJECT);
                */



                // upload to firebase!!!!!!!!!!!!!!
                addGameToFire(uploadGameContentList, selectedStudentIDList, selectedQT_arr, selectedDifficulty);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
        //dialog.getWindow().setLayout(980,1100);

    }

    public void onTeacherSelfPracticeClick(View view){            // button on click for teacher
        /** initialize variables for populating spinner list **/
        String [] noq_array, diff_array, type_array;
        noq_array = new String [] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        diff_array = new String [] {"EASY", "MEDIUM", "HARD", "COMBINE ALL DIFFICULTY"};
        LinkedHashMap lhm = new LinkedHashMap();
        lhm.put("Scale - Treble Clef", false);
        lhm.put("Scale - Bass Clef", false);
        lhm.put("Interval - Treble Clef", false);
        lhm.put("Interval - Bass Clef", false); /** Not yet supported!!!!!**/

        selectedQT.clear();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(jonchan.musex.R.layout.custom_self_practice_exer, null);
        mBuilder.setTitle("Custom Exercise");
        final Spinner NoOfQuestionSpinner = (Spinner) mView.findViewById(jonchan.musex.R.id.spinner_noq);
        final Spinner DifficultySpinner = (Spinner) mView.findViewById(jonchan.musex.R.id.spinner_difficulty);

        ArrayAdapter<String> noq_arrAdapter = new ArrayAdapter<String>(this, jonchan.musex.R.layout.spinner_layout,noq_array);
        noq_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        NoOfQuestionSpinner.setPrompt("Number of Questions");
        NoOfQuestionSpinner.setAdapter(noq_arrAdapter);
        NoOfQuestionSpinner.setSelection(0);

        ArrayAdapter<String> diff_arrAdapter = new ArrayAdapter<String>(this, jonchan.musex.R.layout.spinner_layout,diff_array);
        diff_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        DifficultySpinner.setPrompt("Difficulty");
        DifficultySpinner.setAdapter(diff_arrAdapter);
        DifficultySpinner.setSelection(0);

        final List<String> keylist = new ArrayList<>(lhm.keySet());
        MultiSpinner simpleSpinner = (MultiSpinner) mView.findViewById(jonchan.musex.R.id.spinner_type);

        simpleSpinner.setItems(lhm, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=0; i<selected.length; i++) {
                    if(selected[i]) {
                        Log.i("TAG", i + " : "+ keylist.get(i));
                        if (keylist.get(i).equals("Scale - Treble Clef")){
                            selectedQT.add(QType.SCALE_T);
                        } else if (keylist.get(i).equals("Scale - Bass Clef")) {
                            selectedQT.add(QType.SCALE_B);
                        }else if (keylist.get(i).equals("Interval - Treble Clef")) {
                            selectedQT.add(QType.INTERVAL_T);
                        } else if (keylist.get(i).equals("Interval - Bass Clef")) {
                            selectedQT.add(QType.INTERVAL_B);
                        } else if (keylist.get(i).equals("Arpeggio")){   // Not yet supported
                            selectedQT.add(QType.ARPEGGIO_T);
                            selectedQT.add(QType.ARPEGGIO_B);
                        }
                    }
                }
                selectedQT_arr = new QType[selectedQT.size()];
                selectedQT_arr = selectedQT.toArray(selectedQT_arr);
            }
        });

        Intent playExerIntent = new Intent(this, jonchan.musex.PlayExer.class);
        AlertDialog.Builder noGameBuild = new AlertDialog.Builder(this);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //NoOfQuestionSpinner.setSelection(which);
                selectedNOQ = Integer.parseInt(NoOfQuestionSpinner.getSelectedItem().toString());
                mid_selectedDifficulty = DifficultySpinner.getSelectedItem().toString();
                if (mid_selectedDifficulty == "EASY"){
                    selectedDifficulty = Difficulty.EASY;
                } else if (mid_selectedDifficulty == "MEDIUM"){
                    selectedDifficulty = Difficulty.MEDIUM;
                } else if (mid_selectedDifficulty == "HARD"){
                    selectedDifficulty = Difficulty.HARD;
                } else if (mid_selectedDifficulty == "COMBINE ALL DIFFICULTY"){
                    selectedDifficulty = Difficulty.ALL;
                }

                if (selectedQT.isEmpty()){
                    noGameBuild.setTitle("Warning");
                    noGameBuild.setMessage("Please do not leave any attribute blank! ");
                    noGameBuild.setPositiveButton("OK",null);
                    noGameBuild.show();
                    return;
                }

                dialog.dismiss();


                mexerObject = new MExer(getApplicationContext());
                try {
                    mExer = mexerObject.createExer(selectedNOQ, selectedDifficulty, selectedQT_arr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                for (int i = 0 ; i < mExer.size(); i++){
                    bundle.putSerializable("extras"+i, mExer.get(i));
                }
                bundle.putInt("size", mExer.size());
                playExerIntent.putExtras(bundle);
                startActivityForResult(playExerIntent, CHILD_RESULT_OBJECT);
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void addGameToFire(List<Integer> content, List<String> studentIDLi, QType [] qt, Difficulty diff){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

        DatabaseReference mRef = database.getReference("Game");
        DatabaseReference newGameData;

        for (int k = 0; k < studentIDLi.size(); k++) {
            newGameData = mRef.push();
            newGameData.child("Created").setValue(user_id);    // teacher id

            String gameType = "";
            if (qt.length > 1) {
                gameType = "Mixed";
            } else {
                QType q = qt[0];
                if (q == QType.SCALE_T){
                    gameType = "Scale - Treble Clef";
                } else if (q == QType.SCALE_B){
                    gameType = "Scale - Bass Clef";
                } else if (q == QType.INTERVAL_T){
                    gameType = "Interval - Treble Clef";
                } else if (q == QType.INTERVAL_B){
                    gameType = "Interval - Bass Clef";
                }
            }
            newGameData.child("Type").setValue(gameType);     // game type

            String gameDiff = diff.toString();
            newGameData.child("Difficulty").setValue(gameDiff);  // game difficulty

            int status = 0;      // 0 = incomplete; 1 = complete
            newGameData.child("Status").setValue(status);     // game status (incomplete = 0)

            newGameData.child("Content").setValue(content);  // game content (list)

            newGameData.child("Student").setValue(studentIDLi.get(k));
        }
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
        return R.id.navigation_game;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent myIntent = new Intent(this, LoginActivity.class);
                try {
                    startActivity(myIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private class myAdapter extends ArrayAdapter<GameItem> {
        public myAdapter(@NonNull Context context, List<GameItem> gi) {
            super(context, R.layout.row_layout, gi);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater theInflater = LayoutInflater.from(getContext());

            View theView = theInflater.inflate(R.layout.row_layout, parent, false);
            //we don't want to inflate it into parent -> so we put false

            GameItem game = getItem(position);
            String type = game.type;
            String createdBy = game.createdBy;
            String diff = game.difficulty;

            TextView typeText = (TextView) theView.findViewById(R.id.gameSelect_gameTypeText);
            TextView creatorText = (TextView) theView.findViewById(R.id.gameSelect_createdByText);
            TextView diffText = (TextView) theView.findViewById(R.id.gameSelect_difficultyText);
            typeText.setText(type);
            creatorText.setText(createdBy);
            diffText.setText(diff);

            return (theView);

        }
    }

}


class GameItem {

    public String type;
    public String createdBy;
    public String difficulty;
    public String gameID;
    public List<Integer> content;

    final String TAG = "myAct";

    public GameItem (String gID, String t, String c, String d, List<Integer> myContent){
        this.gameID = gID;
        this.type = t;
        this.createdBy = c;
        this.difficulty = d;

        content = new ArrayList<>();
        for (int i = 0; i < myContent.size(); i++){
            content.add(myContent.get(i));
        }
    }
}
