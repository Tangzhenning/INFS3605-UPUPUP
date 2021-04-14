package com.yw.report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yw.report.activity.Data;
import com.yw.report.activity.NavActivity;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yw.report.network.Url.*;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


















        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_password);
        checkPermission();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String s=(String)msg.obj;
                        Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    public void signUp(View view) {

        Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    public void loginIn(View view) {
        if(email.getText().toString().isEmpty()){

            Toast.makeText(LoginActivity.this,"Please input the email!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Please input the password!",Toast.LENGTH_SHORT).show();

            return;
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call post = API.post(LOGIN, jsonObject.toString());
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
                        Intent intent=new Intent(LoginActivity.this,NavActivity.class);
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
    public void checkPermission(){
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);



        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED
                || permissionCheck2 != PackageManager.PERMISSION_GRANTED||
                permissionCheck3 != PackageManager.PERMISSION_GRANTED||
                permissionCheck4 != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION},
                    124);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int g : grantResults) {
            if (g != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Please grant the app access to mobile storage, otherwise the app will not work normally!", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        }
    }
}