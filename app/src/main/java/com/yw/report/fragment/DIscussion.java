package com.yw.report.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yw.report.R;
import com.yw.report.activity.Data;
import com.yw.report.activity.DetailActivity;
import com.yw.report.activity.HistoryActivity;
import com.yw.report.activity.HistoryDetailActivity;
import com.yw.report.activity.SettingActivity;
import com.yw.report.activity.UploadActivity;
import com.yw.report.adapter.ShowUploadAdapter;
import com.yw.report.adapter.ShowUploadAdapterTag;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.UploadTag;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.QUARY_PUBLISH;
import static com.yw.report.network.Url.QUARY_WITH_TAG_PUBLISH;

public class DIscussion extends Fragment implements View.OnClickListener {
    ImageView add;
    static Handler handler;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_discussion,null);
        initView(v);
        getPublishTag();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        List <UploadNoTag.ListBean> lists=( List <UploadNoTag.ListBean>)msg.obj;
                        initListview(lists);

                        break;
                }
            }
        };
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPublishTag();
    }

    private void getPublishTag() {
        JSONObject jsonObject=new JSONObject();

        Call uploadfile = API.postWithToken(QUARY_PUBLISH, jsonObject.toString());
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
                        TypeToken<UploadNoTag> type=new TypeToken<com.yw.report.bean.UploadNoTag>(){};
                        com.yw.report.bean.UploadNoTag o = gson.fromJson(s, type.getType());
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

    private void initListview( List <UploadNoTag.ListBean> lists) {

        ShowUploadAdapter adapter=new ShowUploadAdapter(getActivity(),R.layout.item_adapter,lists);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailActivity.class);
                Data.now=lists.get(position);
                startActivity(intent);

            }
        });
    }

    private void initView(View v) {
        add=v.findViewById(R.id.add);
        listView=v.findViewById(R.id.listView);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                Intent intent=new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);

            break;
        }
    }
}
