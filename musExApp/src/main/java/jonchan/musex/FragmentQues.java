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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentQues extends Fragment{

    private MQues mQuesObject;
    Communicator comm;
//    private static final String mQuesObject_KEY = "mQuesObject_key";

    TextView myMNote, answerText, questionNo;
    Spinner dropDown;
    Button checkAnswerButton;
    ImageView correctWrongImage;

    ArrayList<String> dropDownItems;
    boolean clicked = false;
    String answer; int color, drawIcon;

    int QuestionNo, TotalQuestionNo;

    final String TAG = "FragQues";


//    public static FragmentQues newInstance (MQues mQues){
//        FragmentQues frag = new FragmentQues();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(mQuesObject_KEY, mQues);
//        frag.setArguments(bundle);
//
//        String TAG2 = "FragNewInstance";
//        Log.d(TAG2,"FragmentNewInstance*********");
//        return frag;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"OnCreate Method");
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
