package jonchan.gittest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Facitilies_BookingActivity extends BaseActivity{

        private String[] room_type = new String[] {"Piano Room", "Band Room","Audio Room","Common Room"};
        private String[] room_num = new String[]{"101","102","103","104","105"};
        private String[][] pandc = new String[][]{{"101","102","103","104","105"},{"210","202","203"},{"301","302","303","304","305"},{"401","402","403","404"},{"501","502","503"}};
        private Spinner sp;
        private Spinner sp2;
        private Button btnNext;
        private Context context;
        ArrayAdapter<String> adapter ;
        ArrayAdapter<String> adapter2;
        private int pos;
        private int roomType=0;
        private int roomNum=0;
        private FirebaseAuth mAuth;

        private Typeface tfrb;
        private Typeface tfrm;
        private TextView tvt;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.facitilies_booking);
            setTitle("BOOKING");

            navigationView = (BottomNavigationView) findViewById(R.id.navigation);
            navigationView.setOnNavigationItemSelectedListener(this);

            btnNext = (Button) findViewById(R.id.btnNext);
            tfrb = Typeface.createFromAsset(getAssets(), "robotobold.ttf");
            tfrm = Typeface.createFromAsset(getAssets(), "robotomedium.ttf");            /**
             * From the DB we obtain the data to assign to room types and number
             * * other Activity Data (Facilities\fees...）past from that activity
             */
            //obtaining database example
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference myRef = database.getReference("room_type");
            final DatabaseReference myRef2 = database.getReference("room_num");
            mAuth = FirebaseAuth.getInstance();
            final String user_id = mAuth.getCurrentUser().getUid();


            AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView parent, View v, int position, long id) {
                    pos = sp.getSelectedItemPosition();
                    adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pandc[pos]);
                    sp2.setAdapter(adapter2);
                    roomType++;
                }
                public void onNothingSelected(AdapterView arg0){}

            };

            AdapterView.OnItemSelectedListener selectListener2 = new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView parent, View v, int position, long id) {
                    roomNum++;

                }
                public void onNothingSelected(AdapterView arg0){}
            };



            tvt = (TextView) findViewById(R.id.choosing_room_textview1);
            tvt.setTypeface(tfrb);
            context = this;
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, room_type);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp = (Spinner) findViewById(R.id.room_type);
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(selectListener);

            adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, room_num);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2 = (Spinner) findViewById(R.id.room_num);
            sp2.setAdapter(adapter2);
            sp2.setOnItemSelectedListener(selectListener2);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.setClass(Facitilies_BookingActivity.this,RoomInformationActivity.class);
                    intent.putExtra("room_num", (String) sp2.getSelectedItem());
                    Facitilies_BookingActivity.this.startActivity(intent);
                }
            });

        }


    @Override
    int getContentViewId() {
        return R.layout.facitilies_booking;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_booking;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                Intent myIntent = new Intent(this, LoginActivity.class);
                try{
                    startActivity(myIntent);
                }catch(android.content.ActivityNotFoundException e){
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
