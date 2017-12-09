package jonchan.musex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jonchan on 19/11/2017.
 */

public class FragmentQuesResult extends Fragment {

    double timeElapsed, averageSec;
    int score, totalQuestion;

    TextView percentageT, correctedAns, averageTime;
    Button returnToActivity;

    String gameID;

    final String TAG = "FragmentQuesResult";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.playexer_result, container, false);

        timeElapsed = getArguments().getDouble("timeElapsed");
        score = getArguments().getInt("score");
        totalQuestion = getArguments().getInt("totalQuestionNo");
        gameID = getArguments().getString("gameID");

        percentageT = (TextView) view.findViewById(R.id.percentage);
        correctedAns = (TextView) view.findViewById(R.id.correctedAns);
        averageTime = (TextView) view.findViewById(R.id.averageTime);

        final int percentage = (score*100) / (totalQuestion);
        percentageT.setText(percentage + "%");
        correctedAns.setText(score + "/" + totalQuestion);
        averageSec = timeElapsed / totalQuestion;
        averageTime.setText(Utility.secondsToMinutes(averageSec));

        returnToActivity = (Button) view.findViewById(R.id.returnButton);
        returnToActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent returnMainActivity = new Intent();
                returnMainActivity.putExtra("score", score);
                returnMainActivity.putExtra("percentage", percentage);
                returnMainActivity.putExtra("averageTime", averageSec);
                returnMainActivity.putExtra("gameID", gameID);
                getActivity().setResult(RESULT_OK, returnMainActivity);
                getActivity().finish();
            }
        });

        return view;
    }
}
