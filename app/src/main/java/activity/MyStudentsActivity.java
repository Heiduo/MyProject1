package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import model.StudentInfo;
import model.TeacherInfo;

public class MyStudentsActivity extends Activity {
    private final static String TAG = "Student";

    private ListView students_listview;
    private LinearLayout ll_no_student;

    private ArrayList<StudentInfo> list_students = new ArrayList<>();
    private MyStudentBaseAdapter adapter;

    SharedPreferences sharedPreferences;

    int layoutNum; //学生信息:1 成绩item : 2与 task: 3
    int function;   //学生信息:1 task : 2

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        initView();
    }

    private void initView() {
        adapter = new MyStudentBaseAdapter();
        students_listview = findViewById(R.id.students_listView);
        ll_no_student = findViewById(R.id.ll_no_student);

        layoutNum = (int) getIntent().getSerializableExtra("Layout"); //获取要装填的信息

        //学生item的点击事件

        init();
    }

    private void init() {
        //获取老师Id
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setObjectId(sharedPreferences.getString("userObjectId",null));

        BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
        //查找学生
        bmobQuery.addWhereEqualTo("teacher",teacherInfo);
        bmobQuery.include("teacher");
        bmobQuery.include("taskInfo");
        //先从网络
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.findObjects(new FindListener<StudentInfo>() {
            @Override
            public void done(List<StudentInfo> list, BmobException e) {
                if (e == null){
                    Log.i(TAG,"success "+list.size());
                    for (int i= 0;i<list.size(); i++){
                        StudentInfo studentInfo = list.get(i);
                        list_students.add(studentInfo);
                        System.out.println(studentInfo.toString());

                        students_listview.setDividerHeight(10);
                        students_listview.setCacheColorHint(0);
                        students_listview.setSelection(R.color.transparent);
                        students_listview.setAdapter(adapter);
                    }
                }else {
                    Log.e(TAG,"defeat "+e.getMessage());
                }
            }
        });
    }

    private class MyStudentBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list_students.size();
        }

        @Override
        public Object getItem(int i) {
            return list_students.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View studentView = null;
            if (layoutNum == 1){ //学生信息item
                if (view ==null){
                    LayoutInflater inflater = MyStudentsActivity.
                            this.getLayoutInflater();
                    studentView = inflater.inflate(R.layout.
                            list_item_student,null);
                }else {
                    studentView = view;
                }
                StudentInfo studentInfo = list_students.get(i);
                TextView tv_studentName = studentView.
                        findViewById(R.id.tv_item_studentName);
                TextView tv_studentId = studentView.
                        findViewById(R.id.tv_item_studentId);
                TextView tv_projectName = studentView.
                        findViewById(R.id.tv_item_projectName);
                TextView tv_projectStage = studentView.
                        findViewById(R.id.tv_item_projectStage);
                TextView tv_projectProgress = studentView.
                        findViewById(R.id.tv_item_projectProgress);
                tv_studentName.setText(studentInfo.getStudentName());

                tv_studentId.setText("学号: "+ studentInfo.getStudentId());
                tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());
                tv_projectStage.setText("毕设阶段: "+ studentInfo.getProjectstage());
                tv_projectProgress.setText("毕设进度: " + studentInfo.getProjectprogress()+ "%");

                students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                        Intent intent = new Intent(MyStudentsActivity.this,MyStudentsDetailActivity.class);
                        intent.putExtra("StudentInfo",student);
                        startActivity(intent);
                    }
                });

            }else if(layoutNum ==2){//学生成绩item
                if (view == null){
                    LayoutInflater inflater = MyStudentsActivity.this.getLayoutInflater();
                    studentView = inflater.inflate(R.layout.list_item_student_grade,null);
                }else {
                    studentView = view;
                }

                StudentInfo studentInfo = list_students.get(i);

                TextView tv_studentName = studentView.findViewById(R.id.tv_item_grade_studentName);
                TextView tv_studentId = studentView.findViewById(R.id.tv_item_grade_studentId);
                TextView tv_projectName = studentView.findViewById(R.id.tv_item_grade_projectName);
                TextView tv_studentGrade = studentView.findViewById(R.id.tv_item_grade_studentGrade);

                tv_studentName.setText(studentInfo.getStudentName());
                tv_studentId.setText("学号: "+ studentInfo.getStudentId());
                tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());
                tv_studentGrade.setText(""+studentInfo.getGrade());

                students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                        Intent intent = new Intent(MyStudentsActivity.this,MyStudentsDetailActivity.class);
                        intent.putExtra("StudentInfo",student);
                        startActivity(intent);
                    }
                });
            }else if(layoutNum == 3){//任务书过程检查item
                if (view ==null){
                    LayoutInflater inflater = MyStudentsActivity.this.getLayoutInflater();
                    studentView = inflater.inflate(R.layout.list_item_student_task,null);
                }else {
                    studentView = view;
                }

                StudentInfo studentInfo = list_students.get(i);

                TextView tv_studentName = studentView.findViewById(R.id.tv_item_studentName);
                TextView tv_studentId = studentView.findViewById(R.id.tv_item_studentId);
                TextView tv_projectName = studentView.findViewById(R.id.tv_item_projectName);
                TextView tv_projectReport = studentView.findViewById(R.id.tv_item_projectReport);
                TextView tv_taskProgress = studentView.findViewById(R.id.tv_item_taskProgress);
                TextView tv_projectStage = studentView.findViewById(R.id.tv_item_projectStage);
                TextView tv_projectProgress = studentView.findViewById(R.id.tv_item_projectProgress);

                tv_studentName.setText(studentInfo.getStudentName());
                tv_studentId.setText("学号: "+ studentInfo.getStudentId());
                tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());
                tv_projectReport.setText("开题报告: "+studentInfo.getProjectReport());
                tv_taskProgress.setText("任务书: 第 " + studentInfo.getTaskProgress()+ "次");
                tv_projectStage.setText("毕设阶段: "+ studentInfo.getProjectstage());
                tv_projectProgress.setText("毕设进度: " + studentInfo.getProjectprogress()+ "%");

                students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                        Intent intent = new Intent(MyStudentsActivity.this, TaskCheckActivity.class);
                        intent.putExtra("StudentInfo",student);
                        startActivity(intent);
                    }
                });
            }else if (layoutNum == 4){ //开题报告下达学生item
                if (view == null){
                    LayoutInflater inflater = MyStudentsActivity.this.getLayoutInflater();
                    studentView = inflater.inflate(R.layout.list_item_student_task_release,null);
                }else {
                    studentView = view;
                }

                StudentInfo studentInfo = list_students.get(i);

                TextView tv_studentName = studentView.findViewById(R.id.tv_item_studentName);
                TextView tv_studentId = studentView.findViewById(R.id.tv_item_studentId);
                TextView tv_projectName = studentView.findViewById(R.id.tv_item_projectName);

                tv_studentName.setText(studentInfo.getStudentName());
                tv_studentId.setText("学号: "+ studentInfo.getStudentId());
                tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());

                students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                        Intent intent = new Intent(MyStudentsActivity.this,TaskReleaseActivity.class);
                        intent.putExtra("StudentInfo",student);
                        startActivity(intent);
                    }
                });
            }

            return studentView;
        }
    }
}
