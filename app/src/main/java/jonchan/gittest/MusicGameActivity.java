package jonchan.gittest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jonchan.musex.*;

/**
 * Created by jonchan on 27/11/2017.
 */

public class MusicGameActivity extends BaseActivity {

    TextView myMNote;
    Spinner dropDown;
    ArrayList<MQues> mExer;
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

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mus_game_activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            navigationView.postDelayed(() -> {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(this, MainActivity.class));
                } else if (itemId == R.id.navigation_booking) {
                    startActivity(new Intent(this, Facitilies_BookingActivity.class));
                } else if (itemId == R.id.navigation_game) {
                    startActivity(new Intent(this, MusicGameActivity.class));
                } else if (itemId == R.id.navigation_contact){
                    startActivity(new Intent(this, DisplayStudentActivity.class));
                }
                finish();
            }, 300);
            return true;
        });

        updateNavigationBarState();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
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

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(jonchan.musex.R.layout.custom_generate_exer, null);
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

        Intent playExerIntent = new Intent(this, jonchan.musex.PlayExer.class);

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

    @Override
    int getContentViewId() {
        return R.layout.mus_game_activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_game;
    }
}
