package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import com.example.myproject1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import model.StudentInfo;
import model.TeacherInfo;

public class ThemeManagementActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "ThemeManage";
    private RelativeLayout rl_task_report;
    private RelativeLayout rl_task_report_check;
    private RelativeLayout rl_open_report_check;
    private RelativeLayout rl_document_trans_check;
    private RelativeLayout rl_paper_check;
    private RelativeLayout rl_student_choose;
    private ArrayList<StudentInfo> list_students = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        findView();
    }

    private void findView() {
        rl_task_report = findViewById(R.id.rl_task_report);
        rl_task_report_check = findViewById(R.id.rl_task_report_check);
        rl_open_report_check = findViewById(R.id.rl_open_report_check);
        rl_document_trans_check = findViewById(R.id.rl_document_trans_check);
        rl_paper_check =findViewById(R.id.rl_paper_check);
        rl_student_choose = findViewById(R.id.rl_student_choose);

        rl_task_report.setOnClickListener(this);
        rl_task_report_check.setOnClickListener(this);
        rl_open_report_check.setOnClickListener(this);
        rl_document_trans_check.setOnClickListener(this);
        rl_paper_check.setOnClickListener(this);
        rl_student_choose.setOnClickListener(this);

        //获取老师Id
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setObjectId(sharedPreferences.getString("userObjectId",null));

        BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
        //查找学生
        bmobQuery.addWhereEqualTo("teacher",teacherInfo);
        bmobQuery.include("teacher");
        //先从网络获取
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.findObjects(new FindListener<StudentInfo>() {
            @Override
            public void done(List<StudentInfo> list, BmobException e) {
                if (e == null){
                    Log.i(TAG,"学生信息获取成功"+list.size());
                    for (int i=0;i<list.size();i++){
                        StudentInfo studentInfo = list.get(i);
                        list_students.add(studentInfo);
                    }
                }else {
                    Log.e(TAG,"信息获取失败 "+e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.rl_task_report:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsActivity.class);
                intent.putExtra("Layout",4);
                startActivity(intent);
                break;

            case R.id.rl_task_report_check:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsActivity.class);
                intent.putExtra("Layout",3);
                startActivity(intent);
                break;

            case R.id.rl_open_report_check:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsFilesActivity.class);
                intent.putExtra("student_list",list_students);
                intent.putExtra("paperType",0);
                startActivity(intent);
                break;

            case R.id.rl_document_trans_check:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsFilesActivity.class);
                intent.putExtra("student_list",list_students);
                intent.putExtra("paperType",1);
                startActivity(intent);
                break;

            case R.id.rl_paper_check:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsFilesActivity.class);
                intent.putExtra("student_list",list_students);
                intent.putExtra("paperType",2);
                startActivity(intent);
                break;

            case R.id.rl_student_choose:
                intent = new Intent(ThemeManagementActivity.this,MyStudentsDefenceActivity.class);
                startActivity(intent);
                break;

                default:
                    break;

        }
    }
}
