package com.yw.report.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yw.report.LoginActivity;
import com.yw.report.R;
import com.yw.report.activity.Data;
import com.yw.report.activity.NavActivity;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.LOGIN;
import static com.yw.report.network.Url.USER_INFO;

//无TAG的
public class ShowUploadAdapter extends ArrayAdapter<UploadNoTag.ListBean> {
    private final int resourceId;
    static Handler handler;
    public ShowUploadAdapter(@NonNull Context context, int resource, @NonNull List<UploadNoTag.ListBean> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UploadNoTag.ListBean bean=getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView name               =view.findViewById(R.id.name);
//        handler=new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        String s=(String)msg.obj;
//                        name         .setText(s);
//                        break;
//
//                }
//            }
//        };
       ImageView imageView                 =view.findViewById(R.id.image);

        TextView title                  =view.findViewById(R.id.title);
        TextView content                =view.findViewById(R.id.content);
        TextView commentNum                 =view.findViewById(R.id.comment);
        TextView praiseNum                  =view.findViewById(R.id.praise);
        TextView date                       =                      view.findViewById(R.id.date);

            name.setText(bean.getNickName());
        title        .setText(bean.getTitle());
        content      .setText(bean.getContent());
        commentNum   .setText(bean.getCommentNum()+"");
        praiseNum    .setText(bean.getLikeNum()+"");
        date         .setText(bean.getCreateTime());
//        getUserName(bean.getUserId());
        List<String> resources = bean.getResources();
        for (String resource : resources) {

            String prefix=resource.substring(resource.lastIndexOf(".")+1);
            Log.d("APILOG","后缀"+prefix);
            if(prefix.equalsIgnoreCase("png")||
                    prefix.equalsIgnoreCase("jpg")||
                    prefix.equalsIgnoreCase("jpeg")||
                    prefix.equalsIgnoreCase("gif")){
                Glide.with(view).load(resource).into(imageView);
                break;
            }


        }



        return view;
    }

    private void getUserName(int id) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userId",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call post = API.postWithToken(USER_INFO, jsonObject.toString());
        post.enqueue(new Callback() {
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
                    String respDesc = object.optString("respDesc");
                    if(respDesc.equalsIgnoreCase("ok")){
                        JSONObject object1=(JSONObject) object.get("data");

                      String ss=(object1.optString("nickName"));

                        Message message=new Message();
                        message.what=1;
                        message.obj=ss;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
