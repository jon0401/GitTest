package jonchan.musex;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jonchan on 18/11/2017.
 */

public class FragmentQues2 extends Fragment {

    private MQues mQuesObject;
    private static final String mQuesObject_KEY = "mQuesObject_key";

    TextView myMNote;
    Spinner dropDown;
    final String TAG = "FragQues";


    public static FragmentQues newInstance (MQues mQues){
        FragmentQues frag = new FragmentQues();
        Bundle bundle = new Bundle();
        bundle.putSerializable(mQuesObject_KEY, mQues);
        frag.setArguments(bundle);

        String TAG2 = "FragNewInstance";
        Log.d(TAG2,"FragmentNewInstance*********");
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"OnCreate Method");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mQuesObject = (MQues) getArguments().getSerializable("bkey");
        Log.d(TAG,"OnCreateView Method");
        return inflater.inflate(R.layout.playexer,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"OnActivityCreated Method");
        super.onActivityCreated(savedInstanceState);
        String myS;
        myS = mQuesObject.notes;

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/STAFCPE_.TTF");
        myMNote = (TextView) getActivity().findViewById(R.id.mNote);
        myMNote.setTypeface(custom_font);
        myMNote.setText(myS);


        ArrayList<String> dropDownItems = new ArrayList<>();
        int [] randSelectedIndex = Utility.getRandomArray(3, mQuesObject.ansRelated.length);  // 3 and 0 needs to be modified
        dropDownItems.add(mQuesObject.ans); // always 0 becoz must store the ans to dropdownlist
        for (int i = 0; i < randSelectedIndex.length; i++){
            dropDownItems.add(mQuesObject.ansRelated[randSelectedIndex[i]]);
        }
        Collections.shuffle(dropDownItems);
        dropDown = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropDownItems);
        dropDown.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG,"OnSavedInstanceState Method");
        super.onSaveInstanceState(outState);
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
}
