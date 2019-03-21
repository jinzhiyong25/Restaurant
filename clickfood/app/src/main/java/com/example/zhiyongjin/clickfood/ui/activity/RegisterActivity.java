package com.example.zhiyongjin.clickfood.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.bean.User;
import com.example.zhiyongjin.clickfood.biz.UserBiz;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.example.zhiyongjin.clickfood.utils.T;
//import android.widget.Toolbar;
//import android.widget.Toolbar;

public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;

    private UserBiz mUserBiz = new UserBiz();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpToolbar();
        initView();
        initEvent();
        setTitle("注册");
    }

    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String rePassword = mEtRePassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    T.showToast("用户名, 密码, 重复输入的密码不能为空");
                    return;
                }
                if (!password.equals(rePassword)) {
                    T.showToast("两次输入的密码不一致");
                    return;
                }

                startLoadProgress();
// TODO: 2018/12/9 ???
                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        stopLoadProgress();
                        T.showToast("注册成功, 用户名为: " + response.getUsername());

                        //吧注册成功的用户名密码, 携带着一起到登录页面
                        LoginActivity.launch(RegisterActivity.this,
                                response.getUsername(), response.getPassword());
                        finish();
                    }

                    // TODO: 2018/12/9 ????
//                    public void onSucess(User response) {
//                    }
                });
            }
        });
    }

    private void initView() {
        mEtUsername = findViewById(R.id.id_et_username);
        mEtPassword = findViewById(R.id.id_et_password);
        mEtRePassword = findViewById(R.id.id_et_repassword);
        mBtnRegister = findViewById(R.id.id_btn_register);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestory();
    }
}
