package jonchan.gittest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RoomInformationActivity extends Activity {

    private TextView textView ;
    private ImageView room_information_back;
    private Button room_information_button;
    private String str;

    private ViewPager viewPager;
    private RadioGroup group;
    //image source
    private int[] imageIds = {R.drawable.room_1,R.drawable.room_2,R.drawable.room_3,R.drawable.piano_room};
    //save images
    private List<ImageView> mList;

    private int index = 0,preIndex = 0;

    private boolean isContinue = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_information);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        group = (RadioGroup) findViewById(R.id.group);
        mList = new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setOnTouchListener(onTouchListener);
        initRadioButton(imageIds.length);

        room_information_back=(ImageView)findViewById(R.id.room_information_back);
        room_information_button=(Button)findViewById(R.id.room_information_button);
        //Obtain value from Fccitilies_BookingActivity
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        str=bundle.getString("room_num");
        textView=(TextView)findViewById(R.id.room_num);
        textView.setText(str);
        room_information_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        room_information_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent();
                intent1.setClass(RoomInformationActivity.this,Book_RoomActivity.class);
                intent1.putExtra("room_num", "ROOM "+str);
                startActivity(intent1);
            }
        });
    }
    /**
     * according to image number to do the button initialization
     * @param length
     */
    private void initRadioButton(int length) {
        for(int i = 0;i<length;i++){
            ImageView imageview = new ImageView(this);
           // imageview.setImageResource(R.drawable.rg_selector);
            imageview.setPadding(20,0,0,0);

            group.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            group.getChildAt(0).setEnabled(false);
        }
    }

    View.OnTouchListener onTouchListener  = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                default:
                    isContinue = true;
            }
            return false;
        }
    };
    /**
     * set the button in the current page
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            index = position;
            setCurrentDot(index%imageIds.length);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**
     * set the status of the buttons
     * @param i current position
     */
    private void setCurrentDot(int i) {
        if(group.getChildAt(i)!=null){
            group.getChildAt(i).setEnabled(false);
        }
        if(group.getChildAt(preIndex)!=null){
            group.getChildAt(preIndex).setEnabled(true);
            preIndex = i;
        }
    }
    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position%imageIds.length;
            ImageView imageView = new ImageView(RoomInformationActivity.this);
            imageView.setImageResource(imageIds[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            mList.add(imageView);
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    };

}
