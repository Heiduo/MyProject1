package activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myproject1.R;


import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import model.StudentInfo;
import model.TeacherInfo;

import static activity.MainActivity.ProjectPreference;


public class LoginActivity extends Activity implements OnClickListener {
	private final static String TAG = "Login";

	private Button login; // 登录按钮
	private EditText username, password; // 用户名，密码输入框
	private Animation shake;
	private Context mContext;
	private Toast toast;
	private String userID;
	private Switch aSwitch;
	private Boolean user_type = false;
	private TeacherInfo teacher;
	private StudentInfo student;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Bmob.initialize(this, MainActivity.ApplicationID);
        //preference 初始化
		preferences = getSharedPreferences(ProjectPreference,MODE_PRIVATE);
		mContext = this;
		//BmobUser.logOut();
		//BmobQuery.clearAllCachedResults();
		if (BmobUser.isLogin()){
			loginSuccess();
		}
		user_type = preferences.getBoolean("user_type",false);
		initView();
	}

	private void initView() {
		username = findViewById(R.id.login_username);
		password = findViewById(R.id.login_password);
		login = findViewById(R.id.login_login); // 登录按钮
		ImageView headImageView =  findViewById(R.id.user_icon);
		aSwitch = findViewById(R.id.switch1);

		final ImageView iconUser = findViewById(R.id.icon_user); // 用户名前面的小图标
		final ImageView iconPassword =  findViewById(R.id.icon_password); // 密码前面的小图标

		username.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {//用户名4,密码6长度限制
				if (s.length() > 0) {
					iconUser.setImageResource(R.mipmap.personal_icon_user_black);
					if (s.length() >= 4 && password.length() >= 6) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					iconUser.setImageResource(R.mipmap.personal_icon_user_gray);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {//用户名4,密码6长度限制
				if (s.length() > 0) {
					iconPassword.setImageResource(R.mipmap.personal_icon_password_black);
					if (s.length() >= 6 && username.length() >= 4) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					iconPassword.setImageResource(R.mipmap.personal_icon_password_gray);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b){
					aSwitch.setText("学生");
					username.setHint("学生号");
					user_type = false;
				}else {
					aSwitch.setText("教师");
					username.setHint("职工号");
					user_type = true;
				}
			}
		});

		login.setOnClickListener(this);
		shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.personal_shake);

		// 忘记密码事件
		findViewById(R.id.personal_login_forget).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//showToast("之后实现");
				Intent intent = new Intent(LoginActivity.this,PasswordForgetActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.login_login) {
			if (username.getText().toString().trim().equals("")) {
				showToast("账号不能为空");
				username.requestFocus();
				username.startAnimation(shake);
				return;
			}
			if (password.getText().toString().trim().equals("")) {
				showToast("密码不能为空");
				password.requestFocus();
				password.startAnimation(shake);
				return;
			}

			/*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();*/
			if (aSwitch.isChecked()){
				user_type = false;
			}else {
				user_type = true;
			}
			if (isNetworkConnected(this)){
				userID = username.getText().toString();
				//记录登录用户类型
				login( username.getText().toString().trim(), password.getText().toString().trim());
			}else {
				showToast("无网络连接");
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode,
									int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 120) {
			if (data != null && data.getBooleanExtra("isRegist", false)) {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void login(final String userId, final String password) {
		//TeacherInfo b = new TeacherInfo();
		//loginSuccess(b);
		BmobUser.loginByAccount(userId, password, new LogInListener<BmobUser>() {

			@Override
			public void done(BmobUser bmobUser, BmobException e) {
				if (e == null) {
					showToast("登录成功:"+bmobUser.getUsername());
					//获取preference
					initSharedPreference();

				} else {
					showToast("登录失败,请重新输入!");
				}
			}
		});
	}

	//登录成功
	private void loginSuccess() {
		//showToast("登录成功");
		//dismissDialog();
		//setResult(200);//登录成功的返回值是200

		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
		if (user_type){
			intent.putExtra("teacher",teacher);
		}else {
			intent.putExtra("student",student);
		}
		startActivity(intent);
		finish();
	}

	//sharedPreference获取
	private void initSharedPreference() {
		//教师登录
		if (user_type){
			BmobQuery<TeacherInfo> bmobQuery = new BmobQuery<>();
			bmobQuery.addWhereEqualTo("teacherId",userID);
			bmobQuery.findObjects(new FindListener<TeacherInfo>() {
				@Override
				public void done(List<TeacherInfo> list, BmobException e) {
					if (e == null){
						Log.i(TAG,"preference success:"+list.size());
						if (list.size() == 1){

							teacher = new TeacherInfo();
							teacher = list.get(0);
							editor = preferences.edit();
							editor.putString("userObjectId",list.get(0).getObjectId());
							editor.putString("userName",list.get(0).getTeacherName());
							editor.putString("userId",list.get(0).getTeacherId());
							editor.putBoolean("user_type",true);
							//提交
							editor.apply();
							Log.i(TAG,"preference:"+preferences.getString("userName",null)
									+" "+preferences.getString("userType",null));

							//界面跳转
							loginSuccess();
						}else {
							Log.e(TAG,"teacher preference fail:"+list.size());
						}
					}else {
						Log.e(TAG,"teacher preference fail:"+e.getMessage());
					}
				}
			});
		}else {
			BmobQuery<StudentInfo> bmobQuery = new BmobQuery<>();
			bmobQuery.addWhereEqualTo("studentId",userID);
			bmobQuery.include("teacher");
			bmobQuery.findObjects(new FindListener<StudentInfo>() {
				@Override
				public void done(List<StudentInfo> list, BmobException e) {
					if (e == null){
						Log.i(TAG,"preference success:"+list.size());
						if (list.size() == 1){
							student = new StudentInfo();
							student = list.get(0);
							editor = preferences.edit();
							editor.putString("userObjectId",list.get(0).getObjectId());
							editor.putString("userName",list.get(0).getStudengName());
							editor.putString("userId",list.get(0).getStudentId());
							editor.putBoolean("user_type",false);

							//提交
							editor.apply();

							Log.i(TAG,"preference:"+preferences.getString("userName",null)
									+" "+preferences.getString("userType",null));

							//界面跳转
							loginSuccess();
						}else {
							Log.e(TAG,"student preference fail:"+list.size());
						}

					}else {
						Log.e(TAG,"student preference fail:"+e.getMessage());
					}
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 直接关闭本页面
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showToast(String msg) {
		if (!TextUtils.isEmpty(msg))
			makeText(getContext(), msg);
	}

	public void makeText(Context context, CharSequence s) {
		try {
			if (null == context || null == s) {
				return;
			}
			if (toast == null) {
				toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			}
			toast.setText(s);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Context getContext() {
		return mContext;
	}

	//是否有网络
	public boolean isNetworkConnected(Context context){
		if (context != null){
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
					Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null){
				return networkInfo.isAvailable();
			}
		}
		return false;
	}
}
