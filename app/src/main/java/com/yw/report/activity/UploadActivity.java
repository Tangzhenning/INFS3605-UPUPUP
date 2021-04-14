package com.yw.report.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yw.report.R;
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

import static com.yw.report.network.Url.FILE_UPLOAD;
import static com.yw.report.network.Url.PUBLISH_NO_TAG;
import static com.yw.report.network.Url.PUBLISH_WITH_TAG;

//discussion的发布   不带tag
public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    EditText title, comment;
    TextView  uploadFile;
    Button submit;
    String path = "";
    static Handler handler;
    List<String>servicePath=new ArrayList<>();//上传后服务器返回的文件路径
    List<String> uploadFiles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String s = (String) msg.obj;
                        Toast.makeText(UploadActivity.this, s, Toast.LENGTH_SHORT).show();
                        break;
                    case 2://上传成功  所有值恢复初始化
                        title.setText("");
                        comment.setText("");
                        uploadFile.setText("Add File or just type here(text,picture,video)\n");
                        servicePath = null;
                        uploadFiles = null;
                        servicePath = new ArrayList<>();
                        uploadFiles = new ArrayList<>();
                        path = "";
                        Toast.makeText(UploadActivity.this,"publish success!",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };
    }

    private void initView() {
        title = findViewById(R.id.title);
        comment = findViewById(R.id.comment);
        submit = findViewById(R.id.submit);
        uploadFile = findViewById(R.id.upload_file);
        submit.setOnClickListener(this);
        uploadFile.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.upload_file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(intent, 1);
                break;
            case R.id.submit:
                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please input the title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                publish();
                break;
        }
    }
    private void publish() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("title",title.getText().toString());
            jsonObject.put("content",comment.getText().toString());
            JSONArray array=new JSONArray();
            for (String s : servicePath) {
                array.put(s);
            }
            jsonObject.put("resources",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call uploadfile = API.postWithToken(PUBLISH_NO_TAG, jsonObject.toString());
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
                        //上传成功
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();

                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                Toast.makeText(this, path + "222222", Toast.LENGTH_SHORT).show();
            }
            //这里进行上传逻辑
            upload(path);
            uploadFiles.add(path);
            uploadFile.append(path+"\n");
        }
    }
    private void upload(String path) {
        Call uploadfile = API.uploadfile(FILE_UPLOAD, path);
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
                    String respDesc = object.optString("respDesc");
                    if(respDesc.equalsIgnoreCase("ok")){
                        String data = object.optString("data");
                        servicePath.add(data);
                        Message message=new Message();
                        message.what=1;
                        message.obj="file upload success!";
                        handler.sendMessage(message);
                        Log.d("APILOG","文件上传成功，添加到列表了");

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
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }
    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}