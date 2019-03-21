package com.example.zhiyongjin.clickfood.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.UserInfoHolder;
import com.example.zhiyongjin.clickfood.bean.User;
import com.example.zhiyongjin.clickfood.biz.UserBiz;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.example.zhiyongjin.clickfood.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

//glide retrofit




public class LoginActivity extends BaseActivity {
    private UserBiz mUserBiz = new UserBiz();
    private EditText mEtUsername, mEtPassword;
    private Button mBbnLogin;
    private TextView mTvRegister;

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    //清空coocies
    @Override
    protected void onResume() {
        super.onResume();
//        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient();
        CookieJarImpl cookieJar1 = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar1.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initView();
        
        initEvent();

        initIntent(getIntent());
    }

    private void initEvent() {
        mBbnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/12/5 登录成功?
                String username =mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                //判断输入情况
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("账号或者密码不能为空!");

                    //单独写一个return, 是为了阻止进程往下走
                    return;
                }

                startLoadProgress();


                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadProgress();
                        T.showToast(e.getMessage());

                    }
//                    public void onSucess(User response) {
//
//                    }
                    @Override
                    public void onSuccess(User response) {
                        stopLoadProgress();
                        T.showToast("登录成功");

                        //保存用户信息
                        UserInfoHolder.getInstance().setUser(response);
                        toOrderActivity();
                    }
                });
            }
        });

        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }

        mEtUsername.setText(username);
        mEtPassword.setText(password);
    }

    private void toOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();

    }

    private void initView() {
        mEtPassword = findViewById(R.id.id_ed_password);
        mEtUsername = findViewById(R.id.id_ed_username);
        mBbnLogin = findViewById(R.id.id_btn_login);
        mTvRegister = findViewById(R.id.id_tv_register);
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);

        //下面这一行, 只有这一行
        //是为了让注册成功之后返回login页面的时候, 不会多出来一个login页面
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}
