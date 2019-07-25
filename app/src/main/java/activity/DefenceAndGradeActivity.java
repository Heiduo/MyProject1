package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myproject1.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import model.StudentInfo;


public class DefenceAndGradeActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "DEFENCE";
    private RelativeLayout rl_defence_type;
    private RelativeLayout rl_my_group;
    private RelativeLayout rl_grade_skim;
    private RelativeLayout rl_grade_submit;
    private ImageView grade_skim_icon;
    private TextView tv_grade;
    private TextView tv_defence_type;
    private Boolean user_type;
    private StudentInfo student;
    private String user_ObjectId;
    private String [] defence_type;

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_and_management);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type",true);

        findView();
        init();
    }

    private void init() {
        Resources res = getResources();
        defence_type = res.getStringArray(R.array.defence_type);

        user_ObjectId = sharedPreferences.getString("userObjectId", null);
        BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.include("project,group");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.getObject(user_ObjectId, new QueryListener<StudentInfo>() {
            @Override
            public void done(StudentInfo studentInfo, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "success:" + studentInfo.toString());
                    student = studentInfo;

                    if (studentInfo.getDenfeceType() == 1){
                        tv_defence_type.setTextColor(Color.BLACK);
                        tv_defence_type.setText(defence_type[0]);
                    }else if(studentInfo.getDenfeceType() == 2){
                        tv_defence_type.setTextColor(Color.RED);
                        tv_defence_type.setText(defence_type[1]);
                    }
                    tv_grade.setText(""+student.getGrade());
                } else {
                    Log.e(TAG, "fail:" + e.toString());
                }
            }
        });
    }

    private void findView() {
        rl_defence_type = findViewById(R.id.rl_defence_type);
        rl_my_group = findViewById(R.id.rl_my_group);
        rl_grade_skim = findViewById(R.id.rl_grade_skim);
        rl_grade_submit = findViewById(R.id.rl_grade_submit);
        tv_defence_type = findViewById(R.id.tv_defence_type);
        grade_skim_icon = findViewById(R.id.grade_skim_icon);
        tv_grade = findViewById(R.id.tv_grade);

        rl_my_group.setOnClickListener(this);
        rl_grade_skim.setOnClickListener(this);
        rl_grade_submit.setOnClickListener(this);

        if (user_type){
            rl_defence_type.setVisibility(View.GONE);
            rl_grade_submit.setVisibility(View.INVISIBLE);
            grade_skim_icon.setVisibility(View.VISIBLE);
            tv_grade.setVisibility(View.INVISIBLE);

            rl_grade_skim.setFocusable(true);
        }else {
            rl_defence_type.setVisibility(View.VISIBLE);
            rl_grade_submit.setVisibility(View.INVISIBLE);
            grade_skim_icon.setVisibility(View.INVISIBLE);
            tv_grade.setVisibility(View.VISIBLE);

            rl_grade_skim.setFocusable(false);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_my_group:   //分组情况
                intent = new Intent(DefenceAndGradeActivity.this, MyGroupActivity.class);
                if (!user_type){
                    intent.putExtra("StudentInfo",student);
                }
                startActivity(intent);
                break;

            case R.id.rl_grade_skim: //成绩查看
                intent = new Intent(DefenceAndGradeActivity.this, MyStudentsActivity.class);
                intent.putExtra("Layout", 2); //成绩
                startActivity(intent);
                break;

            case R.id.rl_grade_submit:
                intent = new Intent(DefenceAndGradeActivity.this, TestActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
