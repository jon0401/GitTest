package jonchan.gittest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private Button btnAddLesson;
    private Button btnDisplayLesson;
    private Button btnLogout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){  //has not logined
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try{
                        startActivity(myIntent);
                    }catch(android.content.ActivityNotFoundException e){
                        e.printStackTrace();
                    }

                }
            }
        };





        btnAddLesson = (Button) findViewById(R.id.btnAddLesson);
        btnDisplayLesson = (Button) findViewById(R.id.btnDisplayLesson);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddLessonActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }

            } });

        btnDisplayLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), DisplayLessonActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
        Log.d("Jon", "Hi I'm Jonathan!");
        Log.d("Jon", "Hi I'm Wayne!");
        Log.d("Jon", "Hi I'm Jonathan!");
        Log.d("Jon", "Hi I'm Erwin!!!");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

}