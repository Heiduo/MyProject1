package activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

public class CallOtherOpenFile {
    public void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("OpenMode", "ReadOnly");// 只读模式
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性   
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型   
            String type = getMIMEType(file);
            /*
            * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!啊啊啊要炸了 ,权限真头疼
            * */
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //设置intent的data和Type属性。   
            Uri uri =  FileProvider.getUriForFile(context, "com.example.myproject1.fileprovider", file);

            //Uri uri = Uri.fromFile(file);
            intent.setDataAndType(/*uri*/uri, type);
            intent.putExtras(bundle);
            //跳转   
            context.startActivity(intent);
            //      Intent.createChooser(intent, "请选择对应的软件打开该附件！");  
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception  
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。   
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。   
        for (
                int i = 0;
                i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private String[][] MIME_MapTable = {
            //{后缀名，MIME类型}   
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".pdf", "application/pdf"},
            {".wps", "application/vnd.ms-works"}
    };
}