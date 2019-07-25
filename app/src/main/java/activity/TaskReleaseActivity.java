package activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import model.ProjectInfo;
import model.StudentInfo;
import model.TaskProgress;

public class TaskReleaseActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "TASKRELEASE";
    private TextView tv_task_projectName;
    private EditText et_task_projectName, et_design_require,
            et_work_importance, et_time_arrange, et_reference_detail;
    RadioGroup rg_project_source;
    RadioButton rb_1, rb_2, rb_3, rb_4;
    private Button bt_task_release_save;

    private int temp;
    private ProjectInfo project;
    private StudentInfo student;
    private String[] array_type;
    private Boolean user_type;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_release);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type",true);

        initView();
        init();
    }

    private void init() {
        //获取item传过来的学生信息
        student = (StudentInfo) getIntent().getSerializableExtra("StudentInfo");
        Log.i(TAG,student.getObjectId());

        project = new ProjectInfo();
        BmobQuery<ProjectInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("student",student);
        if (user_type){
            //先从网络获取
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }else {
            //
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }
        bmobQuery.findObjects(new FindListener<ProjectInfo>() {
            @Override
            public void done(List<ProjectInfo> list, BmobException e) {
                if (e == null) {
                    project = list.get(list.size() - 1);

                    tv_task_projectName = findViewById(R.id.tv_task_projectName);
                    et_task_projectName = findViewById(R.id.et_task_projectName);
                    et_design_require = findViewById(R.id.et_design_require);
                    et_work_importance = findViewById(R.id.et_work_importance);
                    et_time_arrange = findViewById(R.id.et_time_arrange);
                    et_reference_detail = findViewById(R.id.et_reference_detail);
                    rg_project_source = findViewById(R.id.rg_project_source);

                    int i = list.size() - 1;
                    Log.i(TAG,""+i);
                    rb_1 = findViewById(R.id.rb_1);
                    rb_2 = findViewById(R.id.rb_2);
                    rb_3 = findViewById(R.id.rb_3);
                    rb_4 = findViewById(R.id.rb_4);

                    temp = rb_1.getId();

                    //装填界面
                    //获取类型资源
                    Resources res = getResources();
                    array_type = res.getStringArray(R.array.project_type);
                    Log.i(TAG, "type : " + array_type.length + " " + array_type[0] + " " +
                            array_type[1] + " " + array_type[2] + " " + array_type[3]);

                    String type = project.getProjectType();

                    if (type.equals(array_type[0])) {
                        rb_1.setChecked(true);
                    } else if (type.equals(array_type[1])) {
                        rb_2.setChecked(true);
                    } else if (type.equals(array_type[2])) {
                        rb_3.setChecked(true);
                    } else if (type.equals(array_type[3])) {
                        rb_4.setChecked(true);
                    }

                    tv_task_projectName.setText(project.getProjectName());
                    et_task_projectName.setText(project.getProjectEnglish() == null
                            ? "" : project.getProjectEnglish());
                    et_design_require.setText(project.getDesignRequire() == null
                            ? "" : project.getDesignRequire());
                    et_work_importance.setText(project.getWorkImportance() == null
                            ? "" : project.getWorkImportance());
                    et_time_arrange.setText(project.getTimeArrange() == null
                            ? "" : project.getTimeArrange());
                    et_reference_detail.setText(project.getReferenceDetail() == null
                            ? "" : project.getReferenceDetail());

                } else {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private void initView() {
        bt_task_release_save = findViewById(R.id.bt_task_release_save);

        bt_task_release_save.setOnClickListener(this);

        //project = (ProjectInfo) getIntent().getSerializableExtra("StudentInfo");
        //System.out.println(project.toString());
        //设置title
        //spinner_times.setOnItemClickListener();
    }

    @Override
    public void onClick(View view) {
        String teacherName = sharedPreferences.getString("userName", "");
        switch (view.getId()) {
            case R.id.bt_task_release_save:
                //更新毕设类型
                int i = rg_project_source.getCheckedRadioButtonId() - temp;
                if (i < 0 || i > array_type.length) {
                    project.setProjectType("");
                } else {
                    project.setProjectType(array_type[i]);
                }
                //更新英文标题
                String data1 = et_task_projectName.getText().toString().trim();
                if (data1.equals("")) {
                    project.setProjectEnglish("");
                } else if (!project.getProjectEnglish().equals(data1)) {
                    project.setProjectEnglish(data1);
                }
                //更新设计参数
                String data2 = et_design_require.getText().toString().trim();
                if (data2.equals("")) {
                    project.setDesignRequire("");
                } else if (!project.getDesignRequire().equals(data2)) {
                    project.setDesignRequire(data2);
                }
                //更新个人重点工作
                String data3 = et_work_importance.getText().toString().trim();
                if (data3.equals("")) {
                    project.setWorkImportance("");
                } else if (!project.getWorkImportance().equals(data3)) {
                    project.setWorkImportance(data3);
                }
                //更新时间安排
                String data4 = et_time_arrange.getText().toString().trim();
                if (data4.equals("")) {
                    project.setTimeArrange("");
                } else if (!project.getTimeArrange().equals(data4)) {
                    project.setTimeArrange(data4);
                }
                //更新参考文献目录
                String data5 = et_reference_detail.getText().toString().trim();
                if (data5.equals("")) {
                    project.setReferenceDetail("");
                } else if (!project.getReferenceDetail().equals(data5)) {
                    project.setReferenceDetail(data5);
                }

                //更新
                project.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(TaskReleaseActivity.this,
                                    "保存成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TaskReleaseActivity.this,
                                    "保存失败,请重试", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.toString());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
