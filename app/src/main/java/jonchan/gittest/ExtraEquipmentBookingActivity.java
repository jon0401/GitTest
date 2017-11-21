package jonchan.gittest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraEquipmentBookingActivity extends Activity{

    private Spinner spinner;
    List<Map<String,Object>> dataList;
    List<String> dataList2;
    List<String> dataList3;
    private Spinner extra_equipment_spinner2;
    private Spinner extra_equipment_spinner3;
    private ArrayAdapter<String> arr_adapter;
    private ArrayAdapter<String> arr_adapter2;
    private ImageView extra_equipment_booking_back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_equipment_booking);
        spinner = (Spinner) findViewById(R.id.extra_equipment_spinner);
        extra_equipment_spinner2=(Spinner)findViewById(R.id.extra_equipment_spinner2);
        extra_equipment_spinner3=(Spinner)findViewById(R.id.extra_equipment_spinner3);
        extra_equipment_booking_back=(ImageView) findViewById(R.id.extra_equipment_booking_back);
        extra_equipment_booking_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Model selection
        dataList2 = new ArrayList<String>();//database
        dataList2.add("YAMAHA");
        //adapter
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList2);
        //setting style
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter
        extra_equipment_spinner2.setAdapter(arr_adapter);

        //Quantity selection
        dataList3 = new ArrayList<String>();//database
        dataList3.add("1");dataList3.add("2");
        dataList3.add("3");dataList3.add("4");
        dataList3.add("5");dataList3.add("6");
        //adapter
        arr_adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList3);
        //setting style
        arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter
        extra_equipment_spinner3.setAdapter(arr_adapter2);

        //datasource
        dataList = new ArrayList<>();
        //create a SimpleAdapter
        //第一个参数：上下文，第二个参数：数据源，第三个参数：item子布局，第四、五个参数：键值对，获取item布局中的控件id
        final SimpleAdapter s_adapter = new SimpleAdapter(this, getDate(), R.layout.extra_equipment_type_item, new String[]{ "image","text"}, new int[]{R.id.img,R.id.text});
        //connect the controller with adapter
        spinner.setAdapter(s_adapter);
    }
    //database source
    private List<Map<String,Object>> getDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("image", R.drawable.headphone);
        map.put("text", "Headphone");
        dataList.add(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("image", R.drawable.methronome);
        map1.put("text", "Metronome");
        dataList.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("image", R.drawable.music_stand);
        map2.put("text", "Music Stand");
        dataList.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("image", R.drawable.tuner);
        map3.put("text", "Tuner");
        dataList.add(map3);
        return dataList;
    }
}
