package com.yw.report.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yw.report.R;
import com.yw.report.adapter.ShowImageAdapter;

public class HistoryDetailActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        listView=findViewById(R.id.listview);
        showTopic();
        initListView();
    }

    private void initListView() {
        ShowImageAdapter adapter=new ShowImageAdapter(this,R.layout.item_show_image,Data.nowTag.getResources());
        listView.setAdapter(adapter);
    }

    private void showTopic() {
        ImageView imageView = findViewById(R.id.image);
        TextView name = findViewById(R.id.name);
        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        TextView commentNum = findViewById(R.id.comment);
        TextView praiseNum = findViewById(R.id.praise);
        TextView date = findViewById(R.id.date);
        TextView tag = findViewById(R.id.tag);
        tag.setText(Data.nowTag.getTag());
        name.setText(Data.loginUser.getName());
        title.setText(Data.nowTag.getTitle());
        if(Data.nowTag.getContent()!=null){
            content.setText(Data.nowTag.getContent());
        }else {
            content.setText("");
        }


        date.setText(Data.nowTag.getCreateTime());
    }

}