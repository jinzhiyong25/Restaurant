package com.example.zhiyongjin.clickfood;

import android.text.TextUtils;

import com.example.zhiyongjin.clickfood.bean.User;
import com.example.zhiyongjin.clickfood.utils.SPUtils;

import org.w3c.dom.Text;

public class UserInfoHolder {

    private static UserInfoHolder mInstance = new UserInfoHolder();

    private User mUser;

    //持久化
    private static final String KEY_USERNAME = "key_username";


    public static UserInfoHolder getInstance(){
        return mInstance;

    }

    //对外提供的方法:
    public void setUser(User user){
        mUser = user;

        if (user != null) {
            SPUtils.getInstance().put(KEY_USERNAME, user.getUsername());
        }
    }

    public User getUser(){
        User u = mUser;
        if (u == null) {
            String name = (String) SPUtils.getInstance().get("KEY_USERNAME", "");
            if (!TextUtils.isEmpty(name)) {
                u = new User();
                u.setUsername(name);
            }
        }
        mUser = u;
        return u;
    }

}



