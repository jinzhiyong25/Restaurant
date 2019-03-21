package com.example.zhiyongjin.clickfood.ui.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zhiyongjin.clickfood.R;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLoadProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff000000);
        }

        mLoadProgress = new ProgressDialog(this);
        mLoadProgress.setMessage("加载中......");
    }

    protected void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected void stopLoadProgress() {
        if (mLoadProgress != null && mLoadProgress.isShowing()) {
            mLoadProgress.dismiss();
        }
    }

    protected void startLoadProgress() {
        mLoadProgress.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadProgress();
        mLoadProgress = null;
    }

    protected void toLoginActivity() {

    }
}
