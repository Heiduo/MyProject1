package activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myproject1.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.util.V;
import model.GroupInfo;
import model.StudentInfo;
import model.TeacherGroup;
import model.TeacherInfo;

public class MyGroupActivity extends Activity {
    private final static String TAG = "Group";
    private TextView tv_groupId, tv_group_myName, tv_group_time, tv_group_address, tv_group_memb1,
            tv_group_memb2, tv_group_memb3, tv_group_determine;
    private RelativeLayout rl_group_member;

    public TeacherInfo teacher;
    public GroupInfo group;
    public String[] teacherName;

    private Boolean user_type;
    private StudentInfo student;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        //获取preference
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);
        user_type = sharedPreferences.getBoolean("user_type",true);

        initView();
        init();
    }

    private void initView() {
        tv_groupId = findViewById(R.id.tv_groupId);
        tv_group_myName = findViewById(R.id.tv_group_myName);
        tv_group_time = findViewById(R.id.tv_group_time);
        tv_group_address = findViewById(R.id.tv_group_address);
        tv_group_memb1 = findViewById(R.id.tv_group_memb1);
        tv_group_memb2 = findViewById(R.id.tv_group_memb2);
        tv_group_memb3 = findViewById(R.id.tv_group_memb3);
        tv_group_determine = findViewById(R.id.tv_group_determine);
        rl_group_member = findViewById(R.id.rl_group_member);

        tv_group_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void init() {
        if (user_type){//老师分组
            rl_group_member.setVisibility(View.VISIBLE);

            teacher = new TeacherInfo();
            group = new GroupInfo();
            teacherName = new String[3];

            //已知登录者
            String userObjectId = sharedPreferences.getString("userObjectId",null);
            //教师
            BmobQuery<TeacherInfo> bmobQuery = new BmobQuery<>();
            bmobQuery.include("group");
            //先从缓存获取
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            bmobQuery.getObject(userObjectId, new QueryListener<TeacherInfo>() {
                @Override
                public void done(TeacherInfo teacherInfo, BmobException e) {
                    if (e == null) {
                        teacher = teacherInfo;
                        group = teacher.getGroup();
                        System.out.println(group.getObjectId());
                        //同组成员
                        //group.setObjectId("665ea79cd2");
                        BmobQuery<TeacherInfo> groupMember = new BmobQuery<>();
                        groupMember.addWhereEqualTo("group", new BmobPointer(group));
                        groupMember.findObjects(new FindListener<TeacherInfo>() {
                            @Override
                            public void done(List<TeacherInfo> list, BmobException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        teacherName[i] = list.get(i).getTeacherName();
                                        Log.i(TAG, teacherName[i]);
                                    }
                                    Log.i(TAG, teacherName[0]);
                                    //数据填充
                                    initData();
                                } else {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        });

                    } else {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        }else {//学生分组
            rl_group_member.setVisibility(View.GONE);
            student = (StudentInfo) getIntent().getSerializableExtra("StudentInfo");
            System.out.println(student.getStudentId());

            tv_groupId.setText(student.getGroup().getGroupId());
            tv_group_myName.setText(student.getStudentName());
            tv_group_time.setText(student.getGroup().getGroupTime());
            tv_group_address.setText(student.getGroup().getGroupAddress());
        }

    }

    //数据填充
    private void initData() {
        tv_groupId.setText(teacher.getGroup().getGroupId());
        tv_group_myName.setText(teacher.getTeacherName());
        tv_group_time.setText(teacher.getGroup().getGroupTime());
        tv_group_address.setText(teacher.getGroup().getGroupAddress());
        tv_group_memb1.setText(teacherName[0]);
        tv_group_memb2.setText(teacherName[1]);
        tv_group_memb3.setText(teacherName[2]);
    }

}
