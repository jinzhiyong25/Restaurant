package com.example.zhiyongjin.clickfood.biz;

import com.example.zhiyongjin.clickfood.bean.Product;
import com.example.zhiyongjin.clickfood.config.Config;
import com.example.zhiyongjin.clickfood.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class ProductBiz {

    public void listByPage(int currentPage, CommonCallback<List<Product>> callback) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "product_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(callback);
    }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
