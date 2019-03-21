package com.example.zhiyongjin.clickfood.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiyongjin.clickfood.R;
import com.example.zhiyongjin.clickfood.config.Config;
import com.example.zhiyongjin.clickfood.ui.activity.ProductDetailActivity;
import com.example.zhiyongjin.clickfood.ui.vo.ProductItem;
import com.example.zhiyongjin.clickfood.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder> {

    private Context mContext;
    private List<ProductItem> mProductItem;
    private LayoutInflater layoutInflater;

    public ProductListAdapter(Context mContext, List<ProductItem> mProductItem) {
        this.mContext = mContext;
        this.mProductItem = mProductItem;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ProductListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_product_list, parent, false);
        // TODO: 2018/12/10 ?????
        return new ProductListItemViewHolder(itemView);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull ProductListItemViewHolder holder, int position) {
        ProductItem productItem = mProductItem.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl + productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.count +"");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice() + "元/份");

    }

    @Override
    public int getItemCount() {
        return mProductItem.size();
    }

    public interface OnProductListener{
        void onProductAdd(ProductItem productItem);
        void onProductSub(ProductItem productItem);
    }

    private OnProductListener mOnProductListener;

    public void setmOnProductListener(OnProductListener onProductListener) {
        mOnProductListener = onProductListener;
    }

    class ProductListItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIvImage;
        public TextView mTvName, mTvLabel, mTvPrice;
        public ImageView mIvAdd, mIvSub;
        public TextView mTvCount;



        public ProductListItemViewHolder(View itemView) {
            super(itemView);
            //初始化

            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
            mIvAdd = itemView.findViewById(R.id.id_iv_add);
            mIvSub = itemView.findViewById(R.id.id_iv_sub);
            mTvCount = itemView.findViewById(R.id.id_tv_count);

            // TODO: 2018/12/10

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailActivity.launch(mContext, mProductItem.get(getLayoutPosition()));
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItem.get(pos);
                    productItem.count += 1;
                    mTvCount.setText(productItem.count + "");
                    //计算总价格, 回调到activity
                    if (mOnProductListener != null) {
                        mOnProductListener.onProductAdd(productItem);
                    }
                }
            });

            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItem.get(pos);

                    if (productItem.count <= 0) {
                        T.showToast("都是0了!");
                        return;
                    } else {
                        productItem.count -=1;
                        mTvCount.setText(productItem.count+"");
                        //回调
                        if (mOnProductListener != null) {
                            mOnProductListener.onProductSub(productItem);
                        }
                    }

                }
            });
        }
    }

}
