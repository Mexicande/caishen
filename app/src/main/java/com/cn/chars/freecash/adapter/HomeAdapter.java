package com.cn.chars.freecash.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.chars.freecash.bean.Product;
import com.cn.chars.freecash.glide.GlideRoundTransform;
import com.cn.chars.freecash.R;
import com.cn.chars.freecash.bean.Product;
import com.cn.chars.freecash.glide.GlideRoundTransform;

import java.util.List;

/**
 *
 * @author apple
 * @date 2017/4/11
 */

public class HomeAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {

    public HomeAdapter(List<Product> data) {
        super(R.layout.loan_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Product item) {
            helper.setText(R.id.name,item.getName());
        helper .setText(R.id.desc,item.getProduct_introduction())
                .setText(R.id.apply,item.getSort()+1000+"申请")
                .setText(R.id.rate,"日利率: "+item.getMin_algorithm());
        String maximumAmount = item.getMaximum_amount();
        if(maximumAmount.length()>4){
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            helper.setText(R.id.money,item.getMinimum_amount()+"~"+substring+"万");
        }else {
            helper.setText(R.id.money,item.getMinimum_amount()+"~"+maximumAmount);
        }

        Glide.with(mContext).load(item.getProduct_logo())
                .bitmapTransform(new GlideRoundTransform(mContext,10))
                .into((ImageView) helper.getView(R.id.iv_logo))
        ;
    }
}
