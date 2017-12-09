package jonchan.musex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonchan on 18/11/2017.
 */

public class PlayExer extends FragmentActivity implements Communicator {

    int score = 0;
    int questionLeft;
    int NUM_PAGE;
    MyAdapter myAdapter;
    long timeStart, timeEnd, timeDelta;

    ArrayList<MQues> mExer;
    List<MQues> mExerObjects;
    ViewPager viewPager = null;

    String my_GameID;
    int objSize;

    final String TAG = "MyActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playexer_main);

//        Intent activityThatCalled = getIntent();
//        Bundle bundle = activityThatCalled.getExtras();
//        mExer = (ArrayList<MQues>) bundle.getSerializable("exerValue");

        Intent activityThatCalled = getIntent();
        Bundle bundle = activityThatCalled.getExtras();

        mExerObjects = new ArrayList<MQues>();

        // extract game ID
        my_GameID = bundle.getString("gameID");
        objSize = bundle.getInt("size");
        for(int i = 0; i < objSize; i++){
            mExerObjects.add((MQues) bundle.getSerializable("extras" + i));
        }

//        final String TAG = "MyActivity";
//        Log.d(TAG,"onGenerateRecivev!!!!!!!!");

        NUM_PAGE = mExerObjects.size();

        viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        myAdapter = new MyAdapter (fragmentManager, mExerObjects, NUM_PAGE);
        viewPager.setAdapter(myAdapter);

        questionLeft = mExerObjects.size();

        timeStart = System.currentTimeMillis();

        Log.d(TAG,mExerObjects.size() + "****************");

    }

    @Override
    public void onBackPressed() {
        Intent returnMainActivity = new Intent();
        setResult(0, returnMainActivity);
        finish();
        super.onBackPressed();

    }

    @Override
    public void giveToActivity(int intData) {
        if (intData == 1){
            score++;
        }
        else if (intData == 0){
        }
        questionLeft--;

        if (questionLeft == 0){
            timeEnd = System.currentTimeMillis();
            timeDelta = timeEnd - timeStart;
            NUM_PAGE += 1;
            myAdapter.sendLastPageResult(timeDelta, score, my_GameID);
            myAdapter.notifyDataSetChanged();
        }
    }
}

class MyAdapter extends FragmentStatePagerAdapter {

    List<MQues> mExerObjects;
    FragmentManager fm;
    int numPage;
    int fixedCounter;

    int score = 0;
    double elapsedSeconds;
    boolean callChange = false;

    String gameID;

    public MyAdapter(FragmentManager fm, List<MQues> mq, int numPage) {
        super(fm);
        this.fm = fm;
        mExerObjects = mq;
        this.numPage = numPage;
        fixedCounter = this.numPage;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        final String TAG = "MyActivity";

        Log.d(TAG,callChange + "    numPage " + getCount() );


        for (int i = 0 ; i < fixedCounter; i++){
            if (position == i) {
                bundle.putSerializable("bkey", mExerObjects.get(i));
                bundle.putInt("questionNo",i+1);
                bundle.putInt("totalQuestionNo", fixedCounter);
                fragment = new FragmentQues();
                fragment.setArguments(bundle);
                Log.d(TAG, "onFragment_________" + i);
                break;
            }
        }

        if (callChange == true && position == getCount()-1){
            bundle.putDouble("timeElapsed", elapsedSeconds);
            bundle.putInt("score", score);
            bundle.putInt("totalQuestionNo", fixedCounter);
            bundle.putString("gameID",gameID);
            fragment = new FragmentQuesResult();
            fragment.setArguments(bundle);
        }

//        if (position == 0){
//            bundle.putSerializable("bkey", mExerObjects.get(0));
//            fragment = new FragmentQues();
//            fragment.setArguments(bundle);
//            Log.d(TAG,"onFragment_________00");
//        }

        return fragment;
    }

    @Override
    public int getCount() {
        return numPage;
    }

    public void sendLastPageResult (long t, int score, String gID){
        this.numPage++;
        elapsedSeconds = t / 1000.0;
        this.score = score;
        this.gameID = gID;
    }

    @Override
    public void notifyDataSetChanged() {

        callChange = true;
        super.notifyDataSetChanged();
        //incrementCount();

    }

    @Override
    public int getItemPosition(Object object) {
        return MyAdapter.POSITION_UNCHANGED;
    }
}
