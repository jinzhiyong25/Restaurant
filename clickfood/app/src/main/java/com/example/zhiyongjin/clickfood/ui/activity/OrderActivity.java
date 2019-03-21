package com.example.zhiyongjin.clickfood.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.UserInfoHolder;
import com.example.zhiyongjin.clickfood.bean.Order;
import com.example.zhiyongjin.clickfood.bean.User;
import com.example.zhiyongjin.clickfood.biz.OrderBiz;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.example.zhiyongjin.clickfood.ui.adapter.OrderAdapter;
import com.example.zhiyongjin.clickfood.ui.view.CircleTransform;
import com.example.zhiyongjin.clickfood.ui.view.refresh.SwipeRefresh;
import com.example.zhiyongjin.clickfood.ui.view.refresh.SwipeRefreshLayout;
import com.example.zhiyongjin.clickfood.utils.T;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {
    private Button mBtnOrder;
    private TextView mTvUsername;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;

    private OrderAdapter mAdapter;
    private List<Order> mDate = new ArrayList<>();

    private OrderBiz mOrderBiz = new OrderBiz();

    //为了上拉菜单, 默认加载第0页
    private int mCurrentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();

        initEvent();
        //为了不用下拉或者上拉也能显示数据
        loadDatas();
    }

    private void initEvent() {

        //下拉
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();

            }
        });

        //上拉
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this
                        ,ProductListActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadDatas();

        }
    }

    private void loadMore() {

        startLoadProgress();

        //这里的++的意思是, 加载下一页
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadProgress();
                T.showToast(e.getMessage());
                //失败重置回去
                mCurrentPage --;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadProgress();

                if (response.size() == 0) {
                    T.showToast("没有订单了!");
                    //这个setPullUpRefreshing(false)的作用是: 把转圈的小东西取消?
                    mSwipeRefreshLayout.setPullUpRefreshing(false);
                    return;
                } else {
                    T.showToast("订单加载成功");
                    mDate.addAll(response);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setPullUpRefreshing(false);


                }
            }
        });


    }

    private void loadDatas() {
        mOrderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onSuccess(List<Order> response) {
                stopLoadProgress();
                //重置
                mCurrentPage = 0;
                T.showToast("订单更新成功");
                mDate.clear();
                mDate.addAll(response);
                mAdapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
    private void initView() {
        mBtnOrder = findViewById(R.id.id_btn_order);
        mTvUsername = findViewById(R.id.id_tv_username);
        mRecyclerView = findViewById(R.id.id_recycleview);
        mSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        mIvIcon = findViewById(R.id.id_iv_icon);

        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvUsername.setText(user.getUsername());
        } else {
            toLoginActivity();
            finish();
            return;
        }
        //控件设置
        //支持上拉下拉
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        //个性化
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        //初始化Adapter
        mAdapter = new OrderAdapter(this, mDate);

        //初始化recycleView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        //头像
        Picasso.with(this).load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIvIcon);
    }

    public void onDestroy() {
        super.onDestroy();
        mOrderBiz.onDestroy();
    }

    /**
     * 作用是: 用户返回到桌面之后, 再开启这个app, 那么保留登录状态
     *
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            } catch (Exception e) {
                //igonre
            }
        }


        return super.onKeyDown(keyCode, event);
    }
}

