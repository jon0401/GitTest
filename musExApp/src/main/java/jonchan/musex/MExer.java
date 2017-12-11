package jonchan.musex;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jonchan on 16/11/2017.
 */

public class MExer extends Application {
    private static ArrayList <MQues> mQues;
    private static ArrayList <MQues> selectedQues;
    public static final String TAG = "Exception";
    public static int defaultNoQ = 8;
    Context context;

    public MExer(Context context){
        this.context = context;
    }


    public ArrayList<MQues> createExer(int NoQ, Difficulty diff, QType [] qt ) throws IOException {
        instantiateQues();
        selectedQues = new ArrayList<>();
        Collections.shuffle(mQues);
//        int stillNeed = NoQ;
        int mQuesSizeLeft = mQues.size()-1;

        int [] stillNeedArr = Utility.fairShareQuestion(NoQ, qt.length);

        while (mQuesSizeLeft != 0){
            MQues currentMQ = mQues.get(mQuesSizeLeft);
            if (diff == Difficulty.ALL){
                for (int k = 0; k < qt.length; k ++){
                    if (currentMQ.qtype == qt[k] && stillNeedArr[k] > 0) {
                        stillNeedArr[k]--;
                        selectedQues.add(currentMQ);
                        Log.d(TAG, "?????"+ mQuesSizeLeft+  "!!!!!!!!!!!!!!!!!****ALL DIFFICULTY SELECTED *****!!!!!!!!!!!!!!!!!");
                    }
                }
            } else {
                for (int k = 0; k < qt.length; k ++){
                    if ((currentMQ.diff == diff)&&(currentMQ.qtype == qt[k]) && stillNeedArr[k] > 0) {
                        stillNeedArr[k]--;
                        selectedQues.add(currentMQ);
                        Log.d(TAG, "?????"+ mQuesSizeLeft+"!!!!!!!!!!!!!!!!!****OTHER DIFFICULTY SELECTED *****!!!!!!!!!!!!!!!!!");
                    }
                }
            }
            mQuesSizeLeft--;
            boolean checkOk = true;
            for (int i = 0; i < stillNeedArr.length; i++){
                Log.d(TAG, "needArr value: " + stillNeedArr[i]);
                if (stillNeedArr[i] > 0){
                    checkOk = false;
                }
            }
            if (checkOk){
                break;
            }
        }

        Log.d(TAG, "SELECTED QUES SIZE::::" + selectedQues.size());
        if (selectedQues.size() != NoQ){
            Log.d(TAG, "Not enough question select!");
            return null;
        }
        else {
            return (selectedQues);
        }
    }

    public ArrayList<MQues> createExer (int NoQ, boolean b) throws IOException {     // if b is true then use default noOfQuestion; otherwise use NoQ
        instantiateQues();
        selectedQues = new ArrayList<>();
        Collections.shuffle(mQues);
        int stillNeed;
        int noOfQuestion;

        if (b){
            stillNeed = defaultNoQ;
            noOfQuestion = defaultNoQ;
        }
        else {
            stillNeed = NoQ;
            noOfQuestion = NoQ;
        }

        int mQuesSizeLeft = mQues.size()-1;
        Log.d(TAG, mQuesSizeLeft+"!!!!!!!!!!");


        while (mQuesSizeLeft != 0){
            MQues currentMQ = mQues.get(mQuesSizeLeft);
            stillNeed --;
            selectedQues.add(currentMQ);
            mQuesSizeLeft--;
            if (stillNeed == 0){
                break;
            }
        }

        if (selectedQues.size() != noOfQuestion){
            Log.d(TAG, "Not enough question select 2!");
            return null;
        }
        else {
            return (selectedQues);
        }
    }

    public ArrayList<MQues> testExer (int id) throws IOException {
        instantiateQues();
        selectedQues = new ArrayList<>();
        for (int i = 0 ; i < mQues.size(); i++){
            if (mQues.get(i).id == id){
                selectedQues.add(mQues.get(i));
                break;
            }
        }
        return selectedQues;

    }

