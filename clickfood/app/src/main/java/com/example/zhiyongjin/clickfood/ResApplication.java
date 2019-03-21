package com.example.zhiyongjin.clickfood;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.example.zhiyongjin.clickfood.utils.SPUtils;
import com.example.zhiyongjin.clickfood.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class ResApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        T.init(this);
        SPUtils.init(this, "sp_user.pref");
        CookieJarImpl cookieJar1 = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar1)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
