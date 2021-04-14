package com.yw.report.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yw.report.R;
import com.yw.report.adapter.ShowUploadAdapter;
import com.yw.report.adapter.ShowUploadAdapterTag;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.UploadTag;
import com.yw.report.network.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.PUBLISH_WITH_TAG;
import static com.yw.report.network.Url.QUARY_WITH_TAG_PUBLISH;

public class HistoryActivity extends AppCompatActivity {
    ListView listView;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView=findViewById(R.id.listView);
        getPublishTag();

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(HistoryActivity.this,s,Toast.LENGTH_SHORT).show();
                        break;
                    case 2://上传成功  所有值恢复初始化
                      List <UploadTag.ListBean> lists=( List <UploadTag.ListBean>)msg.obj;
                        initListview(lists);
                        break;
                }
            }
        };
    }

    private void getPublishTag() {
        JSONObject jsonObject=new JSONObject();

        Call uploadfile = API.postWithToken(QUARY_WITH_TAG_PUBLISH, jsonObject.toString());
        uploadfile.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("APILOG","FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s=response.body().string();
                Log.d("APILOG","response"+s);
                try {
                    JSONObject object=new JSONObject(s);
                    String respDesc = object.optString("respCode");
                    if(respDesc.equalsIgnoreCase("0000")){
                        //获取成功
                        Gson gson=new Gson();
                        TypeToken<UploadTag> type=new TypeToken<UploadTag>(){};
                        UploadTag o = gson.fromJson(s, type.getType());
                        Message message=new Message();
                        message.what=2;
                        message.obj=o.getList();
                        handler.sendMessage(message);

                    }else {
                        Message message=new Message();
                        message.what=1;
                        message.obj=s;
                        handler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListview( List <UploadTag.ListBean> lists) {

        ShowUploadAdapterTag adapter=new ShowUploadAdapterTag(this,R.layout.item_adapter_tag,lists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HistoryActivity.this, HistoryDetailActivity.class);
                Data.nowTag=lists.get(position);
                startActivity(intent);

            }
        });
    }
}