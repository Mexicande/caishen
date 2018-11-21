package com.Michael.AccountBook.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.glide.GlideCircleTransform;
import com.Michael.AccountBook.R;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.glide.GlideCircleTransform;

import java.util.List;

/**
 * Created by apple on 2018/8/9.
 */

public class HotAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {

    public HotAdapter(@Nullable List<Product> data) {
        super(R.layout.hot_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.name,item.getName());
        Glide.with(mContext).load(item.getProduct_logo())
                .bitmapTransform(new GlideCircleTransform(mContext)).into((ImageView)helper.getView(R.id.logo));

    }
}
