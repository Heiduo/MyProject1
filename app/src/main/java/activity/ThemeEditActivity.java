package activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import model.ProjectInfo;
import model.TeacherInfo;

/**
 * 毕设题目的提交
 */
public class ThemeEditActivity extends Activity implements OnClickListener {
    public Button bt_commit;
    public EditText et_name, et_require;
    public RelativeLayout rl_type;
    public TextView tv_type;
    public String teacherName, teacherObjectId, detail;
    private ProjectInfo project;

    private PopupWindow typeWindow;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_edit);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        findView();
        initView();
    }

    private void findView() {
        et_name = findViewById(R.id.et_name);
        rl_type = findViewById(R.id.rl_project_type);
        tv_type = findViewById(R.id.tv_project_type);
        et_require =findViewById(R.id.et_require);
        bt_commit = findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(this);
        rl_type.setOnClickListener(this);
    }

    private void initView() {
        teacherName = sharedPreferences.getString("userName",null);
        teacherObjectId = sharedPreferences.getString("userObjectId",null);
        project = new ProjectInfo();
    }

    @Override
    public void onClick(View v) {
        /**
         * 完成按钮点击事件 添加或更新
         */
        switch (v.getId()){
            case R.id.bt_commit:
                checkCommit();
                break;

            case R.id.project_type_pop_1:
                //tv_type.setTextColor(R.color.Black);
                tv_type.setText("应用工程");
                typeWindow.dismiss();
                break;

            case R.id.project_type_pop_2:
                tv_type.setText("理论研究");
                typeWindow.dismiss();
                break;

            case R.id.project_type_pop_cancel:
                typeWindow.dismiss();
                break;

            case R.id.rl_project_type:
                showTypeWindow();
                break;
                default:
                    break;
        }
    }

    private void checkCommit() {
        if (TextUtils.isEmpty(et_name.getText())){
            Toast.makeText(getApplicationContext(),"请输入毕设题目",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tv_type.getText())){
            Toast.makeText(getApplicationContext(),"请选择毕设类型",Toast.LENGTH_SHORT).show();
            return;
        }

        final String projectName = et_name.getText() == null ? "":et_name.getText().toString();
        final String projectType = tv_type.getText() == null ? "":tv_type.getText().toString();

        project.setProjectName(projectName);
        project.setProjectType(projectType);
        project.setProjectRequire(et_require.getText().toString().isEmpty()
                ? "" : et_require.getText().toString());
        project.setProjectTeacher(teacherName);
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setObjectId(teacherObjectId);
        project.setTeacher(teacherInfo);
        //project.setTeacher(BmobUser.getCurrentUser(TeacherInfo.class));
        project.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e ==null){
                    Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"提交失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showTypeWindow(){
        View view = LayoutInflater.from(this).inflate(R.layout.project_type_show_pop,null);
        typeWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        typeWindow.setOutsideTouchable(true);

        typeWindow.showAtLocation(bt_commit, Gravity.BOTTOM,0,0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeWindow.dismiss();
            }
        });

        view.findViewById(R.id.project_type_pop_1).setOnClickListener(this);
        view.findViewById(R.id.project_type_pop_2).setOnClickListener(this);
        view.findViewById(R.id.project_type_pop_cancel).setOnClickListener(this);
    }
}
