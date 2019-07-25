package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myproject1.R;


import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import model.TeacherInfo;

public class PersonalDetailActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "PersonDetail";
    private TeacherInfo teacherInfo;
    private ImageView personal_pic_head;
    private TextView tv_teacherId, tv_teacherName, tv_teacherSex, tv_submit,tv_group;
    private EditText et_teacherTel, et_teacherEmail;
    private PopupWindow mShowDialog;
    private Bitmap photo;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_personal);
        sharedPreferences= getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        findView();
        initView();
    }
    //组建数据装填
    private void initView() {
        teacherInfo = new TeacherInfo();
        String teacherObjectId = sharedPreferences.getString("userObjectId",null);
        BmobQuery<TeacherInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.include("group");
        //先从网络获取
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.getObject(teacherObjectId, new QueryListener<TeacherInfo>() {
            @Override
            public void done(TeacherInfo teacher, BmobException e) {
                if (e == null){
                    teacherInfo = teacher;
                    Log.i(TAG,"PersonDetail");
                    tv_teacherId.setText(teacher.getTeacherId());
                    tv_teacherName.setText(teacher.getTeacherName());
                    tv_teacherSex.setText(teacher.getTeacherSex());
                    et_teacherTel.setText(teacher.getTeacherTel());
                    et_teacherEmail.setText(teacher.getTeacherEmail());
                    tv_group.setText(teacher.getGroup().getGroupId());
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }
    //组建获取
    private void findView() {
        personal_pic_head = findViewById(R.id.personal_pic_head);
        tv_teacherId = findViewById(R.id.tv_teacherId);
        tv_teacherName = findViewById(R.id.tv_teacherName);
        tv_teacherSex = findViewById(R.id.tv_teacherSex);
        tv_submit = findViewById(R.id.tv_submit);
        et_teacherTel = findViewById(R.id.et_teacherTel);
        et_teacherEmail = findViewById(R.id.et_teacherEmail);
        tv_group = findViewById(R.id.tv_teacherGroup);
        personal_pic_head.setOnClickListener(this);
        tv_teacherSex.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_pic_head:
                showPop();
                break;
            case R.id.tv_teacherSex:
                tv_teacherName.clearFocus();
                showSEXPop();
                break;
            case R.id.tv_submit:
                submit();
                break;
            case R.id.persinal_data_pop_nan:
                tv_teacherSex.setText("男");
                mShowDialog.dismiss();
                break;
            case R.id.persinal_data_pop_nv:
                tv_teacherSex.setText("女");
                mShowDialog.dismiss();
                break;
            case R.id.persinal_data_pop_cancel:
                mShowDialog.dismiss();
                break;
                default:
                    break;
        }
    }
    /**
     * 确定按钮
     */
    private void submit() {
        final String sex = tv_teacherSex.getText() == null ? "" : tv_teacherSex.getText().toString();
        final String tel = et_teacherTel.getText() == null ? "" : et_teacherTel.getText().toString();
        final String email = et_teacherEmail.getText() == null ? "" : et_teacherEmail.getText().toString();

        teacherInfo.setTeacherSex(sex);
        teacherInfo.setTeacherTel(tel);
        teacherInfo.setTeacherEmail(email);

        teacherInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Log.i(TAG,"PersonDetail update");
                    finish();
                }else {
                    Log.e(TAG,"PersonDetail defeat "+e.getMessage());
                }
            }
        });
    }

    /**
     * 显示照片来源填出框
     */
    private void showPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.personal_data_show_pop, null);
        mShowDialog = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mShowDialog.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowDialog.dismiss();
            }
        });

        view.findViewById(R.id.persinal_data_pop_camera).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_gallery).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_cancel).setOnClickListener(this);

        mShowDialog.showAtLocation(tv_submit, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示性别的选择框
     */
    private void showSEXPop() {
        View view = LayoutInflater.from(this).inflate(R.layout.personal_data_show_pop_sex, null);
        mShowDialog = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        mShowDialog.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mShowDialog.dismiss();
            }
        });

        view.findViewById(R.id.persinal_data_pop_nan).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_nv).setOnClickListener(this);
        view.findViewById(R.id.persinal_data_pop_cancel).setOnClickListener(this);

        mShowDialog.showAtLocation(tv_submit, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 拍照
     */
    private void takePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * 相册
     */
    private void pickGallery() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createTmpFile(PersonalDetailActivity.this)));
        startActivityForResult(intent, 2);
    }

    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = new File(context.getExternalCacheDir() + File.separator + "Pictures");
            if (!pic.exists()) {
                pic.mkdirs();
            }
            File tmpFile = new File(pic, "touxiang.jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            File tmpFile = new File(cacheDir, "touxiang.jpg");
            return tmpFile;
        }

    }

}
