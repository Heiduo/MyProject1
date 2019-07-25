package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import model.ProjectInfo;
import model.StudentInfo;
import model.StudentSelectInfo;

public class MyProjectsActivity extends Activity {
    private final static String TAG = "Project";

    private MyBaseAdapter adapter;
    private TextView tv_projectName, tv_projectRequire, tv_studentSelect;
    private ListView projects_listView;
    private LinearLayout ll_no_project;

    private ArrayList<ProjectInfo> list_selcet = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);
        Bmob.initialize(this,MainActivity.ApplicationID);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        init();
        initView();

    }

    private void initView() {
        projects_listView = findViewById(R.id.projects_listView);
        ll_no_project = findViewById(R.id.ll_no_project);
    }

    private void init() {
        //获取登录者信息
        String teacherName = sharedPreferences.getString("userName",null);

        BmobQuery<ProjectInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.include("student");
        bmobQuery.addWhereEqualTo("projectTeacher",teacherName);
        bmobQuery.order("-createdAt");
        //先从网络获取 再经缓存
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        bmobQuery.findObjects(new FindListener<ProjectInfo>() {
            @Override
            public void done(List<ProjectInfo> list, BmobException e) {
                if (e == null){
                    Log.i(TAG,"select success " +list.size());
                    for (int i =0;i<list.size();i++){
                        ProjectInfo studentSelectInfo= list.get(i);
                        System.out.println(studentSelectInfo.toString());

                        list_selcet.add(studentSelectInfo);
                        //适配器
                        adapter = new MyBaseAdapter();
                        projects_listView.setDividerHeight(10);
                        projects_listView.setCacheColorHint(0);
                        projects_listView.setSelection(R.color.transparent);
                        projects_listView.setAdapter(adapter);
                    }

                }else {
                    Log.e(TAG,"defeat "+e.getMessage());
                }
            }
        });
    }

    private class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list_selcet.size();
        }

        @Override
        public Object getItem(int i) {
            return list_selcet.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View projectView = null;
            if (view ==null){
                LayoutInflater inflater = MyProjectsActivity.this.getLayoutInflater();
                projectView = inflater.inflate(R.layout.list_item_projects,null);
            }else {
                projectView = view;
            }

            final ProjectInfo projectInfo = list_selcet.get(i);
            tv_projectName = projectView.findViewById(R.id.tv_projectName_item);
            tv_projectRequire = projectView.findViewById(R.id.tv_require_item);
            tv_studentSelect = projectView.findViewById(R.id.tv_student_select);

            String s = "毕设题目: " + projectInfo.getProjectName();
            tv_projectName.setText(s);

            String st_require = "技术要求: ";
            if(projectInfo.getProjectRequire() != null){
                st_require += projectInfo.getProjectRequire();
            }
            tv_projectRequire.setText(st_require);
            //学生姓名
            String studentName;
            if (projectInfo.getStudent() == null){
                studentName = "暂无人选";
                //tv_studentSelect.setTextColor(R.color.SpecialBlue);
                tv_studentSelect.setTextColor(Color.BLUE);
                tv_studentSelect.setText(studentName);
            }else {
                studentName = projectInfo.getStudent().getStudentName();
                tv_studentSelect.setText(studentName);
                tv_studentSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyProjectsActivity.this,MyStudentsDetailActivity.class);
                        intent.putExtra("StudentInfo",projectInfo.getStudent());
                        startActivity(intent);
                    }
                });
            }

            return projectView;
        }
    }
}
