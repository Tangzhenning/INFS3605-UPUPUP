package com.yw.report.util;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class FileMimeType {
    public static String getMIME(String path){

        String type = null;
//使用系统API，获取URL路径中文件的后缀名（扩展名）
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (!TextUtils.isEmpty(extension)) {
            //使用系统API，获取MimeTypeMap的单例实例，然后调用其内部方法获取文件后缀名（扩展名）所对应的MIME类型
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        Log.d("MIME", "类型为：" + type);
        return type;
    }
}
