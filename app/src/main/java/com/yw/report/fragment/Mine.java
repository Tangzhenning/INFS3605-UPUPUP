package com.yw.report.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.report.LoginActivity;
import com.yw.report.R;
import com.yw.report.SignUpActivity;
import com.yw.report.activity.Data;
import com.yw.report.activity.HistoryActivity;
import com.yw.report.activity.SettingActivity;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.yw.report.network.Url.SIGNUP;
import static com.yw.report.network.Url.UPDATE_USER_INFO;

public class Mine extends Fragment implements View.OnClickListener {
    ImageView edit,settings;
    Button submit,logout;
    EditText name,id,location,email,history;
    Boolean isEdit=false;
    static Handler handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_mine,null);
        initView(v);
        initText();
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

                        initText();
                        break;
                }
            }
        };
        return v;
    }

    private void initText() {
        Log.d("APILOG","loginuser"+Data.loginUser.toString());
        name.setText(Data.loginUser.getName());
        id.setText(Data.loginUser.getId()+"");
        location.setText(Data.loginUser.getLocation());
        email.setText(Data.loginUser.getEmail());
    }

    private void initView(View v) {
        edit=v.findViewById(R.id.edit);
        submit=v.findViewById(R.id.submit);
        name=v.findViewById(R.id.name);
        id=v.findViewById(R.id.id);
        location=v.findViewById(R.id.location);
        email=v.findViewById(R.id.email);
        settings=v.findViewById(R.id.settings);
        history=v.findViewById(R.id.history);
        logout=v.findViewById(R.id.log_out);
        edit.setOnClickListener(this);
        submit.setOnClickListener(this);
        settings.setOnClickListener(this);
        history.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit:
                initText();
                if(isEdit){//进入不编辑状态
                    edit.setImageResource(R.drawable.edit);
                    submit.setVisibility(View.INVISIBLE);
                    name.setEnabled(false);
                    location.setEnabled(false);
                    email.setEnabled(false);
                }else {//进入编辑状态
                    edit.setImageResource(R.drawable.edit_yes);
                    submit.setVisibility(View.VISIBLE);
                    name.setEnabled(true);
                    location.setEnabled(true);
                    email.setEnabled(true);

                }
                isEdit=!isEdit;

                return;
            case R.id.submit:

                edit.setImageResource(R.drawable.edit);
                submit.setVisibility(View.INVISIBLE);
                name.setEnabled(false);
                location.setEnabled(false);
                email.setEnabled(false);
                isEdit=false;

                update();
                initText();
                break;
            case R.id.settings:
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.history:
                 intent=new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    private void update() {
        JSONObject jsonObject=new JSONObject();
        try {

                Log.d("APILOG","修改后的email"+email.getText().toString());
            Log.d("APILOG","修改后的name"+name.getText().toString());
            Log.d("APILOG","修改后的location"+location.getText().toString());
            jsonObject.put("email",email.getText().toString());
            jsonObject.put("nickName",name.getText().toString());
            jsonObject.put("location",location.getText().toString());
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
                        Message message=new Message();
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
