package com.example.zhiyongjin.clickfood.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zhiyongjin.clickfood.R;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnSkip;
    private Handler handler = new Handler();

    private Runnable mRunnableToLogin = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        initView();
        initEvent();

        handler.postDelayed(mRunnableToLogin, 3000);


    }

    private void initEvent() {
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个handler的方法是说:
                //如果点击的跳过这个按钮的话, 之后的那个3秒之后自动跳转的handler就不用了
                handler.removeCallbacks(mRunnableToLogin);
                toLoginActivity();
            }
        });
    }

    private void initView() {
        mBtnSkip = findViewById(R.id.bt_skip);

    }

    private void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //防止handler引起的内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mRunnableToLogin);
    }
}
