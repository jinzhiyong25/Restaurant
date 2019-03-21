package com.example.zhiyongjin.clickfood.biz;

import com.example.zhiyongjin.clickfood.bean.User;
import com.example.zhiyongjin.clickfood.config.Config;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.OkHttpClient;

public class UserBiz {


    public void login(String username, String password, CommonCallback<User> commonCallback) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    public void register(String username, String password, CommonCallback<User> commonCallback){
        OkHttpUtils.post()
                .url(Config.baseUrl + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);

    }

    //使用.tag(this)
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
