package com.example.expense.DoubleFlower.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.expense.DoubleFlower.R;
import com.example.expense.DoubleFlower.bean.Product;
import com.example.expense.DoubleFlower.glide.GlideRoundTransform;

import java.util.List;

/**
 *
 * @author apple
 * @date 2017/4/11
 */

public class HomeAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {

    public HomeAdapter(List<Product> data) {
        super(R.layout.new_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Product item) {
            helper.setText(R.id.tv_ProductName,item.getName());
        helper .setText(R.id.tv_Summry,item.getProduct_introduction())
                .setText(R.id.number,item.getSort()+1000+"")
                .setVisible(R.id.iv_label, true)
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
