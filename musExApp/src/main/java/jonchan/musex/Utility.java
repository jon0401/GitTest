package jonchan.musex;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jonchan on 17/11/2017.
 */

public abstract class Utility {

    /** this function is only suitable for getting random array from 1..N -- only for related answer selection!! **/
    public static int [] getRandomArray (int HowMany, int arrayLength){
        ArrayList<Integer> usedInt = new ArrayList<>();
        int [] returnArray = new int [HowMany];
        usedInt.add(0); /**due to this line **/
        for (int i = 0; i < HowMany; i++){
            int rnd = new Random().nextInt(arrayLength);
            if (!usedInt.contains(rnd)){
                returnArray[i] = rnd;
                usedInt.add(rnd);
            }
            else {
                i = i - 1;
            }
        }
        return returnArray;
    }

    public static String secondsToMinutes (double sec){

        NumberFormat formatter = new DecimalFormat("#0.00");
        int min;
        double seconds;
        String secString;
        min = (int) sec / 60;
        seconds = sec % 60;
        secString = formatter.format(seconds);
//
//        final String tag = "asd";
//        Log.d(tag,"seconds!!!! NOOOOOOOOOOOOOfunctionL::::: " + sec % 16);
//        Log.d(tag,"seconds!!!! functionL::::: " + secString);
        if (min == 0){
            return (secString + "s");
        }
        else {
            return (min + "m " + secString + "s");
        }
    }

    public static int [] fairShareQuestion (int totalQuestion, int totalType) {
        int [] arr;

        if (totalQuestion >= totalType){
            arr = new int [totalType];
        }
        else {
            arr = new int [totalQuestion];
        }

        for (int i = 0 ; i < arr.length; i ++){
            arr[i] = 0;
        }

        int counter = totalQuestion;
        while (counter > 0){
            for (int i = 0 ; i < totalType; i++){
                arr[i] += 1;
                counter--;
                if (counter == 0) {
                    break;
                }
            }
        }
        return arr;
    }
}
