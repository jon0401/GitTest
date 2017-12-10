package jonchan.musex;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jonchan on 18/11/2017.
 */

public class FragmentQues extends Fragment {

    private MQues mQuesObject;
    Communicator comm;
//    private static final String mQuesObject_KEY = "mQuesObject_key";

    TextView myMNote, answerText, questionNo, blinkText;
    long timeStart, timeEnd, timeDelta;
    boolean swipe;
    Spinner dropDown;
    Button checkAnswerButton;
    ImageView correctWrongImage;

    ArrayList<String> dropDownItems;
    boolean clicked = false;
    String answer; int color, drawIcon;

    int QuestionNo, TotalQuestionNo;

    final String TAG = "FragQues";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"OnCreate Method");
        swipe = false;
        timeStart = System.currentTimeMillis();




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        comm = (Communicator) getActivity();

        mQuesObject = (MQues) getArguments().getSerializable("bkey");
        QuestionNo = getArguments().getInt("questionNo");
        TotalQuestionNo = getArguments().getInt("totalQuestionNo");

        View view = inflater.inflate(R.layout.playexer,container,false);
        Typeface STAFCPE = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/STAFCPE_.TTF");
        Typeface Roboto_Medium = Typeface.createFromAsset(view.getContext().getAssets(),  "fonts/Roboto-Medium.ttf");
        blinkText = (TextView) view.findViewById(R.id.blinkText);
        blinkText.setEnabled(false);
        blinkText.setVisibility(TextView.INVISIBLE);

        final GestureDetector gesture = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                        swipe = true;
                        return true;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        swipe = true;
                        return true;
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        swipe = true;
                        return true;
                    }
                } catch (Exception e) {
                    // nothing
                }
                return onFling(e1, e2, velocityX, velocityY);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeEnd =  System.currentTimeMillis();
                if (timeEnd - timeStart >= 5000){
                    timeStart = System.currentTimeMillis();
                    blinkText.setEnabled(true);
                    blinkText.setVisibility(TextView.VISIBLE);
                } else {
                    blinkText.setEnabled(false);
                    blinkText.setVisibility(TextView.INVISIBLE);
                }
                if (gesture.onTouchEvent(event)){
                    /*
                    timeStart = System.currentTimeMillis();
                    blinkText.setEnabled(false);
                    blinkText.setVisibility(TextView.INVISIBLE);
                    */
                } else {
                        /*
                    timeEnd = System.currentTimeMillis();
                    timeDelta = timeEnd - timeStart;
                    if (timeDelta > 5000){
                        blinkText.setEnabled(true);
                        blinkText.setVisibility(TextView.VISIBLE);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(700); //You can manage the time of the blink with this parameter
                        anim.setStartOffset(20);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        blinkText.startAnimation(anim);
                    }
                    */
                }
                return gesture.onTouchEvent(event);
            }
        });


        String myS;
        myS = mQuesObject.notes;

        myMNote = (TextView) view.findViewById(R.id.mNote);
        myMNote.setTypeface(STAFCPE);
        myMNote.setText(myS);

        if (savedInstanceState == null) {
            dropDownItems = new ArrayList<>();
            int [] randSelectedIndex = Utility.getRandomArray(3, mQuesObject.ansRelated.length);  // 3 and 0 needs to be modified
            dropDownItems.add(mQuesObject.ans); // always 0 becoz must store the ans to dropdownlist
            for (int i = 0; i < randSelectedIndex.length; i++){
                dropDownItems.add(mQuesObject.ansRelated[randSelectedIndex[i]]);
            }
            Collections.shuffle(dropDownItems);
        }
        else {
            dropDownItems = savedInstanceState.getStringArrayList("dropDownItems");
        }

        dropDown = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropDownItems);
        dropDown.setAdapter(adapter);


        answerText = (TextView) view.findViewById(R.id.quesAnswer);
        answerText.setTypeface(Roboto_Medium);
        checkAnswerButton = (Button) view.findViewById(R.id.checkAns);
        correctWrongImage = (ImageView) view.findViewById(R.id.correctOrWrongIcon);
        questionNo = (TextView) view.findViewById(R.id.whichQuesiton);

        if (savedInstanceState != null && savedInstanceState.getBoolean("onClick") == true){
            clicked = true;
            answer = savedInstanceState.getString("answer");
            color = savedInstanceState.getInt("color");
            drawIcon = savedInstanceState.getInt("icon");
            checkAnswerButton.setEnabled(false);
            answerText.setVisibility(View.VISIBLE);
            answerText.setText(answer);
            answerText.setTextColor(color);
            correctWrongImage.setVisibility(View.VISIBLE);
            correctWrongImage.setImageResource(drawIcon);
        } else {
            checkAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked = true;
                    checkAnswerButton.setEnabled(false);
                    correctWrongImage.setVisibility(View.VISIBLE);
                    answerText.setVisibility(View.VISIBLE);
                    String userAnswer = dropDown.getSelectedItem().toString();
                    if (userAnswer == mQuesObject.ans){
                        //answer = "Correct!";
                        drawIcon = R.drawable.tick;
                        correctWrongImage.setImageResource(drawIcon);
                        answer = mQuesObject.ans;
                        color = Color.parseColor("#6CBD44");
                        answerText.setText(answer);
                        answerText.setTextColor(color);
                        comm.giveToActivity(1);
                    }
                    else {
                        //answer = "Wrong! \nCorrect answer is " + mQuesObject.ans;
                        drawIcon = R.drawable.cross;
                        correctWrongImage.setImageResource(drawIcon);
                        answer = mQuesObject.ans;
                        color = Color.parseColor("#F90D2F");
                        answerText.setText(answer);
                        answerText.setTextColor(color);
                        comm.giveToActivity(0);
                    }

                }
            });
        }

        questionNo.setVisibility(View.VISIBLE);
        questionNo.setText("Question " + QuestionNo + " of " + TotalQuestionNo );



        Log.d(TAG,"OnCreateView Method");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"OnActivityCreated Method");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"OnSavedInstanceState Method");
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("dropDownItems", dropDownItems);

        if (clicked == true){
            outState.putBoolean("onClick", clicked);
            outState.putString("answer", answer);
            outState.putInt("color", color);
            outState.putInt("icon", drawIcon);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"OnStart Method");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume Method");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"OnDestroyView Method");
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.checkAns:
//        }
//    }
}
