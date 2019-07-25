package activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import model.FileGroup;
import model.StudentInfo;

public class TeacherDowload extends Activity implements View.OnClickListener {
    private final static String TAG = "DOWNLOAD";
    ImageView img_download;
    TextView tv_upload_times_download, tv_update_time_download,
            tv_file_opinion_download, tv_file_determine_download,
            tv_paper_type_download,tv_file_name_download,
            tv_file_already_download,tv_file_path_download;

    private StudentInfo student;
    private FileGroup fileGroup;
    private File saveFile;
    private String [] file_opinion;
    private String [] paper_type_name;
    private String [] paper_type_path;
    private ArrayList<FileGroup> list_students_file_one = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int paper_type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_download);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);


        paper_type = (int) getIntent().getSerializableExtra("paperType"); //获取要装填的信息
        list_students_file_one.addAll((Collection<? extends FileGroup>)
                getIntent().getSerializableExtra("StudentFileInfo"));

        //根据创建时间排序
        Collections.sort(list_students_file_one, new Comparator<FileGroup>() {
            @Override
            public int compare(FileGroup fileGroup, FileGroup t1) {
                return t1.getCreatedAt().compareTo(fileGroup.getCreatedAt());
            }
        });

        //Toast.makeText(this," "+paper_type,Toast.LENGTH_SHORT).show();

        initView();
        init();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_download:
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);*/

                if (list_students_file_one.size()>0) {

                    if (fileGroup.getCreatedAt().equals(sharedPreferences.getString( //文档上传时间一样
                            fileGroup.getStudent().getStudentId(),null))){
                        Toast.makeText(this,"已是最新文件,无需下载!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i("heiduo","下载文件: ");
                    BmobFile bmobFile = new BmobFile(fileGroup.getFileName(),"",fileGroup.getFilePath());
                    //下载地址
                    saveFile = new File(Environment.getExternalStorageDirectory()
                            +"/BSDwonload/"+fileGroup.getStudent().getStudentId()+"/"
                            , fileGroup.getFileName());
                    bmobFile.download(saveFile, new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                editor = sharedPreferences.edit();
                                Toast.makeText(TeacherDowload.this,
                                        "文件下载成功!",Toast.LENGTH_SHORT).show();
                                Log.i(TAG,"下载文件成功:" +s);
                                tv_file_name_download.setText(fileGroup.getFileName());
                                tv_file_name_download.setFocusable(true);
                                tv_file_name_download.setTextColor(Color.BLUE);
                                tv_file_name_download.setClickable(true);
                                //时间
                                SimpleDateFormat dateFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    Date mDate = dateFormat.parse(list_students_file_one.
                                            get(0).getCreatedAt());
                                    String date = dateFormat.format(mDate);
                                    tv_file_already_download.setText(date);
                                    editor.putString(fileGroup.getStudent().getStudentId()
                                            ,date);
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }

                                tv_file_path_download.setText(s);
                                editor.putString(fileGroup.getStudent().getObjectId()
                                        ,fileGroup.getFileName());
                                editor.commit();
                            }else {
                                Log.i(TAG,"下载文件失败:"+e.toString() );
                                Toast.makeText(TeacherDowload.this,"文件下载失败,请打开存储权限!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {
                            Log.i("bmob","下载进度："+integer+","+l);
                        }
                    });
                }else {
                    Toast.makeText(this,"无可下载文件!",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_file_determine:
                finish();
                break;

            case R.id.tv_file_name_download:    //打开文件
                String fileName = sharedPreferences.getString(fileGroup.getStudent().getObjectId(),"");
                File testFile = new File(Environment.getExternalStorageDirectory(), fileName);
                Log.e(TAG,"test-tv_xian "+testFile.getAbsolutePath());
                CallOtherOpenFile openFile = new CallOtherOpenFile();
                openFile.openFile(TeacherDowload.this,testFile);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                if (uri != null) {
                    String path = getPath(this, uri);
                    if (path != null) {
                        File file = new File(path);
                        if (file.exists()) {
                            String upLoadFilePath = file.toString();
                            String upLoadFileName = file.getName();

                            Toast.makeText(this,file.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        }
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
//                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
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
//            Log.i(TAG,"content***"+uri.toString());
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            Log.i(TAG,"file***"+uri.toString());
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


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.example.myproject1".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.example.myproject1".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.example.myproject1".equals(uri.getAuthority());
    }

    private void init() {
        if (paper_type == 0){
            tv_paper_type_download.setText(paper_type_name[paper_type]);
        }else if(paper_type == 1){
            tv_paper_type_download.setText(paper_type_name[paper_type]);
        }else if(paper_type == 2){
            tv_paper_type_download.setText(paper_type_name[paper_type]);
        }
        tv_upload_times_download.setText(" "+list_students_file_one.size());

        if (list_students_file_one.size()>0){
            fileGroup = list_students_file_one.get(0);
            //文件地址
            File file = new File(Environment.getExternalStorageDirectory()
                    +"/BSDwonload/"+fileGroup.getStudent().getStudentId()+"/",
                    sharedPreferences.getString(fileGroup.getStudent().getObjectId(),TAG));

            if (file.exists()){
                tv_file_name_download.setText(fileGroup.getFileName());
                tv_file_name_download.setFocusable(true);
                tv_file_name_download.setTextColor(Color.BLUE);
                tv_file_name_download.setClickable(true);
                //时间
                tv_file_already_download.setText(sharedPreferences.getString(
                        fileGroup.getStudent().getStudentId(),""));
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date mDate = dateFormat.parse(list_students_file_one.get(0).getCreatedAt());
                String date = dateFormat.format(mDate);
                tv_update_time_download.setText(date);
            } catch (ParseException ex) {
                tv_update_time_download.setText("暂未上传");
                ex.printStackTrace();
            }

            if (list_students_file_one.get(0).getFileOpinion() == 0){
                tv_file_opinion_download.setTextColor(Color.BLACK);
                tv_file_opinion_download.setText(file_opinion[0]);
            }else if(list_students_file_one.get(0).getFileOpinion() == 1){
                tv_file_opinion_download.setTextColor(Color.BLUE);
                tv_file_opinion_download.setText(file_opinion[1]);
            }else if(list_students_file_one.get(0).getFileOpinion() == 2){
                tv_file_opinion_download.setTextColor(Color.RED);
                tv_file_opinion_download.setText(file_opinion[2]);
            } else if(list_students_file_one.get(0).getFileOpinion() == 3){
                tv_file_opinion_download.setTextColor(Color.RED);
                tv_file_opinion_download.setText(file_opinion[3]);
            }
        }else {
            tv_update_time_download.setText("暂未上传");
            tv_file_opinion_download.setTextColor(Color.RED);
            tv_file_opinion_download.setText("暂未上传");
        }
    }

    private void initView() {
        student = new StudentInfo();

        Resources res = getResources();
        file_opinion = res.getStringArray(R.array.file_opinion);
        paper_type_name = res.getStringArray(R.array.paper_type);
        paper_type_path = res.getStringArray(R.array.paper_type_path);

        tv_upload_times_download = findViewById(R.id.tv_upload_times_download);
        tv_update_time_download = findViewById(R.id.tv_update_time_download);
        tv_file_opinion_download = findViewById(R.id.tv_file_opinion_download);
        tv_file_path_download = findViewById(R.id.tv_file_path_download);
        tv_paper_type_download = findViewById(R.id.tv_paper_type_download);
        tv_file_already_download = findViewById(R.id.tv_file_already_download);
        tv_file_name_download = findViewById(R.id.tv_file_name_download);
        tv_paper_type_download.setVisibility(View.VISIBLE);

        tv_file_determine_download = findViewById(R.id.tv_file_determine_download);
        img_download = findViewById(R.id.img_download);

        img_download.setOnClickListener(this);
        tv_file_name_download.setOnClickListener(this);
        tv_file_determine_download.setOnClickListener(this);
    }
}
