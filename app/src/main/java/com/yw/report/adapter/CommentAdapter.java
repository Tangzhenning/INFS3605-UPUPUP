package com.yw.report.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yw.report.R;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.UploadTag;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.USER_INFO;

//无TAG的
public class CommentAdapter extends ArrayAdapter<UploadNoTag.ListBean.CommentsBean> {
    private final int resourceId;
     Handler handler;
    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<UploadNoTag.ListBean.CommentsBean> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UploadNoTag.ListBean.CommentsBean bean=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView username                     =                      view.findViewById(R.id.username);
        TextView content                     =                      view.findViewById(R.id.content);
        TextView date                     =                      view.findViewById(R.id.date);
        username.setText(bean.getNickName()+":");
//        handler=new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1:
//                        String s=(String)msg.obj;
//                        username.setText(s+":");
//                        break;
//
//                }
//            }
//        };
//        getUserName(bean.getUserId());

       content.setText(bean.getComment());
       date.setText(bean.getCreateTime());






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
                        android.os.Message message=new Message();
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