    public List<Integer> createdExer_toID (int NoQ, Difficulty diff, QType [] qt ) throws IOException{
        instantiateQues();
        selectedQues = new ArrayList<>();
        Collections.shuffle(mQues);
        int mQuesSizeLeft = mQues.size()-1;

        int [] stillNeedArr = Utility.fairShareQuestion(NoQ, qt.length);

        while (mQuesSizeLeft != 0){
            MQues currentMQ = mQues.get(mQuesSizeLeft);
            if (diff == Difficulty.ALL){
                for (int k = 0; k < qt.length; k ++){
                    if (currentMQ.qtype == qt[k] && stillNeedArr[k] > 0) {
                        stillNeedArr[k]--;
                        selectedQues.add(currentMQ);
                    }
                }
            } else {
                for (int k = 0; k < qt.length; k ++){
                    if ((currentMQ.diff == diff)&&(currentMQ.qtype == qt[k]) && stillNeedArr[k] > 0) {
                        stillNeedArr[k]--;
                        selectedQues.add(currentMQ);
                    }
                }
            }
            mQuesSizeLeft--;
            boolean checkOk = true;
            for (int i = 0; i < stillNeedArr.length; i++){
                if (stillNeedArr[i] > 0){
                    checkOk = false;
                }
            }
            if (checkOk){
                break;
            }
        }

        if (selectedQues.size() != NoQ){
            Log.d(TAG, "Not enough question select!");
            return null;
        }
        else {
            List<Integer> passOutIndexList = new ArrayList<>();
            for (int i = 0; i < selectedQues.size(); i++){
                passOutIndexList.add(selectedQues.get(i).id);
            }
            return (passOutIndexList);
        }
    }

    public ArrayList<MQues> createExer_id (List<Integer> content) throws IOException{
        instantiateQues();
        selectedQues = new ArrayList<>();
        List<Integer> myContent = new ArrayList<>();
        for (int i = 0; i < content.size(); i++){
            myContent.add(content.get(i));
        }


        Comparator<MQues> c = new Comparator<MQues>() {
            @Override
            public int compare(MQues o1, MQues o2) {
                return o1.getId() - o2.getId();
            }
        };

        for (int i = 0; i < content.size(); i++){
            int index = Collections.binarySearch(mQues, new MQues(content.get(i), new String[] {}, null, new String[] {}, null, null), c);
            selectedQues.add(mQues.get(index));
        }


        if (selectedQues.size() != content.size()){
            Log.d(TAG, "Some problem!!!");
            return null;
        }
        else {
            return (selectedQues);
        }

    }



    private void instantiateQues() throws IOException {
        //public MQues (int id, String [] notes, String ans, String [] ansRelated, Difficulty diff, QType qtype)
        mQues = new ArrayList<>();

        InputStream inputStream = context.getAssets().open("INT.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String strRead;
        while ((strRead = br.readLine()) != null){
            String splitarray[] = strRead.split("\t");
            int id = Integer.parseInt(splitarray [0]);
            String qtype = splitarray [1];
            QType qtypeEnum = QType.valueOf(qtype);
            String diff = splitarray [2];
            Difficulty diffEnum = Difficulty.valueOf(diff);
            String ans = splitarray [3];
            String relatedAns = splitarray [4];
            List<String> rAns = Arrays.asList(relatedAns.split("\\s*,\\s*"));
            String [] rAns_arr = rAns.toArray(new String[rAns.size()]);
            String musicNotes = splitarray [5];
            List<String> mNotes = Arrays.asList(musicNotes.split("\\s*,\\s*"));
            String [] mNotes_arr = mNotes.toArray(new String[mNotes.size()]);
            mQues.add(new MQues(id,mNotes_arr,ans,rAns_arr,diffEnum,qtypeEnum));
        }
        br.close();
        Log.d(TAG, "READABLE!!!!");
    }

}
