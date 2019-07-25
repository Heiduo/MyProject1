package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import model.StudentInfo;

public class ProjectDetail extends Activity implements View.OnClickListener {
    private final static String TAG = "ProjectDetail";

    private TextView tv_project_detail_name, tv_pro_teacherName,
            tv_pro_require, tv_pro_task_progress, tv_project_submit;
    private Spinner spinner_stage;
    private SeekBar seekBar;
    private EditText et_pro_progress;
    private String user_ObjectId;
    private StudentInfo student;
    private String[] pro_stage;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);

        initView();
        init();
    }

    private void init() {
        Resources res = getResources();
        pro_stage = res.getStringArray(R.array.project_stage);

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

                    //装填布局
                    tv_project_detail_name.setText(student.getProject().getProjectName()
                            == null ? "" : student.getProject().getProjectName());
                    tv_pro_teacherName.setText(student.getTeacher().getTeacherName()
                            == null ? "" : student.getTeacher().getTeacherName());
                    tv_pro_require.setText(student.getProject().getProjectRequire()
                            == null ? "" : student.getProject().getProjectRequire());
                    tv_pro_task_progress.setText("" + student.getTaskProgress());
                    et_pro_progress.setText("" + student.getProjectprogress());
                    seekBar.setProgress(student.getProjectprogress());
                    for (int i = 0; i < pro_stage.length; i++) {
                        if (student.getProjectstage().equals(pro_stage[i])) {
                            spinner_stage.setSelection(i);
                        }
                    }

                    et_pro_progress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            int x = Integer.parseInt(charSequence.toString());
                            if (x > 100) {
                                x = 100;
                                et_pro_progress.setText("100");
                            } else if (x < 0) {
                                x = 0;
                                et_pro_progress.setText("0");
                            }
                            seekBar.setProgress(x);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            et_pro_progress.setText("" + i);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                } else {
                    Log.e(TAG, "fail:" + e.toString());
                }
            }
        });
    }

    private void initView() {
        tv_project_detail_name = findViewById(R.id.tv_project_detail_name);
        tv_pro_teacherName = findViewById(R.id.tv_pro_teacherName);
        tv_pro_require = findViewById(R.id.tv_pro_require);
        tv_pro_task_progress = findViewById(R.id.tv_pro_task_progress);
        spinner_stage = findViewById(R.id.spinner_stage);
        et_pro_progress = findViewById(R.id.et_pro_progress);
        tv_project_submit = findViewById(R.id.tv_project_submit);
        seekBar = findViewById(R.id.seekBar);

        tv_project_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_project_submit:
                //保存
                //student.setProjectstage(spinner_stage.getPrompt().toString());
                Log.i(TAG, spinner_stage.getSelectedItem().toString()
                        + " " + et_pro_progress.getText());
                student.setProjectstage(spinner_stage.getSelectedItem().toString());
                student.setProjectprogress(Integer.parseInt(et_pro_progress.getText().toString()));

                student.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(ProjectDetail.this,
                                    "保存成功!", Toast.LENGTH_SHORT).show();
                        } else {
                        Toast.makeText(ProjectDetail.this,
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
