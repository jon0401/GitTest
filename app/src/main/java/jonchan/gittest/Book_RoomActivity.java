package jonchan.gittest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Book_RoomActivity extends BaseActivity{

    private EditText book_room_edittext1;
    private Spinner spinner;
    private Spinner book_room_location;
    private List<String> data_list;
    private List<String> data_list2;
    private ArrayAdapter<String> arr_adapter;
    private ArrayAdapter<String> arr_adapter2;
    private TextView textView;
    private Button book_room_button1;
    private Button book_room_button2;
    private FirebaseAuth mAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_room);
        setTitle("BOOKING");
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        book_room_edittext1=(EditText)findViewById(R.id.book_room_edittext1);
        spinner=(Spinner)findViewById(R.id.book_room_timeslot);
        book_room_location=(Spinner)findViewById(R.id.book_room_location);
        book_room_button1=(Button)findViewById(R.id.book_room_button1);
        book_room_button2=(Button)findViewById(R.id.book_room_button2);
        book_room_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Book_RoomActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
        book_room_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent();
                intent2.setClass(Book_RoomActivity.this,ExtraEquipmentBookingActivity.class);
                startActivity(intent2);
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras() will get the extra data brought by intent
        String str=bundle.getString("room_num");
        textView=(TextView)findViewById(R.id.book_room_textview1);//use TextView to show the value
        textView.setText(str);

        //time choosing
        data_list = new ArrayList<String>();//database
        data_list.add("09:00am-10:00am");
        data_list.add("10:00am-11:00am");
        data_list.add("11:00am-12:00pm");
        data_list.add("12:00pm-13:00pm");
        data_list.add("13:00pm-14:00pm");
        data_list.add("14:00pm-15:00pm");
        data_list.add("15:00pm-16:00pm");
        data_list.add("16:00pm-17:00pm");
        data_list.add("17:00pm-18:00pm");
        data_list.add("18:00pm-19:00pm");
        data_list.add("19:00pm-20:00pm");
        data_list.add("20:00pm-21:00pm");

        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);

        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arr_adapter);

        //position selection
        data_list2 = new ArrayList<String>();//database
        data_list2.add("Facing south");
        data_list2.add("near aisle");
        data_list2.add("Near the lobby");
        data_list2.add("Near the doorway");

        arr_adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list2);

        arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        book_room_location.setAdapter(arr_adapter2);
        //data selection
        book_room_edittext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickDlg();

            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.book_room;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_booking;
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Book_RoomActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Book_RoomActivity.this.book_room_edittext1.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
