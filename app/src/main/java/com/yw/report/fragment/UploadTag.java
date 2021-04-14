package com.yw.report.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.yw.report.R;
import com.yw.report.activity.Data;
import com.yw.report.activity.UploadActivity;
import com.yw.report.bean.User;
import com.yw.report.network.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;
import static com.yw.report.network.Url.FILE_UPLOAD;
import static com.yw.report.network.Url.PUBLISH_WITH_TAG;

public class UploadTag extends Fragment implements View.OnClickListener {
    EditText title, comment;
    TextView t1, t2, t3, t4, t5, t6, t7, t8, uploadFile;
    Button submit;
    String tag = "null";
    String path = "";
    static Handler handler;
    List<String> servicePath = new ArrayList<>();//上传后服务器返回的文件路径
    List<String> uploadFiles = new ArrayList<>();
    String location="null";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_tag, null);
        initView(v);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String s = (String) msg.obj;
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        break;
                    case 2://上传成功  所有值恢复初始化
                        title.setText("");
                        comment.setText("");
                        uploadFile.setText("Add File or just type here(text,picture,video)\n");
                        servicePath = null;
                        uploadFiles = null;
                        servicePath = new ArrayList<>();
                        uploadFiles = new ArrayList<>();
                        tag = "null";
                        path = "";
                        setInit();
                        Toast.makeText(getActivity(), "publish success!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        return v;
    }

//    private void getLocation() {
//
//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        List<Address> addresses = geocoder.getFromLocation(locationBean.getLat(), locationBean.getLon(), 1);
//        if(addresses!=null||addresses.size()>0) {
//            String locality = addresses.get(0).getLocality() + addresses.get(0).getFeatureName();
//            Toast.makeText(getActivity(),"位置"+locality,Toast.LENGTH_SHORT).show();
//
//        }
//    }


    //入口是getLocation

    /**
     * 定位：权限判断
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        //检查定位权限
        ArrayList<String> permissions = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        //判断
        if (permissions.size() == 0) {//有权限，直接获取定位
            getLocationLL();
        } else {//没有权限，获取定位权限
            requestPermissions(permissions.toArray(new String[permissions.size()]), 2);

            Log.d("*************", "没有定位权限");
        }
    }

    /**
     * 定位：获取经纬度
     */
    private void getLocationLL() {
        Log.d("*************", "获取定位权限1 - 开始");
        Location location = getLastKnownLocation();
        if (location != null) {
            //传递经纬度给网页
           this.location = "longitude:" + location.getLongitude() + ",latitude: " + location.getLatitude() ;

//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

        } else {
//            Toast.makeText(getActivity(), "location get fail!", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 定位：得到位置对象
     * @return
     */
    private Location getLastKnownLocation() {
        //获取地理位置管理器
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }





    private void initView(View v) {
        title = v.findViewById(R.id.title);
        comment = v.findViewById(R.id.comment);
        submit = v.findViewById(R.id.submit);
        t1 = v.findViewById(R.id.t1);
        t2 = v.findViewById(R.id.t2);
        t3 = v.findViewById(R.id.t3);
        t4 = v.findViewById(R.id.t4);
        t5 = v.findViewById(R.id.t5);
        t6 = v.findViewById(R.id.t6);
        t7 = v.findViewById(R.id.t7);
        t8 = v.findViewById(R.id.t8);
        uploadFile = v.findViewById(R.id.upload_file);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        submit.setOnClickListener(this);
        uploadFile.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        setInit();
        switch (v.getId()) {
            case R.id.t1:

                tag = t1.getText().toString();
                this.t1.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t2:
                tag = t2.getText().toString();
                this.t2.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t3:
                tag = t3.getText().toString();
                this.t3.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t4:
                tag = t4.getText().toString();
                this.t4.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t5:
                tag = t5.getText().toString();
                this.t5.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t6:
                tag = t6.getText().toString();
                this.t6.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t7:
                tag = t7.getText().toString();
                this.t7.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.t8:
                tag = t8.getText().toString();
                this.t8.setBackgroundResource(R.drawable.shape_born_choose);
                break;
            case R.id.submit:
                if (tag.equals("null")) {
                    Toast.makeText(getActivity(), "Please choose a tag!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please input the title!", Toast.LENGTH_SHORT).show();
                    return;
                }
                publish();
                break;
            case R.id.upload_file://choose file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(intent, 1);
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void publish() {
        getLocation();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("title",title.getText().toString());
            jsonObject.put("content",comment.getText().toString());
            jsonObject.put("tag",tag);
            jsonObject.put("location",location);
            JSONArray array=new JSONArray();
            for (String s : servicePath) {
                array.put(s);
            }
            jsonObject.put("resources",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call uploadfile = API.postWithToken(PUBLISH_WITH_TAG, jsonObject.toString());
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

    public void setInit() {
        this.t1.setBackgroundResource(R.drawable.shape_born);
        this.t2.setBackgroundResource(R.drawable.shape_born);
        this.t3.setBackgroundResource(R.drawable.shape_born);
        this.t4.setBackgroundResource(R.drawable.shape_born);
        this.t5.setBackgroundResource(R.drawable.shape_born);
        this.t6.setBackgroundResource(R.drawable.shape_born);
        this.t7.setBackgroundResource(R.drawable.shape_born);
        this.t8.setBackgroundResource(R.drawable.shape_born);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();

                Toast.makeText(getActivity(), path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(getActivity(), uri);
                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                Toast.makeText(getActivity(), path + "222222", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
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
