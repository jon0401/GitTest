package jonchan.musex;
import android.support.annotation.Keep;
import android.util.Log;

import java.io.Serializable;
import java.util.*;

/**
 * Created by jonchan on 16/11/2017.
 */

enum QType{
    SCALE_T, SCALE_B, ARPEGGIO_T, ARPEGGIO_B, INTERVAL_T, INTERVAL_B
}

enum Difficulty{
    EASY, MEDIUM, HARD, ALL
}

public class MQues implements Serializable {

    public int id;
    //private String [] ansRelated;

    public List <String> ansRelated;
    public String ans;
    public String notes;
    private StringBuffer notesBuffer;
    public QType qtype;
    public Difficulty diff;
    private final String TAG = "Exception";

    public MQues() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Keep
    public MQues (int id, String [] notes, String ans, List <String> ansRelated, Difficulty diff, QType qtype){

        this.id = id;
        this.ans = ans;
        this.diff = diff;
        this.qtype = qtype;

        this.ansRelated = new ArrayList<>();
        for (int i = 0; i < ansRelated.size(); i++){
            this.ansRelated.add(ansRelated.get(i));
        }

        MNotes mn = MNotes.getInstance();
        Map<String,String> mBMap = mn.getBMap();
        Map<String,String> mTMap = mn.getTMap();

        /** convert string of notes name (e.g. ["c5", "c4"]) to String of UNICODE **/
        notesBuffer = new StringBuffer();
        switch (qtype){
            case ARPEGGIO_T:
            case INTERVAL_T:
            case SCALE_T: notesBuffer.append(mTMap.get("tclef"));
                          for (int i = 0; i < notes.length; i++){
                                if (mTMap.containsKey(notes[i])){
                                    notesBuffer.append(mTMap.get(notes[i]));
                                }
                                else {
                                    break;
                                }
                          }
                          break;
            case ARPEGGIO_B:
            case INTERVAL_B:
            case SCALE_B: notesBuffer.append(mBMap.get("bclef"));
                          for (int i = 0; i < notes.length; i++){
                                if (mBMap.containsKey(notes[i])){
                                    notesBuffer.append(mBMap.get(notes[i]));
                                }
                                else {
                                    break;
                                }
                          }
                          break;
            default:      Log.d(TAG, "MQues_switch error!");
                          break;
        }
        notesBuffer.append(mTMap.get("end"));
        notesBuffer.append(mTMap.get("end"));
        this.notes = notesBuffer.toString();
    }

}


