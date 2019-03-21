package com.example.zhiyongjin.clickfood.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.bean.Order;
import com.example.zhiyongjin.clickfood.bean.Product;
import com.example.zhiyongjin.clickfood.biz.OrderBiz;
import com.example.zhiyongjin.clickfood.biz.ProductBiz;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.example.zhiyongjin.clickfood.ui.adapter.ProductListAdapter;
import com.example.zhiyongjin.clickfood.ui.view.refresh.SwipeRefresh;
import com.example.zhiyongjin.clickfood.ui.view.refresh.SwipeRefreshLayout;
import com.example.zhiyongjin.clickfood.ui.vo.ProductItem;
import com.example.zhiyongjin.clickfood.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 点餐的页面
 */


public class ProductListActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView mTvCount;
    private Button button;

    private ProductBiz productBiz = new ProductBiz();
    private OrderBiz orderBiz = new OrderBiz();
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDates = new ArrayList<>();

    private int mCurrentPage = 0;
    private float mTotalPrice;
    private int mTotalCount;
    private Order mOrder = new Order();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setUpToolbar();
        setTitle("订单");

        initView();
        initEvent();


    }

    private void initEvent() {
        swipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDates();
            }
        });

        mAdapter.setmOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量: " + mTotalCount);
                mTotalPrice += productItem.getPrice();
                button.setText(mTotalPrice + "元 立即支付");

                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量: " + mTotalCount);
                mTotalPrice -= productItem.getPrice();

                if (mTotalCount == 0) {
                    mTotalPrice =0;
                }

                button.setText(mTotalPrice + "元 立即支付");

                mOrder.removeProduct(productItem);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTotalCount <= 0) {
                    T.showToast("你还没有选择菜");
                    return;
                }
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDates.get(0).getRestaurant());

                startLoadProgress();

                orderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadProgress();
                        T.showToast("订单支付成功");

                        setResult(RESULT_OK);
                        finish();
                    }
                });

            }
        });

    }


    private void loadDates() {
        startLoadProgress();
        productBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadProgress();
                T.showToast(e.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadProgress();
                swipeRefreshLayout.setRefreshing(false);
                mCurrentPage = 0;
                mDates.clear();
                for (Product p : response) {
                    mDates.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                //清空选择的数据, 数量, 价格
                mTotalPrice = 0;
                mTotalCount = 0;


                mTvCount.setText("数量: " + mTotalCount);
                button.setText(mTotalPrice + "元 立即支付");
            }
        });

    }

    private void loadMore() {
        startLoadProgress();
        productBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                swipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadProgress();
                swipeRefreshLayout.setPullUpRefreshing(false);

                if (response.size() == 0) {
                    T.showToast("没有数据啦~");
                    return;
                } else {
                    T.showToast("又找到" + response.size() + "道菜");
                }
                for (Product p : response) {
                    mDates.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        recyclerView = findViewById(R.id.id_recycleview);
        button = findViewById(R.id.id_btn_pay);
        mTvCount = findViewById(R.id.id_tv_count);

        swipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        //个性化
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        mAdapter = new ProductListAdapter(this, mDates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productBiz.onDestroy();
    }
}
