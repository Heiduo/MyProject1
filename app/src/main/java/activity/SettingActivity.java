package activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends Activity implements View.OnClickListener {
    private TextView tv_logout;
    private RelativeLayout rl_deleteCache;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    PopupWindow logoutWindow, deleteCacheWindow;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Toast.makeText(SettingActivity.this, "清理缓存完成", Toast.LENGTH_LONG);
                    break;
                case 11:
                    setResult(404);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences(MainActivity.ProjectPreference, MODE_PRIVATE);

        initView();
    }

    private void initView() {
        tv_logout = findViewById(R.id.tv_logout);
        rl_deleteCache = findViewById(R.id.rl_deleteCache);

        tv_logout.setOnClickListener(this);
        rl_deleteCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_logout:                  //退出登录
                showLogoutWindow();
                break;

            case R.id.rl_deleteCache:              //清除缓存
                showDeleteWindow();
                break;

            case R.id.show_pop_delete_determine:
                BmobQuery.clearAllCachedResults();//缓存清除
                handler.sendEmptyMessage(10);
                deleteCacheWindow.dismiss();
                break;

            case R.id.show_pop_delete_cancel:
                deleteCacheWindow.dismiss();
                break;

            case R.id.show_pop_logout_determine:
                editor = sharedPreferences.edit();
                editor.putString("userName", null);
                //editor.putBoolean("user_type", false);

                //提交
                editor.apply();
                BmobUser.logOut();
                logoutWindow.dismiss();

                handler.sendEmptyMessage(11);
                break;

            case R.id.show_pop_logout_cancel:
                logoutWindow.dismiss();
                break;

            default:
                break;
        }
    }

    private void showDeleteWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.show_pop_delete_cache, null);
        deleteCacheWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        deleteCacheWindow.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCacheWindow.dismiss();
            }
        });

        view.findViewById(R.id.show_pop_delete_determine).setOnClickListener(this);
        view.findViewById(R.id.show_pop_delete_cancel).setOnClickListener(this);

        deleteCacheWindow.showAtLocation(tv_logout, Gravity.BOTTOM, 0, 0);
    }

    private void showLogoutWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.show_pop_logout, null);
        logoutWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        logoutWindow.setOutsideTouchable(true);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutWindow.dismiss();
            }
        });

        view.findViewById(R.id.show_pop_logout_determine).setOnClickListener(this);
        view.findViewById(R.id.show_pop_logout_cancel).setOnClickListener(this);

        logoutWindow.showAtLocation(tv_logout, Gravity.BOTTOM, 0, 0);
    }
}
