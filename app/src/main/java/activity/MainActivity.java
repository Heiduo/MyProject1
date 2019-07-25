package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myproject1.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import model.CreditCoursrInfo;
import model.StudentInfo;
import model.TeacherInfo;

public class MainActivity extends Activity {
    public final static String ApplicationID = "";//这里自己根据Bmob的AppID设置
    public static final String ProjectPreference = "activity";
    private final static String TAG = "MAIN";

    private LinearLayout main_my_type;
    private LinearLayout main_management;
    private LinearLayout main_announce;
    private LinearLayout main_myself;
    private LinearLayout main_defence_grade;
    private TextView tv_main_type;
    private Boolean user_type;
    private TeacherInfo teacher;
    private StudentInfo student;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, ApplicationID);
        sharedPreferences = getSharedPreferences(ProjectPreference, MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type",false);
        String id = sharedPreferences.getString("userObjectId",null);

        if (user_type){
            teacher = new TeacherInfo();
            teacher = (TeacherInfo) getIntent().getSerializableExtra("teacher");
            if (teacher ==null){
                BmobQuery<TeacherInfo> bmobQuery = new BmobQuery<>();
                bmobQuery.include("teacherId");
                bmobQuery.getObject(id, new QueryListener<TeacherInfo>() {
                    @Override
                    public void done(TeacherInfo teacherInfo, BmobException e) {
                        if (e == null){
                            teacher = teacherInfo;
                            Log.i(TAG,"教师信息"+teacher.toString());
                        }else {
                            Log.e(TAG,"教师信息失败"+e.getMessage());
                        }
                    }
                });
            }
        }else {
            student = new StudentInfo();
            student = (StudentInfo) getIntent().getSerializableExtra("student");
            if (student == null){
                BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
                bmobQuery.include("teacher");
                bmobQuery.getObject(id, new QueryListener<StudentInfo>() {
                    @Override
                    public void done(StudentInfo studentInfo, BmobException e) {
                        if (e == null){
                            student = studentInfo;
                            Log.i(TAG,"学生信息"+student.toString());
                        }else {
                            Log.e(TAG,"学生信息失败"+e.getMessage());
                        }
                    }
                });
            }
        }
        //BmobUser.logOut();

        init();
    }

    public void init() {
        main_my_type = findViewById(R.id.main_my_type);
        main_management = findViewById(R.id.main_management);
        main_announce = findViewById(R.id.main_announce);
        main_myself = findViewById(R.id.main_myself);
        main_defence_grade = findViewById(R.id.main_defence_and_grade);
        tv_main_type = findViewById(R.id.tv_main_type);

        if (user_type) {
            tv_main_type.setText("我的学生");
        } else {
            tv_main_type.setText("我的毕设");
        }

        main_my_type.setOnClickListener(new MyClickListener());
        main_management.setOnClickListener(new MyClickListener());
        main_announce.setOnClickListener(new MyClickListener());
        main_myself.setOnClickListener(new MyClickListener());
        main_defence_grade.setOnClickListener(new MyClickListener());
    }

    private class MyClickListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_announce:  //公告 双端相同
                    intent = new Intent(MainActivity.this
                            , MyAnnounceActivity.class);
                    if (user_type){
                        intent.putExtra("teacher",teacher);
                    }else {
                        intent.putExtra("student",student);
                    }
                    startActivity(intent);
                    break;
                case R.id.main_myself:   //个人中心
                    intent = new Intent(MainActivity.this
                            , PersonalMainActivity.class);
                    startActivityForResult(intent, 99);
                    break;
                case R.id.main_my_type: //学生或毕设课题
                    if (user_type){ //teacher
                        intent = new Intent(MainActivity.this
                                , MyStudentsActivity.class);
                        intent.putExtra("Layout", 1);
                    }else {         //student
                        intent = new Intent(MainActivity.this
                                ,ProjectDetail.class);
                    }
                    startActivity(intent);
                    break;
                case R.id.main_management:  //毕设管理
                    if (user_type){      //教师
                        intent = new Intent(MainActivity.this,
                                ThemeManagementActivity.class);
                    }else {//学生
                        intent = new Intent(MainActivity.this,
                                ThemeStudentManagementActivity.class);
                    }
                    startActivity(intent);
                    break;
                case R.id.main_defence_and_grade:
                    intent = new Intent(MainActivity.this, DefenceAndGradeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 99 && resultCode == 403) {// 退出登录 直接回到首页
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }
    }

}
