package jonchan.musex;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonchan on 16/11/2017.
 */

public class MNotes {
    private static MNotes instance = null;
    private  Map<String,String> mTMap = new HashMap<String,String>();
    private Map<String,String> mBMap = new HashMap<String,String>();


    private MNotes(){
//        Map<String,String> mTMap = new HashMap<String,String>();
//        Map<String,String> mBMap = new HashMap<String,String>();

        mTMap.put("f3","\u0076");
        mTMap.put("f#3","\u0056");
        mTMap.put("gb3","\u222B");
        mTMap.put("g3","\u0062");
        mTMap.put("g#3","\u0042");
        mTMap.put("ab3","\u02DC");
        mTMap.put("a3","\u006E");
        mTMap.put("a#3","\u004E");
        mTMap.put("bb3","\u00B5");
        mTMap.put("b3","\u006D");
        mTMap.put("b#3","\u004D");
        mTMap.put("cb4","\u00E5");
        mTMap.put("c4","\u0061");
        mTMap.put("c#4","\u0041");
        mTMap.put("db4","\u00DF");
        mTMap.put("d4","\u0073");
        mTMap.put("d#4","\u0053");
        mTMap.put("eb4","\u2202");
        mTMap.put("e4","\u0064");
        mTMap.put("e#4","\u0044");
        mTMap.put("fb4","\u0192");
        mTMap.put("f4","\u0066");
        mTMap.put("f#4","\u0046");
        mTMap.put("gb4","\u00A9");
        mTMap.put("g4","\u0067");
        mTMap.put("g#4","\u0047");
        mTMap.put("ab4","\u02D9");
        mTMap.put("a4","\u0068");
        mTMap.put("a#4","\u0048");
        mTMap.put("bb4","\u2206");
        mTMap.put("b4","\u006A");
        mTMap.put("b#4","\u004A");
        mTMap.put("cb5","\u0153");
        mTMap.put("c5","\u0071");
        mTMap.put("c#5","\u0051");
        mTMap.put("db5","\u2211");
        mTMap.put("d5","\u0077");
        mTMap.put("d#5","\u0057");
        mTMap.put("eb5","\u00B4");
        mTMap.put("e5","\u0065");
        mTMap.put("e#5","\u0045");
        mTMap.put("fb5","\u00AE");
        mTMap.put("f5","\u0072");
        mTMap.put("f#5","\u0052");
        mTMap.put("gb5","\u2020");
        mTMap.put("g5","\u0074");
        mTMap.put("g#5","\u0054");
        mTMap.put("ab5","\u00A5");
        mTMap.put("a5","\u0079");
        mTMap.put("a#5","\u0059");
        mTMap.put("bb5","\u00A8");
        mTMap.put("b5","\u0075");
        mTMap.put("b#5","\u0055");
        mTMap.put("cb6","\u00A1");
        mTMap.put("c6","\u0031");
        mTMap.put("c#6","\u0021");
        mTMap.put("db6","\u2122");
        mTMap.put("d6","\u0032");
        mTMap.put("d#6","\u0040");
        mTMap.put("eb6","\u00A3");
        mTMap.put("e6","\u0033");
        mTMap.put("s-space","\u003D");
        mTMap.put("tclef","\u0026");
        mTMap.put("end","\u007C");

        mBMap.put("c2","\u006E");
        mBMap.put("c#2","\u004E");
        mBMap.put("db2","\u00B5");
        mBMap.put("d2","\u006D");
        mBMap.put("d#2","\u004D");
        mBMap.put("eb2","\u00E5");
        mBMap.put("e2","\u0061");
        mBMap.put("e#2","\u0041");
        mBMap.put("fb2","\u00DF");
        mBMap.put("f2","\u0073");
        mBMap.put("f#2","\u0053");
        mBMap.put("gb2","\u2202");
        mBMap.put("g2","\u0064");
        mBMap.put("g#2","\u0044");
        mBMap.put("ab2","\u0192");
        mBMap.put("a2","\u0066");
        mBMap.put("a#2","\u0046");
        mBMap.put("bb2","\u00A9");
        mBMap.put("b2","\u0067");
        mBMap.put("b#2","\u0047");
        mBMap.put("cb3","\u02D9");
        mBMap.put("c3","\u0068");
        mBMap.put("c#3","\u0048");
        mBMap.put("db3","\u2206");
        mBMap.put("d3","\u006A");
        mBMap.put("d#3","\u004A");
        mBMap.put("eb3","\u0153");
        mBMap.put("e3","\u0071");
        mBMap.put("e#3","\u0051");
        mBMap.put("fb3","\u2211");
        mBMap.put("f3","\u0077");
        mBMap.put("f#3","\u0057");
        mBMap.put("gb3","\u00B4");
        mBMap.put("g3","\u0065");
        mBMap.put("g#3","\u0045");
        mBMap.put("ab3","\u00AE");
        mBMap.put("a3","\u0072");
        mBMap.put("a#3","\u0052");
        mBMap.put("bb3","\u2020");
        mBMap.put("b3","\u0074");
        mBMap.put("b#3","\u0054");
        mBMap.put("cb4","\u00A5");
        mBMap.put("c4","\u0079");
        mBMap.put("c#4","\u0059");
        mBMap.put("db4","\u00A8");
        mBMap.put("d4","\u0075");
        mBMap.put("d#4","\u0055");
        mBMap.put("eb4","\u00A1");
        mBMap.put("e4","\u0031");
        mBMap.put("s-space","\u003D");
        mBMap.put("bclef","?");
        mBMap.put("end","\u007C");
    }

    public static MNotes getInstance(){
        if (instance == null)
            instance = new MNotes();
        return instance;
    }

    public Map <String, String> getTMap(){
        return mTMap;
    }

    public Map <String, String> getBMap(){
        return mBMap;
    }

}
