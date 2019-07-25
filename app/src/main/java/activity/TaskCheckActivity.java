package activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import model.StudentInfo;
import model.TaskProgress;

public class TaskCheckActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "TASKCHECK";
    private TextView tv_task_date, tv_teacher_signature;
    private EditText et_teacher_opinion,et_task;
    private Spinner spinner_times;
    private Button bt_task_save;

    private int the_time;
    private StudentInfo student;
    private TaskProgress task;
    private ArrayList<TaskProgress> task_list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<String> times_list;
    private Boolean user_type;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        sharedPreferences = getSharedPreferences(MainActivity
                .ProjectPreference,MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type",true);

        initView();
        init();
    }
    //组建信息装填
    private void init() {
        BmobQuery<TaskProgress> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("student", student);
        bmobQuery.order("number");

        bmobQuery.findObjects(new FindListener<TaskProgress>() {
            @Override
            public void done(List<TaskProgress> list, BmobException e) {
                if (e == null) {
                    task_list.addAll(list);
                    System.out.println(task_list.size() + " success");

                    if (user_type){
                        et_task.setFocusable(false);
                        et_task.setHint(" ");
                        et_teacher_opinion.setFocusable(true);
                        et_teacher_opinion.setHint("请输入相关意见");
                    }else {
                        et_task.setFocusable(true);
                        et_task.setHint("请输入相关进展");
                        et_teacher_opinion.setFocusable(false);
                        et_teacher_opinion.setHint(" ");
                        tv_teacher_signature.setHint(" ");
                        tv_teacher_signature.setClickable(false);
                    }

                    //配置adapter
                    if (task_list.size() > 0) {
                        times_list = new ArrayList<String>();
                        for (int i = 1; i <= task_list.size(); i++) {
                            times_list.add("第 " + i + "次");
                        }
                        //数据
                        adapter = new ArrayAdapter<String>(TaskCheckActivity.this,
                                android.R.layout.simple_spinner_item, times_list);
                        //下拉
                        adapter.setDropDownViewResource(
                                R.layout.support_simple_spinner_dropdown_item);
                        spinner_times.setAdapter(adapter);

                        //选择
                        spinner_times.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                System.out.println("第" + i);
                                the_time = i;
                                //装填
                                et_task.setText(task_list.get(i).getTask_progress() == null
                                        ? "" : task_list.get(i).getTask_progress());
                                et_teacher_opinion.setText(task_list.get(i).getTeacher_opinion() == null
                                        ? "" : task_list.get(i).getTeacher_opinion());
                                tv_teacher_signature.setText(task_list.get(i).getSignature() == null
                                        ? "" : task_list.get(i).getSignature());
                                //日期
                                SimpleDateFormat dateFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    Date mDate = dateFormat.parse(task_list.get(i).getCreatedAt());
                                    String date = dateFormat.format(mDate);
                                    tv_task_date.setText(date);
                                } catch (ParseException ep) {
                                    tv_task_date.setText("");
                                    ep.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                } else {
                    System.out.println(e.toString());
                }
            }
        });
    }
    //组建获取
    private void initView() {
        tv_task_date = findViewById(R.id.tv_task_date);
        et_task = findViewById(R.id.et_task);
        et_teacher_opinion = findViewById(R.id.et_teacher_opinion);
        tv_teacher_signature = findViewById(R.id.tv_teacher_signature);
        spinner_times = findViewById(R.id.spinner_times);
        bt_task_save = findViewById(R.id.bt_task_save);

        tv_teacher_signature.setOnClickListener(this);
        //et_teacher_opinion.setOnClickListener(this);
        bt_task_save.setOnClickListener(this);

        student = (StudentInfo) getIntent().getSerializableExtra("StudentInfo");
        System.out.println(student.toString());
        //设置title
        //spinner_times.setOnItemClickListener();
    }
    //处理点击事件
    @Override
    public void onClick(View view) {
        String teacherName = sharedPreferences.getString("userName","");
        switch (view.getId()) {
            case R.id.tv_teacher_signature:
                tv_teacher_signature.setText(teacherName);
                break;

            case R.id.bt_task_save:
                task = task_list.get(the_time);
                //更新意见
                String opinion = et_teacher_opinion.getText().toString();
                if (opinion.equals("")){
                    task.setTeacher_opinion("");
                }else{
                    task.setTeacher_opinion(opinion);
                }
                //更新签名
                String signature = tv_teacher_signature.getText().toString().trim();
                if (signature.equals("")){
                    task.setSignature("");
                }else{
                    task.setSignature(signature);
                }
                //更新
                task.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(TaskCheckActivity.this,
                                    "保存成功!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(TaskCheckActivity.this,
                                    "保存失败,请重试",Toast.LENGTH_SHORT).show();
                            Log.e(TAG,e.toString());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
