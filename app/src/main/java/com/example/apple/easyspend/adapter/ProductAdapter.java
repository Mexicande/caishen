package com.example.apple.easyspend.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.bean.Product;
import com.example.apple.easyspend.glide.GlideRoundTransform;

import java.util.List;

/**
 *
 * @author apple
 * @date 2017/4/11
 */

public class ProductAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {

    public ProductAdapter(List<Product> data) {
        super(R.layout.product_item, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_name,item.getP_name())
                .setText(R.id.tv_desc,item.getP_desc())
                .setText(R.id.average_time_Special,"放款速度: "+item.getFastest_time());

        int interestAlgorithm = item.getInterest_algorithm();
        if(interestAlgorithm==0){
            helper.setText(R.id.special_rate,"日利率: "+item.getMin_algorithm()+"%");
        }else {
            helper.setText(R.id.special_rate,"月利率: "+item.getMin_algorithm()+"%");
        }
        String maximumAmount = item.getMaximum_amount();
        if(maximumAmount.length()>4){
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"~"+substring+"万");
        }else {
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"~"+maximumAmount);
        }

        Glide.with(mContext).load(item.getP_logo()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new GlideRoundTransform(mContext,5))
                .into((ImageView) helper.getView(R.id.iv_logo));
    }
}
