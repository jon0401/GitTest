package jonchan.gittest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Facitilies_BookingActivity extends Activity{

        private String[] room_type = new String[] {"Piano Room", "Band Room","Audio Room","Common Room"};
        private String[] room_num = new String[]{"101","102","103","104","105"};
        private String[][] pandc = new String[][]{{"101","102","103","104","105"},{"210","202","203"},{"301","302","303","304","305"},{"401","402","403","404"},{"501","502","503"}};
        private Spinner sp;
        private Spinner sp2;
        private Context context;
        ArrayAdapter<String> adapter ;
        ArrayAdapter<String> adapter2;
        private int pos;
        private int roomType=0;
        private int roomNum=0;
        private ImageView choosing_room;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.facitilies_booking);
            choosing_room=(ImageView)findViewById(R.id.choosing_room_back);

            /**
             * From the DB we obtain the data to assign to room types and number
             * * other Activity Data (Facilities\fees...）past from that activity
             */
            //obtaining database example
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //获取Reference  "message" stored as key
            final DatabaseReference myRef = database.getReference("room_type");
            final DatabaseReference myRef2 = database.getReference("room_num");

            //saving realtime data in db
           // myRef.setValue("");

            //listen to the event for change
           /* myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    room_type[0]= value;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "obtained abnormally", Toast.LENGTH_SHORT);
                   // Log.e(TAG, error.toException().toString());
                }
            });*/
            //listen to the event for change
          /*  myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    room_num[0]= value;
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "obtained abnormally", Toast.LENGTH_SHORT);
                    // Log.e(TAG, error.toException().toString());
                    // Log.e(TAG, error.toException().toString());
                }
            });*/

            choosing_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
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
        }

         private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
             public void onItemSelected(AdapterView parent, View v, int position, long id) {
                 pos = sp.getSelectedItemPosition();
                 adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pandc[pos]);
                 sp2.setAdapter(adapter2);
                 roomType++;
             }
             public void onNothingSelected(AdapterView arg0){}

         };

        private AdapterView.OnItemSelectedListener selectListener2 = new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView parent, View v, int position, long id) {
                roomNum++;
                /*new AlertDialog.Builder(Facitilies_BookingActivity.this).setTitle("System alert")//set dialog title
                        .setMessage("room_num:"+ (String) sp2.getSelectedItem()).show();*/
                //after selecting the room jump to Room_Information
                if(!(roomType==1&&roomNum==1||roomNum==2)){
                    Intent intent=new Intent();
                    intent.setClass(Facitilies_BookingActivity.this,RoomInformationActivity.class);
                    intent.putExtra("room_num", (String) sp2.getSelectedItem());
                    Facitilies_BookingActivity.this.startActivity(intent);
                }
            }
            public void onNothingSelected(AdapterView arg0){}
        };
}
