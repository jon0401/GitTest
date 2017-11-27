package jonchan.musex;
import android.util.Log;

import java.io.Serializable;
import java.util.*;

public class MQues implements Serializable {

    protected int id;
    protected String [] ansRelated;
    protected String ans;
    protected String notes;
    protected StringBuffer notesBuffer;
    protected QType qtype;
    protected Difficulty diff;
    final String TAG = "Exception";

    public MQues (int id, String [] notes, String ans, String [] ansRelated, Difficulty diff, QType qtype){

        this.id = id;
        this.ans = ans;
        this.diff = diff;
        this.qtype = qtype;

        this.ansRelated = new String [ansRelated.length];
        for (int i = 0; i < ansRelated.length; i++){
            this.ansRelated[i] = ansRelated[i];
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


