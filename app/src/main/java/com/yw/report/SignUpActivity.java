package com.yw.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yw.report.network.Url.SIGNUP;

public class SignUpActivity extends AppCompatActivity {
    EditText email,password,name;
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        name=findViewById(R.id.et_name);
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(SignUpActivity.this,s,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public void signUp(View view) {
        if(email.getText().toString().isEmpty()){

            Toast.makeText(SignUpActivity.this,"Please input the email!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().isEmpty()){
            Toast.makeText(SignUpActivity.this,"Please input the password!",Toast.LENGTH_SHORT).show();

            return;
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("nickName",name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call post = API.post(SIGNUP, jsonObject.toString());
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
                        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
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

    public void toLogin(View view) {
        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}