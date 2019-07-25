package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.myproject1.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import model.AnnounceInfo;
import model.TeacherInfo;

public class TeacherPunishAnnounce extends Activity {
    private final static String TAG = "AnnouncePunish";
    private TeacherInfo teacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_annouce);

        teacher = new TeacherInfo();
        teacher = (TeacherInfo) getIntent().getSerializableExtra("teacher");
        Log.i(TAG,teacher.toString());

        Button button = (Button) findViewById(R.id.bt_announce_commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //公告编辑
                String announce_name = ((EditText) findViewById(R.id.et_announce_name)).getText().toString().trim();
                String announce_intro1 = ((EditText) findViewById(R.id.et_announce_introduce1)).getText().toString().trim();
                String announce_intro2 = ((EditText) findViewById(R.id.et_announce_introduce2)).getText().toString().trim();
                if (announce_name.equals("")){
                    showToast("公告名不能为空!");
                    return;
                }
                if (announce_intro1.equals("")){
                    showToast("内容简述不能为空!");
                    return;
                }
                AnnounceInfo announceInfo = new AnnounceInfo();
                TeacherInfo teacherInfo = new TeacherInfo();
                teacherInfo.setObjectId(teacher.getObjectId());
                announceInfo.setTeacher(teacherInfo);
                announceInfo.setAnnounceName(announce_name);
                announceInfo.setAnnounceIntroduce(announce_intro1);
                announceInfo.setAnnounceIntroduce2(announce_intro2);
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                String filename = format.format(date);
                announceInfo.setAnnounceId(filename);
                announceInfo.setState("publishing");
                //保存
                announceInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            showToast("公告发布成功！");
                        }else {
                            showToast("公告发布失败！请重试");
                            Log.e("Announce",e.toString());
                        }
                    }
                });
                setResult(11);
            }
        });
    }

    public void showToast(String msg) {

        if (msg == null || msg.equals("")) {
            Log.e("login","无toast信息");
        } else {
            Toast toast = Toast.makeText(TeacherPunishAnnounce.this, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
