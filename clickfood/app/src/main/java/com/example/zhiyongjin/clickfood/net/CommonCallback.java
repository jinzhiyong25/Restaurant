package com.example.zhiyongjin.clickfood.net;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;
import com.example.zhiyongjin.clickfood.utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class CommonCallback<T> extends StringCallback {


    //获取type(T)
    Type mType;

    public CommonCallback(){
        Class<? extends CommonCallback> aClass = getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Miss Type Params");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        mType = parameterizedType.getActualTypeArguments()[0];
    }
    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");
            if (resultCode == 1) {
                //成功
//                String data = resp.getString("data");
//                Gson gson = new Gson();
//                onSuccess((T) gson.fromJson(data, mType));
                onSuccess((T) GsonUtil.getGson().fromJson(resp.getString("data"), mType));
            } else {
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onError(Exception e);
    public abstract void onSuccess(T response);
}
