package com.yw.report.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yw.report.R;
import com.yw.report.adapter.CommentAdapter;

import com.yw.report.adapter.ShowImageAdapter;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.PRAISE_PUBLISH;
import static com.yw.report.network.Url.PUBLISH_COMMENT;
import static com.yw.report.network.Url.QUARY_PUBLISH;
import static com.yw.report.network.Url.USER_INFO;

public class DetailActivity extends AppCompatActivity {
    ListView listView;
    ListView listView1;
    static Handler handler;
    TextView name;
    TextView commentNum;
    TextView praiseNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        listView1=findViewById(R.id.image);
        listView = findViewById(R.id.listview);
        showTopic();
        initListView1();
        initListView(Data.now.getComments());

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1://获取失败toast
                        String s=(String)msg.obj;
                        name         .setText(s);
                        break;
                    case 2://重新加载评论列表
                        UploadNoTag.ListBean topic=(UploadNoTag.ListBean)msg.obj;
                        initListView(topic.getComments());

                        break;
                    case 3://点赞成功
                        Integer p=Integer.valueOf(praiseNum.getText().toString());
                        p++;
                        praiseNum.setText(p+"");
                        break;
                    case 4://评论成功
                        getPublish(Data.now.getUploadId());//重新请求该帖子  显示最新的评论数据
                        Integer c=Integer.valueOf(commentNum.getText().toString());
                        c++;

                        commentNum.setText(c+"");
//                        initListView(Data.now.getComments());

                        break;

                }
            }
        };
        getUserName(Data.now.getUserId());
    }
    private void initListView1() {
        ShowImageAdapter adapter=new ShowImageAdapter(this,R.layout.item_show_image,Data.now.getResources());
        listView1.setAdapter(adapter);
    }
    private void getPublish(int uploadId) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uploadId",uploadId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        message.obj=o.getList().get(0);
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

    private void showTopic() {

    name = findViewById(R.id.name);
        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        commentNum = findViewById(R.id.comment);
         praiseNum = findViewById(R.id.praise);
        TextView date = findViewById(R.id.date);


        title.setText(Data.now.getTitle());
        content.setText(Data.now.getContent());
        commentNum.setText(Data.now.getCommentNum() + "");
        praiseNum.setText(Data.now.getLikeNum() + "");
        date.setText(Data.now.getCreateTime());
    }
//评论列表
    private void initListView(List<UploadNoTag.ListBean.CommentsBean> commentsBean) {
        Log.d("APILOG","评论数据长度"+commentsBean.size());

        CommentAdapter adapter = new CommentAdapter(this, R.layout.item_comment, commentsBean);
        listView.setAdapter(adapter);
    }

    //弹出输入框 添加评论
    public void addComment(View view) {
        showAddCommentDialog(Data.now.getUploadId());
    }

    private void showAddCommentDialog(int id) {
        EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("public comment" +
                "").setView(et)
                .setNegativeButton("cancel", null);
        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(et.getText().toString().isEmpty()){
                    return;
                }
                        submitComment(id,et.getText().toString());

            }
        });
        builder.show();
    }
//发表评论
    private void submitComment(int id,String s) {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uploadId",id);
            jsonObject.put("comment",s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call post = API.postWithToken(PUBLISH_COMMENT, jsonObject.toString());
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
                        Message message=new Message();
                        message.what=4;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

                        android.os.Message message=new android.os.Message();
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
//点赞
    public void praise(View view) {


        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uploadId",Data.now.getUploadId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call post = API.postWithToken(PRAISE_PUBLISH, jsonObject.toString());
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
                        Message message=new Message();
                        message.what=3;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}