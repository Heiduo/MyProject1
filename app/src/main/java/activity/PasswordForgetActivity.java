package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myproject1.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordForgetActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String TAG = "SingIn";
    private TextView tv_authority_send;
    private EditText et_signin_username,et_singin_password,
            et_signin_password2,et_signin_phone,et_signin_authority;
    private Button bt_signin_submit;
    private Boolean sendMessage;

    private String usersignname ;
    private String signpassword ;
    private String signpasswordconfirm ;
    private String signin_phone ;
    private String signin_authority ;
    //Resident newResident = new Resident();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget);

        initView();
        sendMessage = false;
    }

    private void initView() {
        et_signin_username = (EditText) findViewById(R.id.et_signin_username);
        et_singin_password = (EditText) findViewById(R.id.et_singin_password);
        et_signin_password2 = (EditText) findViewById(R.id.et_signin_password2);
        et_signin_phone = (EditText) findViewById(R.id.et_signin_phone);
        et_signin_authority = (EditText) findViewById(R.id.et_signin_authority);
        tv_authority_send = (TextView) findViewById(R.id.tv_authority_send);
        bt_signin_submit = (Button) findViewById(R.id.bt_signin_submit);

        tv_authority_send.setOnClickListener(this);
        bt_signin_submit.setOnClickListener(this);

        //当任意一项输入为空时 无法点击确定
        et_signin_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    bt_signin_submit.setEnabled(true);
                } else {
                    bt_signin_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_singin_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    bt_signin_submit.setEnabled(true);
                } else {
                    bt_signin_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_signin_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    bt_signin_submit.setEnabled(true);
                } else {
                    bt_signin_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //手机号11位
        et_signin_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    bt_signin_submit.setEnabled(true);
                    tv_authority_send.setClickable(true);
                } else {
                    bt_signin_submit.setEnabled(false);
                    tv_authority_send.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_signin_authority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    bt_signin_submit.setEnabled(true);
                } else {
                    bt_signin_submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        usersignname = et_signin_username.getText().toString();
        signpassword = et_singin_password.getText().toString();
        signpasswordconfirm = et_signin_password2.getText().toString();
        signin_phone = et_signin_phone.getText().toString();
        signin_authority = et_signin_authority.getText().toString();

        switch (v.getId()){
            case R.id.bt_signin_submit:
                //toLogin();//测试用
                if (! signpasswordconfirm.equals(signpassword)){
                    Toast.makeText(PasswordForgetActivity.this, "两次填写密码不一致", Toast.LENGTH_SHORT).show();
                    et_singin_password.requestFocus();
                    return;
                } else {
                    //重置密码
                    BmobUser.resetPasswordBySMSCode(signin_authority, signpassword, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                showToast("重置成功");
                            } else {
                                showToast("重置失败：" + e.getErrorCode() + "-" + e.getMessage());
                            }
                        }
                    });
                }
                break;
            case R.id.tv_authority_send:
                if (!sendMessage){
                    //发送验证码
                    if (signin_phone.length() != 11){
                        showToast("请输入11位手机号码");
                        return;
                    }else {
                        BmobSMS.requestSMSCode(signin_phone, "project", new QueryListener<Integer>() {
                            @Override
                            public void done(Integer smsId, BmobException e) {
                                if (e == null) {
                                    showToast("发送验证码成功，短信ID：" + smsId + "\n");
                                } else {
                                    showToast("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                                }
                            }
                        });
                        sendMessage = true;
                    }
                }else {
                    tv_authority_send.setClickable(false);
                    showToast("验证码已发送，请稍等！");
                }

                break;
            default:
                break;
        }
    }

    private void toLogin() {
        finish();
    }

    public void showToast(String msg) {

        if (msg == null || msg.equals("")) {
            Log.e("login","无toast信息");
        } else {
            Toast toast = Toast.makeText(PasswordForgetActivity.this, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
