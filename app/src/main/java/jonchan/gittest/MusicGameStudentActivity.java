package jonchan.gittest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jonchan.musex.*;
/**
 * Created by jonchan on 9/12/2017.
 */

public class MusicGameStudentActivity extends BaseActivity {

    /** firebase **/
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mus_game_student_activity_main);

        setTitle("GAME");

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        /** Dummy value for list **/
        List<Integer> content = new ArrayList<>();
        List<GameItem> allGame = new ArrayList<>();

        for (int i = 0; i < 10; i ++){
            content.add(i);
        }

        for (int i = 0 ; i < 10 ; i ++){
            //allGame.add(new GameItem("Mixed","Teacher X","EASY", content));
        }

        GameItem[] gameArray = allGame.toArray(new GameItem[allGame.size()]);
        //GameItem[] arr = allGame.toArray();
        /** Dummy value for list **/


        ListAdapter theAdpater = new myAdapter(this, gameArray);

        ListView theListView = (ListView) findViewById(R.id.gameListView);
        theListView.setAdapter (theAdpater);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GameItem game = (GameItem) adapterView.getAdapter().getItem(position);
                String s = game.type;
                Toast.makeText(MusicGameStudentActivity.this, s, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    int getContentViewId() {
        return R.layout.mus_game_student_activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_game;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent myIntent = new Intent(this, LoginActivity.class);
                try {
                    startActivity(myIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private class myAdapter extends ArrayAdapter <GameItem> {
        public myAdapter(@NonNull Context context, GameItem[] gi) {
            super(context, R.layout.row_layout, gi);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater theInflater = LayoutInflater.from(getContext());

            View theView = theInflater.inflate(R.layout.row_layout, parent, false);
            //we don't want to inflate it into parent -> so we put false

            GameItem game = getItem(position);
            String type = game.type;
            String createdBy = game.createdBy;
            String diff = game.difficulty;

            TextView typeText = (TextView) theView.findViewById(R.id.gameSelect_gameTypeText);
            TextView creatorText = (TextView) theView.findViewById(R.id.gameSelect_createdByText);
            TextView diffText = (TextView) theView.findViewById(R.id.gameSelect_difficultyText);
            typeText.setText(type);
            creatorText.setText(createdBy);
            diffText.setText(diff);

            return (theView);

        }
    }
}


/*
class GameItem {

    public String type;
    public String createdBy;
    public String difficulty;
    public List<Integer> content;

    public GameItem(String t, String c, String d, List<Integer> myContent){
        this.type = t;
        this.createdBy = c;
        this.difficulty = d;

        content = new ArrayList<>();
        for (int i = 0; i < content.size(); i++){
            content.add(myContent.get(i));
        }
    }
}
*/