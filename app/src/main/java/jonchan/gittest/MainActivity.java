package jonchan.gittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Jon", "Hi I'm Jonathan!");
        Log.d("Jon", "Hi I'm Wayne!");
        Log.d("Jon", "Hi I'm Jonathan!");
        Log.d("Jon", "Hi I'm Linda!!!!");
    }
}
