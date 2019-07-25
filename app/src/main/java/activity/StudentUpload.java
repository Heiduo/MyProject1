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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import model.FileGroup;
import model.StudentInfo;

public class StudentUpload extends Activity implements View.OnClickListener {
    private final static String TAG = "UPLOAD";
    ImageView img_upload;
    TextView tv_upload_times, tv_update_time, tv_file_opinion, tv_file_determine,tv_paper_type;

    private StudentInfo student;
    private FileGroup fileGroup;
    private String [] file_opinion;
    private String [] paper_type_name;
    SharedPreferences sharedPreferences;
    private String user_ObjectId;
    private int paper_type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);
        user_ObjectId = sharedPreferences.getString("userObjectId",null);

        paper_type = (int) getIntent().getSerializableExtra("paperType"); //获取要装填的信息

        //Toast.makeText(this," "+paper_type,Toast.LENGTH_SHORT).show();

        initView();
        init();
    }

    private void init() {
        BmobQuery<FileGroup> fileGroupQuery = new BmobQuery<>();
        fileGroupQuery.addWhereEqualTo("student",student);
        fileGroupQuery.addWhereEqualTo("paperType",paper_type);
        fileGroupQuery.order("-createdAt");
        fileGroupQuery.findObjects(new FindListener<FileGroup>() {
            @Override
            public void done(List<FileGroup> list, BmobException e) {

                if (e == null){
                    Log.i(TAG,"文件信息查询成功");
                    tv_upload_times = findViewById(R.id.tv_upload_times);
                    tv_update_time = findViewById(R.id.tv_update_time);
                    tv_file_opinion = findViewById(R.id.tv_file_opinion);
                    tv_paper_type = findViewById(R.id.tv_paper_type);
                    tv_paper_type.setVisibility(View.VISIBLE);

                    if (paper_type == 0){
                        tv_paper_type.setText(paper_type_name[paper_type]);
                    }else if(paper_type == 1){
                        tv_paper_type.setText(paper_type_name[paper_type]);
                    }else if(paper_type == 2){
                        tv_paper_type.setText(paper_type_name[paper_type]);
                    }

                    tv_upload_times.setText(" "+list.size());

                    if (list.size()>0){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        try {
                            Date mDate = dateFormat.parse(list.get(0).getCreatedAt());
                            String date = dateFormat.format(mDate);
                            tv_update_time.setText(date);
                        } catch (ParseException ex) {
                            tv_update_time.setText("暂未上传");
                            ex.printStackTrace();
                        }

                        if (list.get(0).getFileOpinion() == 0){
                            tv_file_opinion.setTextColor(Color.BLACK);
                            tv_file_opinion.setText(file_opinion[0]);
                        }else if(list.get(0).getFileOpinion() == 1){
                            tv_file_opinion.setTextColor(Color.BLUE);
                            tv_file_opinion.setText(file_opinion[1]);
                        }else if(list.get(0).getFileOpinion() == 2){
                            tv_file_opinion.setTextColor(Color.RED);
                            tv_file_opinion.setText(file_opinion[2]);
                        } else if(list.get(0).getFileOpinion() == 3){
                            tv_file_opinion.setTextColor(Color.RED);
                            tv_file_opinion.setText(file_opinion[3]);
                        }
                    }else {
                        tv_update_time.setText("暂未上传");
                        tv_file_opinion.setTextColor(Color.RED);
                        tv_file_opinion.setText("暂未上传");
                    }

                }else {
                    Log.e(TAG,"文件信息查询失败");
                }
            }
        });
    }

    private void initView() {
        student = new StudentInfo();
        student.setObjectId(user_ObjectId);

        Resources res = getResources();
        file_opinion = res.getStringArray(R.array.file_opinion);
        paper_type_name = res.getStringArray(R.array.paper_type);

        img_upload = findViewById(R.id.img_upload);
        tv_file_determine = findViewById(R.id.tv_file_determine);

        img_upload.setOnClickListener(this);
        tv_file_determine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_upload:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
                break;
            case R.id.tv_file_determine:
                finish();
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

                            fileGroup = new FileGroup();
                            fileGroup.setFileName(upLoadFileName);
                            fileGroup.setStudent(student);
                            fileGroup.setPaperType(paper_type);
                            fileGroup.setFileOpinion(3);
                            Toast.makeText(this,file.toString(),
                                    Toast.LENGTH_SHORT).show();
                            final BmobFile bmobFile = new BmobFile(new File(upLoadFilePath));
                            //bmobFile.setName(upLoadFileName);
                            //bmobFile.setOpinion("tongguo");
                            bmobFile.uploadblock(new UploadFileListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                        Log.i("heiduo","上传文件成功:" +bmobFile.getFileUrl());
                                        fileGroup.setFilePath(bmobFile.getFileUrl());

                                        fileGroup.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if(e==null) {
                                                    Log.i(TAG, "保存文件成功:");
                                                    init();
                                                }
                                            }
                                        });
                                    }else{
                                        Log.e(TAG,"上传文件失败：" + e.getMessage());
                                    }
                                }
                            });

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
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
