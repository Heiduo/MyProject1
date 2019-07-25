package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import model.StudentInfo;
import model.TeacherInfo;

public class MyStudentsDefenceActivity extends Activity {
    private final static String TAG = "studentD";
    private final static int FLAG = -1;

    private ListView students_listview;
    private LinearLayout ll_no_student_defence;
    private TextView tv_defence_save;
    private Button bt_save;

    private StudentInfo student1,student2;
    private ArrayList<StudentInfo> list_students;
    private MyDefenceBaseAdapter adapter;

    SharedPreferences sharedPreferences;

    private String [] defence_type;


    private PopupWindow popupWindow,popupDetermineWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_student_defence);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);

        initView();
    }

    private void initView() {
        students_listview = findViewById(R.id.student_denfence_listview);
       // ll_no_student_defence = findViewById(R.id.ll_no_student_defence);
        tv_defence_save = findViewById(R.id.tv_defence_save);

        Resources res = getResources();
        defence_type = res.getStringArray(R.array.defence_type);


        init();
    }

    private class DefenceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int s_flag = (int) students_listview.getTag();
            if (s_flag == FLAG) {
                Toast.makeText(MyStudentsDefenceActivity.this,
                        "没有数据更改",Toast.LENGTH_SHORT).show();
                popupDetermineWindow.dismiss();
            }else {
                int i = (int) students_listview.getTag();
                MyDefenceBaseAdapter.ViewH viewH = (MyDefenceBaseAdapter.ViewH) //注意方法
                        getViewByPosition(i,students_listview).getTag();
                switch (view.getId()){
                    case R.id.student_defence_pop_normal:
                        list_students.get(i).setDenfeceType(1);
                        //tv_defenceEdit.setText("正常答辩");
                        adapter.upgradeDefence(viewH,list_students.get(i).getDenfeceType());
                        popupWindow.dismiss();
                        break;

                    case R.id.student_defence_pop_delay:
                        list_students.get(i).setDenfeceType(2);
                        //tv_defenceEdit.setText("延期答辩");
                        adapter.upgradeDefence(viewH,list_students.get(i).getDenfeceType());
                        popupWindow.dismiss();
                        break;

                    case R.id.student_defence_pop_cancel:
                        popupWindow.dismiss();
                        break;

                    /*case R.id.bt_defence_save:

                        showDefenceDeterminePop();
                        break;*/

                    case R.id.tv_defence_save:
                        showDefenceDeterminePop();
                        break;

                    case R.id.my_student_defence_pop_cancel:
                        popupDetermineWindow.dismiss();
                        break;

                    case R.id.my_student_defence_pop_determine:          //更新答辩资格
                        List<BmobObject> studentsDefence = new ArrayList<>();
                    /*for (int j = 0;j<list_students.size();j++){
                        studentsDefence.add(list_students.get(j));
                    }*/
                        studentsDefence.addAll(list_students);
                        new BmobBatch().updateBatch(studentsDefence).doBatch(new QueryListListener<BatchResult>() {
                            @Override
                            public void done(List<BatchResult> list, BmobException e) {
                                if(e == null){
                                    for (int k= 0; k< list.size(); k++){
                                        BatchResult batchResult = list.get(k);
                                        BmobException ex = batchResult.getError();
                                        if (ex == null) {
                                            Log.i(TAG, "第" + k + "个数据批量更新成功：" + batchResult.getCreatedAt()
                                                    + "," + batchResult.getObjectId() + "," + batchResult.getUpdatedAt());
                                        } else {
                                            Log.e(TAG, "第" + k + "个数据批量更新失败：" + ex.getMessage() + "," + ex.getErrorCode());

                                        }
                                    }
                                    Toast.makeText(MyStudentsDefenceActivity.this,
                                            "修改成功",Toast.LENGTH_SHORT).show();
                                    //init();
                                }else {
                                    Log.e(TAG, "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                    Toast.makeText(MyStudentsDefenceActivity.this,
                                            "修改失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        popupDetermineWindow.dismiss();
                        break;
                    default:
                        break;
                }
            }


        }
    }

    private View getViewByPosition(int pos,ListView listView){          //获取listView的item布局
        final int firstItemPos =  listView.getFirstVisiblePosition();
        final int lastItemPos = firstItemPos + listView.getChildCount() - 1;

        if (pos < firstItemPos || pos > lastItemPos){
            return listView.getAdapter().getView(pos, null, listView);
        }else {
            final int childIndex = pos - firstItemPos;
            return listView.getChildAt(childIndex);
        }
    }

    private class MyDefenceBaseAdapter extends BaseAdapter {   //适配器
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewH viewH = new ViewH();
            View studentView = null;
            if (view ==null){
                LayoutInflater inflater = MyStudentsDefenceActivity.this.getLayoutInflater();
                studentView = inflater.inflate(R.layout.list_item_student_defence,null);
            }else {
                studentView = view;
            }

            StudentInfo studentInfo = list_students.get(i);

            viewH.tv_studentName = studentView.findViewById(R.id.tv_item_defence_studentName);
            viewH.tv_studentId = studentView.findViewById(R.id.tv_item_defence_studentId);
            viewH.tv_projectName = studentView.findViewById(R.id.tv_item_defence_projectName);
            viewH.tv_defenceEdit = studentView.findViewById(R.id.tv_defence_edit);
            studentView.setTag(viewH);

            viewH.tv_studentName.setText(studentInfo.getStudengName());
            viewH.tv_studentId.setText("学号: "+ studentInfo.getStudentId());
            viewH.tv_projectName.setText("毕设题目: "+ studentInfo.getProjectName());

            //students_listview.setTag(i);

            if (studentInfo.getDenfeceType() == 1){
                viewH.tv_defenceEdit.setTextColor(Color.BLACK);
                viewH.tv_defenceEdit.setText(defence_type[0]);
            }else if(studentInfo.getDenfeceType() == 2){
                viewH.tv_defenceEdit.setTextColor(Color.RED);
                viewH.tv_defenceEdit.setText(defence_type[1]);
            }

            students_listview.setTag(FLAG);

            viewH.tv_defenceEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    students_listview.setTag(i);  //设置Tag,在那个item
                    showDefencePop();
                }
            });

            return studentView;
        }

        //更新答辩资格
        void upgradeDefence(ViewH view, int i){
            switch (i){
                case 1:
                    view.tv_defenceEdit.setTextColor(Color.BLACK);
                    view.tv_defenceEdit.setText("正常答辩");
                    break;
                case 2:
                    view.tv_defenceEdit.setTextColor(Color.RED);
                    view.tv_defenceEdit.setText("延期答辩");
                    break;
                default:
                    break;
            }
        }

        private class ViewH {
            private TextView tv_studentName;
            private TextView tv_studentId;
            private TextView tv_projectName;
            private TextView tv_defenceEdit;
        }
    }

    private void init() {
       // bt_save = findViewById(R.id.bt_defence_save);
//        bt_save.setOnClickListener(new DefenceOnClickListener());

        list_students = new ArrayList<>();
        //获取老师Id
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setObjectId(sharedPreferences.getString("userObjectId",null));

        BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
        //查找学生
        bmobQuery.addWhereEqualTo("teacher",teacherInfo);
        bmobQuery.include("teacher");
        bmobQuery.findObjects(new FindListener<StudentInfo>() {
            @Override
            public void done(List<StudentInfo> list, BmobException e) {
                if (e == null){
                    Log.i(TAG,"学生信息获取成功");
                    for (int i=0;i<list.size();i++){
                        StudentInfo studentInfo = list.get(i);
                        list_students.add(studentInfo);

                        adapter = new MyDefenceBaseAdapter();
                        students_listview.setDividerHeight(5);
                        students_listview.setCacheColorHint(0);
                        students_listview.setSelection(R.color.transparent);
                        students_listview.setAdapter(adapter);

                    }

                    //添加保存按钮
                    View view = (View) LayoutInflater.from(MyStudentsDefenceActivity.this).
                            inflate(R.layout.z_save_textviw_button,null);

                    students_listview.addFooterView(view);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDefenceDeterminePop();
                        }
                    });

                    Log.i(TAG,list_students.size()+"");

                    //学生item的点击事件
                    students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == -1){
                                showDefenceDeterminePop();
                            }else {
                                StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                                Intent intent = new Intent(MyStudentsDefenceActivity.this,MyStudentsDetailActivity.class);
                                intent.putExtra("StudentInfo",student);
                                startActivity(intent);
                            }
                            Log.i(TAG,"单击"+i+" "+l );
                        }
                    });
                }else {
                    Log.e(TAG,"信息获取失败 "+e.getMessage());
                }
            }
        });
    }

    //答辩弹框
    private void showDefencePop() {
        View view = LayoutInflater.from(this).inflate(R.layout.student_defence_show_pop,null);
        popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(students_listview, Gravity.BOTTOM,0,0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.student_defence_pop_normal).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.student_defence_pop_delay).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.student_defence_pop_cancel).setOnClickListener(new DefenceOnClickListener());

        //popupWindow.showAtLocation(); //弹窗位置
    }

    //答辩确定弹框
    private void showDefenceDeterminePop() {
        View view = LayoutInflater.from(this).inflate(R.layout.my_student_defence_pop,null);
        popupDetermineWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        popupDetermineWindow.setOutsideTouchable(true);

        popupDetermineWindow.showAtLocation(students_listview, Gravity.BOTTOM,0,0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDetermineWindow.dismiss();
            }
        });

        view.findViewById(R.id.my_student_defence_pop_determine).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.my_student_defence_pop_cancel).setOnClickListener(new DefenceOnClickListener());

    }
}
