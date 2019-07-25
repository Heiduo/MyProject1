package activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.SignUpAnnounceAdapter;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import model.AdminInfo;
import model.AnnounceInfo;
import model.StudentInfo;
import model.TeacherInfo;

import static java.lang.Thread.sleep;

/**
 * Created by Heiduo on 2019/4/16.
 */
public class MyAnnounceActivity extends Activity {
    private final static String TAG = "Announce";

    private ListView sign_up_announce_listview;
    private LinearLayout ll_no_announce;
    private Button bt_refresh;
    private View mFootView;
    private MyBaseAdapter adapter;
    private int pageNum = 1;
    private ArrayList<AnnounceInfo> list_announce = new ArrayList<>();

    private Context mContext;
    AdminInfo admin;
    private Boolean userType;
    private TeacherInfo teacher;
    private StudentInfo student;
    SharedPreferences sharedPreferences;
    private AnnounceInfo announce;//需要删除的公告
    PopupWindow deleteWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_announce);

        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference,MODE_PRIVATE);
        userType = sharedPreferences.getBoolean("user_type",false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (userType){
            teacher = new TeacherInfo();
            teacher = (TeacherInfo) getIntent().getSerializableExtra("teacher");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyAnnounceActivity.this,TeacherPunishAnnounce.class);
                    intent.putExtra("teacher",teacher);
                    //startActivity(intent);
                    startActivityForResult(intent, 99);
                }
            });
        }else {
            student = new StudentInfo();
            student = (StudentInfo) getIntent().getSerializableExtra("student");
            fab.setVisibility(View.GONE);
        }
        init();
        findView();
    }

    public void init() {
        BmobQuery<AnnounceInfo> query1 = new BmobQuery<AnnounceInfo>();
        query1.include("teacher");
        if (!userType){//学生查找
            Log.i(TAG, "学生公告");
            BmobQuery<AnnounceInfo> eq1 = new BmobQuery<>();
            eq1.addWhereEqualTo("announceAdminName","管理员: heiduo");
            BmobQuery<AnnounceInfo> eq2 = new BmobQuery<>();
            TeacherInfo teacherInfo = new TeacherInfo();
            teacherInfo.setObjectId(student.getTeacher().getObjectId());
            eq2.addWhereEqualTo("teacher",teacherInfo);
            BmobQuery<AnnounceInfo> eq3 = new BmobQuery<>();
            eq3.addWhereEqualTo("state","publishing");
            List<BmobQuery<AnnounceInfo>> queries = new ArrayList<BmobQuery<AnnounceInfo>>();
            queries.add(eq1);
            queries.add(eq2);
            BmobQuery<AnnounceInfo> mainQuery = new BmobQuery<AnnounceInfo>();
            BmobQuery<AnnounceInfo> or = mainQuery.or(queries);
            List<BmobQuery<AnnounceInfo>> andQuerys = new ArrayList<BmobQuery<AnnounceInfo>>();
            andQuerys.add(or);
            andQuerys.add(eq3);
            query1.and(andQuerys);
        }else {
            query1.addWhereEqualTo("state","publishing");
            Log.i(TAG, "老师公告");
        }
        //查询公告
        query1.order("-createdAt");

        //先从网络获取 无网络从缓存开始
        query1.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query1.findObjects(new FindListener<AnnounceInfo>() {
            @Override
            public void done(List<AnnounceInfo> list, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "查询成功" + list.size());
                    list_announce = new ArrayList<>();
                    list_announce.addAll(list);
                    /*for (int i = 0; i < list.size(); i++) {
                        AnnounceInfo announceInfo = list.get(i);
                        list_announce.add(announceInfo);
                    }*/
                    adapter = new MyBaseAdapter();//适配器

                    sign_up_announce_listview.setDividerHeight(10);
                    sign_up_announce_listview.setCacheColorHint(0);
                    sign_up_announce_listview.setSelection(R.color.transparent);
                    sign_up_announce_listview.setAdapter(adapter);

                } else {
                    sign_up_announce_listview.setEmptyView(ll_no_announce);
                    /**
                     * 当出现“暂无公告”时，点击 “点击刷新”，进行刷新
                     */
                    //刷新从网络获取 再从缓存
                    bt_refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            init();
                        }
                    });
                    Log.e(TAG, e.toString() + " " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void findView() {
        sign_up_announce_listview = findViewById(R.id.sign_up_announce_listview);
        ll_no_announce = findViewById(R.id.ll_no_announce);
        bt_refresh = findViewById(R.id.bt_refresh);
    }

    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list_announce.size();
        }

        @Override
        public Object getItem(int i) {
            return list_announce.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = null;
            if (view == null) {
                LayoutInflater inflater = MyAnnounceActivity.this.getLayoutInflater();
                view1 = inflater.inflate(R.layout.list_item_announce, null);
            } else {
                view1 = view;
            }

            AnnounceInfo announceInfo = list_announce.get(i);

            TextView tv_announce_name = view1.findViewById(R.id.tv_announce_name);
            TextView tv_introduce = view1.findViewById(R.id.tv_introduce);
            TextView tv_introduce2 = view1.findViewById(R.id.tv_introduce2);
            TextView tv_admin_name = view1.findViewById(R.id.tv_admin_name);
            TextView tv_announce_time = view1.findViewById(R.id.tv_announce_time);

            tv_announce_name.setText(announceInfo.getannounceName());
            tv_introduce.setText(announceInfo.getannounceIntroduce() ==
                    null ? "": announceInfo.getannounceIntroduce());
            tv_introduce2.setText(announceInfo.getannounceIntroduce2() ==
                    null ? "": announceInfo.getannounceIntroduce2());
            tv_admin_name.setText(announceInfo.getAnnounceAdminName() ==
                    null ? "老师: "+announceInfo.getTeacher().getTeacherName()
                    : announceInfo.getAnnounceAdminName());
            //tv_admin_name.setText( announceInfo.getTeacher().getTeacherName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date mDate = dateFormat.parse(announceInfo.getCreatedAt());
                String date = dateFormat.format(mDate);
                tv_announce_time.setText(date);
            } catch (ParseException e) {
                tv_announce_time.setText(announceInfo.getannounceTime() ==
                        null ? "": announceInfo.getannounceTime());
                e.printStackTrace();
            }

            if (userType){
                sign_up_announce_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        announce = (AnnounceInfo) adapterView.getAdapter().getItem(i);
                        if (announce.getTeacher()!=null){
                            if (announce.getTeacher().getObjectId().equals(teacher.getObjectId())){
                                showDeleteWindow();
                            }
                        }
                    }
                });
            }
            return view1;
        }
    }

    private void showDeleteWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.show_pop_delete_announcement, null);
        deleteWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        deleteWindow.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWindow.dismiss();
            }
        });

        view.findViewById(R.id.show_pop_delete_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announce.setState("delete");
                announce.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            showToast("公告删除成功");
                        }else {
                            showToast("公告删除失败");
                            Log.e(TAG,"公告删除失败"+e.getMessage());
                        }
                        init();
                    }
                });
                deleteWindow.dismiss();
            }
        });
        view.findViewById(R.id.show_pop_delete_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWindow.dismiss();
            }

        });
        deleteWindow.showAtLocation(bt_refresh, Gravity.BOTTOM, 0, 0);
    }
    public void showToast(String msg) {

        if (msg == null || msg.equals("")) {
            Log.e("login","无toast信息");
        } else {
            Toast toast = Toast.makeText(MyAnnounceActivity.this, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 99 && resultCode == 11) {// 刷新
            init();
        }
    }
}
