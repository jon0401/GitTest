package jonchan.gittest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
    private ImageView book_room_back;
    private TextView textView;
    private Button book_room_button1;
    private Button book_room_button2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_room);
        book_room_edittext1=(EditText)findViewById(R.id.book_room_edittext1);
        spinner=(Spinner)findViewById(R.id.book_room_timeslot);
        book_room_back=(ImageView)findViewById(R.id.book_room_back);
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
        String str=bundle.getString("room_num");//getString()return to the specified key value 返回指定key的值
        textView=(TextView)findViewById(R.id.book_room_textview1);//use TextView to show the value
        textView.setText(str);
        book_room_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //time choosing
        data_list = new ArrayList<String>();//database
        data_list.add("9:00am-9:30am");
        data_list.add("9:30am-10:00am");
        data_list.add("10:00am-10:30am");
        data_list.add("10:30am-11:00am");
        data_list.add("11:00am-12:00am");

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
}
