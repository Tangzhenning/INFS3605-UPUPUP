package com.yw.report.adapter;

import android.content.Context;
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
import com.yw.report.R;
import com.yw.report.bean.Message;

import java.util.List;

//无TAG的
public class ShowImageAdapter extends ArrayAdapter<String> {
    private final int resourceId;
    public ShowImageAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        Log.d("APILOG","图片数量"+objects.size());
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String bean=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView imageView=view.findViewById(R.id.image);
        Glide.with(view).load(bean).into(imageView);






        return view;
    }
}
