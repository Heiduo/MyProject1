package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myproject1.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import model.StudentInfo;
//import per.goweii.actionbarex.ActionBarEx;

/**
 *
 */
public class PersonalMainActivity extends Activity implements
        View.OnClickListener {
    private final static String TAG = "PERSON";
    //private ActionBarEx actionBarEx;
    private TextView tv_teacher_name;
    private RelativeLayout rl_my_theme;
    private RelativeLayout rl_my_students;
    private RelativeLayout rl_my_others;
    private RelativeLayout rl_setting;
    SharedPreferences sharedPreferences;
    private Boolean user_type;
    private String user_ObjectId;
    private StudentInfo student;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type", true);

        findView();
        initView();
    }

    private void initView() {
        if (BmobUser.isLogin()) {
            String userName = sharedPreferences.getString("userName", "未登录");
            tv_teacher_name.setText(userName);
        }
    }

    private void findView() {
        tv_teacher_name = findViewById(R.id.tv_teacher_name);
        RelativeLayout rl_myDetail = findViewById(R.id.rl_myDetail);
        rl_my_theme = findViewById(R.id.rl_my_theme);
        rl_my_students = findViewById(R.id.rl_my_students);
        rl_my_others = findViewById(R.id.rl_my_others);
        rl_setting = findViewById(R.id.rl_setting);
        //actionBarEx = findViewById(R.id.action_bar_ex);
        //TextView title= actionBarEx.getView(R.id.common_title_title_text);
        //title.setText("个人中心");

        // actionBarEx.getView(R.id.common_title_left_img).setOnClickListener(this);
        rl_myDetail.setOnClickListener(this);
        rl_my_theme.setOnClickListener(this);
        rl_my_students.setOnClickListener(this);
        rl_my_others.setOnClickListener(this);
        rl_setting.setOnClickListener(this);

        if (!user_type) {//学生
            rl_my_students.setVisibility(View.GONE);

            user_ObjectId = sharedPreferences.getString("userObjectId", null);
            BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
            bmobQuery.include("project");
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            bmobQuery.getObject(user_ObjectId, new QueryListener<StudentInfo>() {
                @Override
                public void done(StudentInfo studentInfo, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, "success:" + studentInfo.toString());
                        student = studentInfo;
                    } else {
                        Log.e(TAG, "fail:" + e.toString());
                    }
                }
            });
        }else {
            rl_my_students.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentPic();
    }

    /**
     * 获取头像
     */
    private void getStudentPic() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.personal_main_pic:                  //头像-->信息界面
                intent = new Intent(this, PersonalDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_myDetail:                        //我的信息-->信息界面
                if (user_type) {
                    intent = new Intent(this, PersonalDetailActivity.class);
                } else {
                    intent = new Intent(this, MyStudentsDetailActivity.class);
                    intent.putExtra("StudentInfo", student);
                }
                startActivity(intent);
                break;
            case R.id.rl_my_theme:                        //我的课题-->课题界面
                if (user_type){
                    intent = new Intent(this, MyProjectsActivity.class);

                }else {
                    intent = new Intent(this,ProjectDetail.class);
                }
                startActivity(intent);
                break;
            case R.id.rl_my_students:                     //我的学生-->学生界面
                intent = new Intent(this, MyStudentsActivity.class);
                intent.putExtra("Layout", 1);  //我的学生界面
                startActivity(intent);
                break;
            case R.id.rl_my_others:                       //审核与答辩
                intent = new Intent(this, ThemeManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting:                         //设置-->设置界面
                intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, 99);
                break;
            case R.id.common_title_left_img:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 99 && resultCode == 404) {// 退出登录 直接回到首页
            setResult(403);
            finish();
        }
    }
}
