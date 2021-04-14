package com.yw.report.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 导航
 */
import com.ycl.tabview.library.TabView;
import com.ycl.tabview.library.TabViewChild;
import com.yw.report.R;
import com.yw.report.fragment.DIscussion;
import com.yw.report.fragment.UploadTag;
import com.yw.report.fragment.Mine;

import java.util.ArrayList;
import java.util.List;

public class NavActivity extends AppCompatActivity {
    DIscussion dIscussion=new DIscussion();
    UploadTag uploadTag =new UploadTag();
    Mine mine=new Mine();
    TabView tabView;
    TabListener listener;
    List<TabViewChild> tabViewChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        init();
    }
    private void init() {
        tabView = (TabView) findViewById(R.id.tabView);
        tabView.setImageViewHeight(40);
        tabView.setTabViewHeight(20);
        tabView.setTextViewSize(12);



        //start add data

        tabViewChildList = new ArrayList<>();
        TabViewChild tabViewChild01 =
                new TabViewChild(R.drawable.discussion_yes, R.drawable.discussion_no, "Discussion", dIscussion);

        TabViewChild tabViewChild02 =
                new TabViewChild(R.drawable.add_yes, R.drawable.add, "upload", uploadTag);

//        TabViewChild tabViewChild03 =
//                new TabViewChild(R.drawable.nav_wrongbook_select, R.drawable.nav_wrongbook_unselect, "错题本", wrongBook);
        TabViewChild tabViewChild04 =
                new TabViewChild(R.drawable.mine_yes, R.drawable.mine_no, "Me", mine);



        tabViewChildList.add(tabViewChild01);
        tabViewChildList.add(tabViewChild02);
//        tabViewChildList.add(tabViewChild03);
        tabViewChildList.add(tabViewChild04);
        //end add data
        tabView.setTabViewDefaultPosition(0);
        tabView.setTextViewSelectedColor(0xff02F1C2);
        tabView.setTabViewChild(tabViewChildList, getSupportFragmentManager());
        listener = new TabListener();
        tabView.setOnTabChildClickListener(listener);
    }
    class TabListener implements TabView.OnTabChildClickListener {
        @Override
        public void onTabChildClick(int position, ImageView currentImageIcon, TextView currentTextView) {

//                Toast.makeText(getApplicationContext(),"position:"+position,Toast.LENGTH_SHORT).show();
        }
    }


}