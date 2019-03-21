package com.example.zhiyongjin.clickfood.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.bean.Order;
import com.example.zhiyongjin.clickfood.config.Config;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    private List<Order> mDate;
    private Context mContext;
    private LayoutInflater mInflater;

    //构造方法
    public OrderAdapter(Context mContext, List<Order> mDate) {
        this.mDate = mDate;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_order_list, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order = mDate.get(position);

        Picasso.with(mContext)
                .load(Config.baseUrl+order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        if (order.getPs().size() > 0) {
            holder.mTvLabel.setText(order.getPs().get(0).product.getName() + "等" + order.getCount() + "件商品");
        } else {
            holder.mTvLabel.setText("无消费");
        }

        holder.mTvName.setText(order.getRestaurant().getName());
        holder.mTvPrice.setText("共消费: "+order.getPrice()+"元");

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvImage;
        public TextView mTvName, mTvLabel, mTvPrice;


        public OrderItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 2018/12/9
                }
            });
            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
        }
    }
}

