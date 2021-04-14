package com.yw.report.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yw.report.R;
import com.yw.report.bean.UploadNoTag;
import com.yw.report.bean.UploadTag;

import java.util.List;

//无TAG的
public class ShowUploadAdapterTag extends ArrayAdapter<UploadTag.ListBean> {
    private final int resourceId;
    public ShowUploadAdapterTag(@NonNull Context context, int resource, @NonNull List<UploadTag.ListBean> u) {
        super(context, resource, u);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UploadTag.ListBean bean=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView tag                      =                      view.findViewById(R.id.tag);

        tag.setText(bean.getTag());






        return view;
    }
}
