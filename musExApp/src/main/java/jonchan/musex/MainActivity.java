package jonchan.musex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;

import java.io.IOException;
import java.util.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    /**fire base **/
    FirebaseDatabase database;
    FirebaseAuth mAuth;



    TextView myMNote;
    Spinner dropDown;
    ArrayList <MQues> mExer;
    MExer mexerObject;

    protected int id;
    protected String [] ansRelated;
    protected String ans;
    protected String notes;
    protected StringBuffer notesBuffer;
    protected QType qtype;
    protected Difficulty diff;

    int noOfQuestion;
    public final int CHILD_RESULT_OBJECT = 1;


    //return stuff!!!!
    int returnScore;
    int returnCorrectWrongPercentage;

    /** initilize variables for holding Spinner Selected Items */
    int selectedNOQ;
    String mid_selectedDifficulty;
    Difficulty selectedDifficulty;
    ArrayList <QType> selectedQT = new ArrayList<>();
    QType [] selectedQT_arr;

    final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mus_ex_activity_main);

        int [] a = Utility.fairShareQuestion(2,5);
        for (int i = 0 ; i < a.length;  i++){
            Log.d(TAG," GIVE ME THE GG : " + a[i]);
        }

        //tryAddGameToFire(mExer);
        //tryAddStuffToFire();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (requestCode == Activity.RESULT_OK){
                returnScore = data.getIntExtra("score",0);
                returnCorrectWrongPercentage = data.getIntExtra("percentage",0);
                Log.d(TAG, returnScore + "  RETURN RESULT!!!!!!!!!");
                Log.d(TAG, returnCorrectWrongPercentage + "  RETURN PERCENTAGE!!!!!!!!!");
            }
            if (requestCode == Activity.RESULT_CANCELED){
                Log.d(TAG, "RESULT_CANCELLED???????");
                //////////// can't figure out how to catch this
                //////////// but can use above to configure what you want
            }
        }

    }

    public void onCustomGenerateClick(View view) {
        /** initialize variables for populating spinner list **/
        String [] noq_array, diff_array, type_array;
        noq_array = new String [] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        diff_array = new String [] {"EASY", "MEDIUM", "HARD", "COMBINE ALL DIFFICULTY"};
        LinkedHashMap lhm = new LinkedHashMap();
        lhm.put("Scale", false);
        lhm.put("Interval", false);  /** Not yet supported!!!!!**/
//        lhm.put("Arpeggio", false);  /** Not yet supported!!!!!**/

        selectedQT.clear();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_generate_exer, null);
        mBuilder.setTitle("Custom Exercise");
        final Spinner NoOfQuestionSpinner = (Spinner) mView.findViewById(R.id.spinner_noq);
        final Spinner DifficultySpinner = (Spinner) mView.findViewById(R.id.spinner_difficulty);

        ArrayAdapter<String> noq_arrAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_layout,noq_array);
        noq_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        NoOfQuestionSpinner.setPrompt("Number of Questions");
        NoOfQuestionSpinner.setAdapter(noq_arrAdapter);
        NoOfQuestionSpinner.setSelection(0);

        ArrayAdapter<String> diff_arrAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_layout,diff_array);
        diff_arrAdapter.setDropDownViewResource (android.R.layout.simple_dropdown_item_1line);
        DifficultySpinner.setPrompt("Difficulty");
        DifficultySpinner.setAdapter(diff_arrAdapter);
        DifficultySpinner.setSelection(0);

        final List<String> keylist = new ArrayList<>(lhm.keySet());
        MultiSpinner simpleSpinner = (MultiSpinner) mView.findViewById(R.id.spinner_type);

        simpleSpinner.setItems(lhm, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=0; i<selected.length; i++) {
                    if(selected[i]) {
                        Log.i("TAG", i + " : "+ keylist.get(i));
                        if (keylist.get(i) == "Scale"){
                            selectedQT.add(QType.SCALE_B);
                            selectedQT.add(QType.SCALE_T);
                        } else if (keylist.get(i) == "Interval") {
                            selectedQT.add(QType.INTERVAL_B);
                            selectedQT.add(QType.INTERVAL_T);
                        } else if (keylist.get(i) == "Arpeggio"){
                            selectedQT.add(QType.ARPEGGIO_T);
                            selectedQT.add(QType.ARPEGGIO_B);
                        }
                    }
                }
                selectedQT_arr = new QType[selectedQT.size()];
                selectedQT_arr = selectedQT.toArray(selectedQT_arr);
            }
        });

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

                dialog.dismiss();
                Intent playExerIntent = new Intent(MainActivity.this, PlayExer.class);
                ////////
                //mExer = MExer.createExer(5, Difficulty.EASY, new QType[] {QType.SCALE_B,QType.SCALE_T});
                ////////

                Log.d(TAG,"*********selected No. of Quesiton" + selectedNOQ );
                Log.d(TAG,"*********selected Difficulty" + selectedDifficulty);
                Log.d(TAG,"*********selected Types" + selectedQT_arr[0]);

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
                playExerIntent.putExtras(bundle);
                Log.d(TAG,mExer.size() + " onCustomGenerateClick!!!!!!!!!!!!!!");

                /**Firebase Activity **/
                tryAddGameToFire(mExer);
                /**Firebase Activity **/



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

    public void onAutoGenerateClick(View view) {

        Intent playExerIntent = new Intent(this, PlayExer.class);
        mexerObject = new MExer(getApplicationContext());
        try {
            mExer = mexerObject.createExer(0,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mExer = MExer.createExer(0,true);

        //FOR TESTING
//        mExer = MExer.testExer(1063);

        Log.d(TAG,mExer.size() + " onAutoGenerateClick!!!!!!!!!!!!!!");

        Bundle bundle = new Bundle();
        for (int i = 0 ; i < mExer.size(); i++){
            bundle.putSerializable("extras"+i, mExer.get(i));
        }
        playExerIntent.putExtras(bundle);
        startActivityForResult(playExerIntent, CHILD_RESULT_OBJECT);
    }

    public void tryAddStuffToFire(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String user_id = mAuth.getCurrentUser().getUid();

        DatabaseReference mRef = database.getReference("Testing");
        DatabaseReference newTestingData = mRef.push();
        newTestingData.child("t1").setValue("Hello");

    }

    ArrayList<ArrayList<MQues>> All_Extracted_Games = new ArrayList<>();
    ArrayList<MQues> mQues_firebase = new ArrayList<>();

    public void tryAddGameToFire(ArrayList<MQues> mx){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();

        /** Firebase RETRIEVE **/
        DatabaseReference gameIDRef = database.getReference("Game");
        gameIDRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String gameID = dataSnapshot.getKey();
//                    Log.d(TAG, "HIIIIIIIIIIIIIIzzz       " + gameID);
                    DatabaseReference gameUserIDRef = database.getReference("Game").child(gameID).child("Created");
                    gameUserIDRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String createdUserID = dataSnapshot.getValue(String.class);
                            if (createdUserID != null){
                                if (createdUserID.equals(user_id)){
                                    DatabaseReference extractGameRef = database.getReference("Game").child(gameID);
                                    extractGameRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            ArrayList<MQues> mQues_firebase = new ArrayList<>();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                if (!snapshot.getValue().equals("Created")){
                                                    for (DataSnapshot data : snapshot.getChildren()){
                                                        MQues mq = snapshot.getValue(MQues.class);
                                                        mQues_firebase.add(mq);
//                                                        Log.d(TAG,"SUCCESSFULLL HOPEFULLY*****" + mq.ans);
                                                        break;
                                                    }
                                                }
//                                                Log.d(TAG, "HIi INNER____CHILD");
                                            }
                                            All_Extracted_Games.add(mQues_firebase);
                                            Log.d(TAG, "ALL EXTRACT GAMES size () : " + All_Extracted_Games.size());
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
//                            Log.d(TAG,"FINAL-FINAL-CALL");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });


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

        /** Firebase add **/
        DatabaseReference mRef = database.getReference("Game");
        DatabaseReference newQuesData = mRef.push();
        for (int i = 0; i < mx.size(); i++){
            MQues mq = mx.get(i);
            //DatabaseReference newQuesData = mRef.push();
            newQuesData.child("Question " + i).setValue(mq);
        }
        newQuesData.child("Created").setValue(user_id);

        //MQues mq = new MQues(1004, new String [] {"db4","eb4","f4","fb4","gb4","ab4","bb4","db5"}, "Db Major", new String [] {"Db Major","C# Harmonic Minor","C# Melodic Minor","C# Natural Minor","Eb Major","Ab Major","Bb Major","D Major"}, Difficulty.MEDIUM, QType.SCALE_T);
        //testQues tq = new testQues("C Major", new String [] {"c note", "d note"}, Difficulty.EASY);
        //newGameData.setValue(tq);


        //newGameData.child("asd").setValue("asd");
    }


}

/**
class SpinnerItem {
    private final String text;
    private final boolean isHint;

    public SpinnerItem(String strItem, boolean flag) {
        this.isHint = flag;
        this.text = strItem;
    }

    public String getItemString() {
        return text;
    }

    public boolean isHint() {
        return isHint;
    }
}

class MySpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    public MySpinnerAdapter(Context context, int resource, List<SpinnerItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }

    @Override
    public SpinnerItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}

**/
