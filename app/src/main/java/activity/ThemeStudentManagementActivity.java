package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.myproject1.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import model.StudentInfo;

public class ThemeStudentManagementActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "THEMESTUDENT";

    private RelativeLayout rl_task_report_student;
    private RelativeLayout rl_task_report_check_student;
    private RelativeLayout rl_open_report_check_student;
    private RelativeLayout rl_document_trans_check_student;
    private RelativeLayout rl_paper_check_student;

    SharedPreferences sharedPreferences;
    private StudentInfo student;
    private String user_ObjectId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_student);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);

        findView();
        init();
    }

    private void init() {
        user_ObjectId = sharedPreferences.getString("userObjectId", null);
        BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.include("project,teacher");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.getObject(user_ObjectId, new QueryListener<StudentInfo>() {
            @Override
            public void done(StudentInfo studentInfo, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "success:");
                    student = studentInfo;
                } else {
                    Log.e(TAG, "fail:" + e.toString());
                }
            }
        });
    }

    private void findView() {
        rl_task_report_student = findViewById(R.id.rl_task_report_student);
        rl_task_report_check_student = findViewById(R.id.rl_task_report_check_student);
        rl_open_report_check_student = findViewById(R.id.rl_open_report_check_student);
        rl_document_trans_check_student = findViewById(R.id.rl_document_trans_check_student);
        rl_paper_check_student = findViewById(R.id.rl_paper_check_student);

        rl_task_report_student.setOnClickListener(this);
        rl_task_report_check_student.setOnClickListener(this);
        rl_open_report_check_student.setOnClickListener(this);
        rl_document_trans_check_student.setOnClickListener(this);
        rl_paper_check_student.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_task_report_student:
                intent = new Intent(ThemeStudentManagementActivity.this,
                        TaskReleaseActivity.class);
                intent.putExtra("StudentInfo", student);
                startActivity(intent);
                break;

            case R.id.rl_task_report_check_student:
                intent = new Intent(ThemeStudentManagementActivity.this,
                        TaskCheckActivity.class);
                intent.putExtra("StudentInfo", student);
                startActivity(intent);
                break;

            case R.id.rl_open_report_check_student:
                intent = new Intent(ThemeStudentManagementActivity.this,
                        StudentUpload.class);
                intent.putExtra("paperType",0);
                startActivity(intent);
                break;

            case R.id.rl_document_trans_check_student:
                intent = new Intent(ThemeStudentManagementActivity.this,
                        StudentUpload.class);
                intent.putExtra("paperType",1);
                startActivity(intent);
                break;

            case R.id.rl_paper_check_student:
                intent = new Intent(ThemeStudentManagementActivity.this,
                        StudentUpload.class);
                intent.putExtra("paperType",2);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}
