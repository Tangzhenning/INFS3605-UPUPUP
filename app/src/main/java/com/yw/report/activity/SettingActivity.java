package com.yw.report.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yw.report.R;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.UPDATE_USER_INFO;

public class SettingActivity extends AppCompatActivity {
    Boolean isEdit=false;
    ImageView edit;
    EditText password;
    Button submit;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        edit=findViewById(R.id.edit);
        password=findViewById(R.id.password);
        submit=findViewById(R.id.submit);
        initText();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(SettingActivity.this,s,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //重新设置文字
             initText();
                        break;
                }
            }
        };
    }

    public void edit(View view) {
        initText();
        if(isEdit){//进入不编辑状态
            edit.setImageResource(R.drawable.edit);
            password.setEnabled(false);
            submit.setVisibility(View.INVISIBLE);

        }else {//进入编辑状态
            edit.setImageResource(R.drawable.edit_yes);
            submit.setVisibility(View.VISIBLE);
            password.setEnabled(true);


        }
        isEdit=!isEdit;
    }

    public void submit(View view) {
        edit.setImageResource(R.drawable.edit);
        submit.setVisibility(View.INVISIBLE);
        password.setEnabled(false);
        isEdit=false;

        update();
        initText();
    }

    private void initText() {
        Log.d("APILOG","loginuser"+Data.loginUser.toString());
        password.setText(Data.loginUser.getPassword());
    }
    private void update() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("password",password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call post = API.postWithToken(UPDATE_USER_INFO, jsonObject.toString());
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
                        User user=new User();
                        user.setId(object1.optInt("userId"));
                        user.setEmail(object1.optString("email"));
                        user.setName(object1.optString("nickName"));
                        user.setLocation(object1.optString("location"));
                        user.setToken(object1.optString("token"));
                        user.setPassword(object1.optString("password"));
                        Log.d("APILOG","user--"+user.toString());
                        Data.loginUser=user;
                        Message message=new Message();//更新文字显示
                        message.what=2;
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
}