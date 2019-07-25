package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.ArrayList;

import model.StudentInfo;

public class MyStudentsGradeActivity extends Activity {
    private ListView students_listview;
    private LinearLayout ll_no_student;
    private TextView tv_studentName,tv_studentId,tv_projectName,tv_projectStage,tv_projectProgress;

    private StudentInfo student1,student2;
    private ArrayList<StudentInfo> list_students = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students);

        initView();
    }

    private void initView() {
        init();
        students_listview = findViewById(R.id.students_listView);
        ll_no_student = findViewById(R.id.ll_no_student);

        list_students.add(student1);
        list_students.add(student2);

        //适配器
        students_listview.setDividerHeight(10);
        students_listview.setCacheColorHint(0);
        students_listview.setSelection(R.color.transparent);
        students_listview.setAdapter(new BaseAdapter() {
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
                if (view ==null){
                    LayoutInflater inflater = MyStudentsGradeActivity.this.getLayoutInflater();
                    studentView = inflater.inflate(R.layout.list_item_student_grade,null);
                }else {
                    studentView = view;
                }

                StudentInfo studentInfo = list_students.get(i);
                tv_studentName = studentView.findViewById(R.id.tv_item_studentName);
                tv_studentId = studentView.findViewById(R.id.tv_item_studentId);
                tv_projectName = studentView.findViewById(R.id.tv_item_projectName);
                tv_projectStage = studentView.findViewById(R.id.tv_item_projectStage);
                tv_projectProgress = studentView.findViewById(R.id.tv_item_projectProgress);

                tv_studentName.setText(studentInfo.getStudengName());
                tv_studentId.setText("学号: "+ studentInfo.getStudentId());
                tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());
                tv_projectStage.setText("毕设阶段: "+ studentInfo.getProjectstage());
                tv_projectProgress.setText("毕设进度: " + studentInfo.getProjectprogress()+ "%");

                return studentView;
            }
        });

        //item的点击事件
        students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(MyStudentsGradeActivity.this,MyStudentsDetailActivity.class);
                intent.putExtra("StudentInfo",student);
                startActivity(intent);
            }
        });

    }

    private void init() {
    }


}
