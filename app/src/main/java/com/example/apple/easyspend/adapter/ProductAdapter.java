package com.example.apple.easyspend.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apple
 * @date 2017/4/11
 */

public class ProductAdapter extends BaseQuickAdapter<Product,BaseViewHolder> {
    private ArrayList<String>list;

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ProductAdapter(List<Product> data) {
        super(R.layout.product_item, data);
        list=new ArrayList<>();
        list.add("2123");
        list.add("3204");
        list.add("10394");
        list.add("4154");
        list.add("12982");
        list.add("8732");
        list.add("6340");
        list.add("7893");
        list.add("3092");
        list.add("4857");
        list.add("36281");
        list.add("8981");
        list.add("12135");
        list.add("9764");

    }
    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_name,item.getP_name())
                .setText(R.id.tv_desc,item.getP_desc())
                .setText(R.id.average_time_Special,"放款速度: "+item.getFastest_time())
                .addOnClickListener(R.id.go);

        int interestAlgorithm = item.getInterest_algorithm();
        if(interestAlgorithm==0){
            helper.setText(R.id.special_rate,"参考日利率: "+item.getMin_algorithm()+"%");
        }else {
            helper.setText(R.id.special_rate,"参考月利率: "+item.getMin_algorithm()+"%");
        }
        helper.setText(R.id.tv_people,item.getApply());
        String maximumAmount = item.getMaximum_amount();
        if(maximumAmount.length()>4){
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"-"+substring+"万");
        }else {
            helper.setText(R.id.min_max_Special,item.getMinimum_amount()+"-"+maximumAmount);
        }

        Glide.with(mContext).load(item.getP_logo()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) helper.getView(R.id.iv_logo));
    }
}
