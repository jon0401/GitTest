package jonchan.musex;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jonchan on 23/11/2017.
 */

public class testQues implements Serializable {

    public String ans;
    public String notesStr;
    private StringBuffer notess;
    public Difficulty diff;
    private final String TAG = "Fireb";

    public testQues(){

    }

    public testQues (String ans, String [] notes, Difficulty d){
        this.ans = ans;
        this.diff = d;
        notess = new StringBuffer();
        for (int i = 0 ; i < notes.length; i ++){
            //Log.d(TAG, "my NOTE: " + notes[i]);
            notess.append(notes[i]);
        }
        this.notesStr = notess.toString();
    }
}
