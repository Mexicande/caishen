package com.Michael.AccountBook.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.glide.GlideRoundTransform;
import com.Michael.AccountBook.R;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.glide.GlideRoundTransform;

import java.util.List;

/**
 *
 * @author apple
 * @date 2017/4/11
 */

public class ProductAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {

    public ProductAdapter(List<Product> data) {
        super(R.layout.new_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Product item) {
            helper.setText(R.id.tv_ProductName,item.getName());
        helper .setText(R.id.tv_Summry,item.getProduct_introduction())
                .setText(R.id.number,item.getSort()+1000+"")
                .setText(R.id.rate,"日利率: "+item.getMin_algorithm());
        String maximumAmount = item.getMaximum_amount();
        if(maximumAmount.length()>4){
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"~"+substring+"万");
        }else {
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"~"+maximumAmount);
        }

        Glide.with(mContext).load(item.getProduct_logo())
                .bitmapTransform(new GlideRoundTransform(mContext,5))
                .into((ImageView) helper.getView(R.id.head))
        ;
    }
}
