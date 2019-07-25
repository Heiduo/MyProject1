package activity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import model.FileGroup;
import model.GroupInfo;
import model.StudentInfo;
import model.TeacherInfo;

public class MyStudentsFilesActivity extends Activity {
    private final static String TAG = "studentD";
    private final static int FLAG = -1;

    private ListView students_listview;
    private LinearLayout ll_no_student_defence;
    private TextView tv_defence_save;
    private Button bt_save;

    private StudentInfo student1, student2;
    private ArrayList<StudentInfo> list_students = new ArrayList<>();
    private ArrayList<FileGroup> list_students_file_ALL = new ArrayList<>();
    private ArrayList<FileGroup> list_students_file_first = new ArrayList<>();

    private MyDefenceBaseAdapter adapter;

    SharedPreferences sharedPreferences;
    private String[] file_opinion;
    private String[] paper_type_name;
    private int paper_type;
    private int list_add_num = 0;

    private PopupWindow popupWindow, popupDetermineWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_student_defence);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);
        paper_type = (int) getIntent().getSerializableExtra("paperType"); //获取要装填的信息
        list_students.addAll((Collection<? extends StudentInfo>)
                getIntent().getSerializableExtra("student_list"));

        initView();
    }

    private void initView() {
        students_listview = findViewById(R.id.student_denfence_listview);
        // ll_no_student_defence = findViewById(R.id.ll_no_student_defence);
        tv_defence_save = findViewById(R.id.tv_defence_save);

        Resources res = getResources();
        file_opinion = res.getStringArray(R.array.file_opinion);
        paper_type_name = res.getStringArray(R.array.paper_type);

        init();
    }

    private class DefenceOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int s_flag = (int) students_listview.getTag();
            if (s_flag == FLAG) {
                Toast.makeText(MyStudentsFilesActivity.this,
                        "没有数据更改", Toast.LENGTH_SHORT).show();
                popupDetermineWindow.dismiss();
            } else {
                int i = (int) students_listview.getTag();
                MyDefenceBaseAdapter.ViewH viewH = (MyDefenceBaseAdapter.ViewH) //注意方法
                        getViewByPosition(i, students_listview).getTag();

                /*int j ;
                for (j = 0; j < list_students_file.size(); j++) {
                    if (list_students_file.get(j).getStudent().getObjectId().
                            equals(list_students.get(i).getObjectId())) {

                        Log.i(TAG, "文件更新位置"+ i + " list "+j);
                        break;
                    }
                }*/

                switch (view.getId()) {
                    case R.id.show_pop_file_normal:
                        list_students_file_first.get(i).setFileOpinion(0);
                        Log.i(TAG, "文件更新Normal" + list_students_file_first
                                .get(i).getFileOpinion());
                        adapter.upgradeDefence(viewH, 0);
                        popupWindow.dismiss();
                        break;

                    case R.id.show_pop_file_small:
                        list_students_file_first.get(i).setFileOpinion(1);
                        adapter.upgradeDefence(viewH, 1);
                        Log.i(TAG, "文件更新small" + list_students_file_first
                                .get(i).getFileOpinion());
                        popupWindow.dismiss();
                        break;

                    case R.id.show_pop_file_big:
                        list_students_file_first.get(i).setFileOpinion(2);
                        Log.i(TAG, "文件更新big" + list_students_file_first
                                .get(i).getFileOpinion());
                        adapter.upgradeDefence(viewH, 2);
                        popupWindow.dismiss();
                        break;

                    case R.id.show_pop_file_cancel:
                        popupWindow.dismiss();
                        break;

                    case R.id.tv_defence_save:
                        showDefenceDeterminePop();
                        break;

                    case R.id.my_student_defence_pop_cancel:
                        popupDetermineWindow.dismiss();
                        break;

                    case R.id.my_student_defence_pop_determine:          //更新文档意见
                        List<BmobObject> studentsFileOpinion = new ArrayList<>();

                        studentsFileOpinion.addAll(list_students_file_first);
                        Log.i(TAG, "文件更新大小" + studentsFileOpinion.size());
                        new BmobBatch().updateBatch(studentsFileOpinion).doBatch(new QueryListListener<BatchResult>() {
                            @Override
                            public void done(List<BatchResult> list, BmobException e) {
                                if (e == null) {
                                    for (int k = 0; k < list.size(); k++) {
                                        BatchResult batchResult = list.get(k);
                                        BmobException ex = batchResult.getError();
                                        if (ex == null) {
                                            Log.i(TAG, "第" + k + "个数据批量更新成功：" +
                                                    batchResult.getCreatedAt()
                                                    + "," + batchResult.getObjectId() + "," +
                                                    batchResult.getUpdatedAt());
                                        } else {
                                            Log.e(TAG, "第" + k + "个数据批量更新失败：" +
                                                    ex.getMessage() + "," + ex.getErrorCode());

                                        }
                                    }

                                    Toast.makeText(MyStudentsFilesActivity.this,
                                            "意见修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e(TAG, "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                    Toast.makeText(MyStudentsFilesActivity.this,
                                            "修改失败", Toast.LENGTH_SHORT).show();
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

    private View getViewByPosition(int pos, ListView listView) {          //获取listView的item布局
        final int firstItemPos = listView.getFirstVisiblePosition();
        final int lastItemPos = firstItemPos + listView.getChildCount() - 1;

        if (pos < firstItemPos || pos > lastItemPos) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
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
            if (view == null) {
                LayoutInflater inflater = MyStudentsFilesActivity.this.getLayoutInflater();
                studentView = inflater.inflate(R.layout.list_item_student_file, null);
            } else {
                studentView = view;
            }

            viewH.tv_studentName = studentView.findViewById(R.id.tv_item_file_studentName);
            viewH.tv_studentId = studentView.findViewById(R.id.tv_item_file_studentId);
            viewH.tv_projectName = studentView.findViewById(R.id.tv_item_file_projectName);
            viewH.tv_updateTime = studentView.findViewById(R.id.tv_item_file_time);
            viewH.tv_defenceEdit = studentView.findViewById(R.id.tv_file_edit);
            viewH.tv_item_file_type = studentView.findViewById(R.id.tv_item_file_type);
            studentView.setTag(viewH);

            StudentInfo studentInfo = list_students.get(i);
            FileGroup fileGroup = new FileGroup();
            //在list中获取同学最新的文档
            for (int j = 0; j < list_students_file_ALL.size(); j++) {
                if (list_students_file_ALL.get(j).getStudent().getObjectId().
                        equals(list_students.get(i).getObjectId())) {
                    fileGroup = list_students_file_ALL.get(j);

                    list_students_file_first.add(list_students_file_ALL.get(j));

                    Log.i(TAG, "文件信息已经匹配" + fileGroup.getStudent().getStudentName()
                            + " " + studentInfo.getObjectId());
                    Log.i(TAG, "文件信息已经匹配" + fileGroup.toString());
                    break;
                }
            }

            viewH.tv_studentName.setText(studentInfo.getStudengName());
            viewH.tv_studentId.setText("学号: " + studentInfo.getStudentId());
            viewH.tv_projectName.setText("毕设题目: " + studentInfo.getProjectName());
            viewH.tv_item_file_type.setText(paper_type_name[paper_type]);

            //students_listview.setTag(i);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            if (fileGroup.getFilePath() == null) {
                viewH.tv_updateTime.setText(file_opinion[3]);
                viewH.tv_defenceEdit.setText(file_opinion[4]);
                viewH.tv_defenceEdit.setTextColor(Color.BLACK);

            } else {
                if (fileGroup.getFileOpinion() == 0) {
                    viewH.tv_defenceEdit.setTextColor(Color.BLACK);
                    viewH.tv_defenceEdit.setText(file_opinion[0]);
                } else if (fileGroup.getFileOpinion() == 1) {
                    viewH.tv_defenceEdit.setTextColor(Color.BLUE);
                    viewH.tv_defenceEdit.setText(file_opinion[1]);
                } else if (fileGroup.getFileOpinion() == 2) {
                    viewH.tv_defenceEdit.setTextColor(Color.RED);
                    viewH.tv_defenceEdit.setText(file_opinion[2]);
                } else if (fileGroup.getFileOpinion() == 3) {
                    viewH.tv_defenceEdit.setText(file_opinion[3]);
                }
                try {
                    Date mDate = dateFormat.parse(fileGroup.getCreatedAt());
                    String date = dateFormat.format(mDate);
                    viewH.tv_updateTime.setText(date);

                } catch (ParseException ex) {
                    viewH.tv_updateTime.setText("暂未上传");
                    ex.printStackTrace();
                }
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
        void upgradeDefence(ViewH view, int i) {
            switch (i) {
                case 0:
                    view.tv_defenceEdit.setTextColor(Color.BLACK);
                    view.tv_defenceEdit.setText(file_opinion[0]);
                    break;
                case 1:
                    view.tv_defenceEdit.setTextColor(Color.BLUE);
                    view.tv_defenceEdit.setText(file_opinion[1]);
                    break;
                case 2:
                    view.tv_defenceEdit.setTextColor(Color.RED);
                    view.tv_defenceEdit.setText(file_opinion[2]);
                    break;
                default:
                    break;
            }
        }

        private class ViewH {
            private TextView tv_studentName;
            private TextView tv_studentId;
            private TextView tv_projectName;
            private TextView tv_updateTime;
            private TextView tv_defenceEdit;
            private TextView tv_item_file_type;
        }
    }

    private void init() {
        //for (int i=0;i < list_students.size();i++){

        BmobQuery<FileGroup> fileGroupBmobQuery = new BmobQuery<>();
        //fileGroupBmobQuery.addWhereEqualTo("student",list_students.get(i));
        fileGroupBmobQuery.addWhereEqualTo("paperType", paper_type);
        fileGroupBmobQuery.include("student");
        fileGroupBmobQuery.order("-createdAt");
        fileGroupBmobQuery.findObjects(new FindListener<FileGroup>() {
            @Override
            public void done(List<FileGroup> listFile, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "文件信息查询成功");
                    //获取最新信息
                    //FileGroup fileInfo = listFile.get(0);
                    //list_students_file.add(fileInfo);
                    //list_students_file.set(list_add_num,listFile.get(0));
                    list_students_file_ALL.addAll(listFile);
                    Log.i(TAG, list_students_file_ALL.size() + "文件数量总量");

                    adapter = new MyDefenceBaseAdapter();
                    students_listview.setDividerHeight(5);
                    students_listview.setCacheColorHint(0);
                    students_listview.setSelection(R.color.transparent);
                    students_listview.setAdapter(adapter);
                } else {
                    Log.e(TAG, "文件信息查询失败" + e.toString());
                }
            }
        });
        //}

        //添加保存按钮
        View view = (View) LayoutInflater.from(MyStudentsFilesActivity.this).
                inflate(R.layout.z_save_textviw_button, null);
        students_listview.addFooterView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDefenceDeterminePop();
            }
        });

        Log.i(TAG, list_students.size() + " 学生");
        Log.i(TAG, list_students_file_ALL.size() + " 总量");

        //学生item的点击事件
        students_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == -1) {
                    showDefenceDeterminePop();
                } else {/*  重新写!!!!!!!!!!*/
                    StudentInfo student = (StudentInfo) adapterView.getAdapter().getItem(i);
                    ArrayList<FileGroup> list_students_file_one = new ArrayList<>();
                    for (int j = 0; j < list_students_file_ALL.size(); j++) {
                        if (list_students_file_ALL.get(j).getStudent().getObjectId().
                                equals(student.getObjectId())) {
                            list_students_file_one.add(list_students_file_ALL.get(j));
                        }
                        Log.i(TAG, "获取单人文件信息: " + list_students_file_one.size());
                    }

                    Intent intent = new Intent(MyStudentsFilesActivity.this
                            , TeacherDowload.class);
                    intent.putExtra("StudentFileInfo", list_students_file_one);
                    intent.putExtra("paperType",paper_type);
                    startActivity(intent);
                }
                Log.i(TAG, "单击" + i + " " + l);
            }
        });
    }
    //意见弹框
    private void showDefencePop() {
        View view = LayoutInflater.from(this).inflate(R.layout.show_pop_file_opinion, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(students_listview, Gravity.BOTTOM, 0, 0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        view.findViewById(R.id.show_pop_file_normal).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.show_pop_file_small).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.show_pop_file_big).setOnClickListener(new DefenceOnClickListener());
        view.findViewById(R.id.show_pop_file_cancel).setOnClickListener(new DefenceOnClickListener());

    }
    //意见确定弹框
    private void showDefenceDeterminePop() {
        View view = LayoutInflater.from(this).inflate(R.layout.my_student_defence_pop, null);
        popupDetermineWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupDetermineWindow.setOutsideTouchable(true);

        popupDetermineWindow.showAtLocation(students_listview, Gravity.BOTTOM, 0, 0);

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
