package com.cn.chars.freecash.common.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cn.chars.freecash.R;
import com.cn.chars.freecash.bean.Banner;
import com.cn.chars.freecash.common.Api;
import com.cn.chars.freecash.common.ApiService;
import com.cn.chars.freecash.common.OnRequestDataListener;
import com.cn.chars.freecash.common.activity.ProductActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    @Bind(R.id.banner_fresco_demo_content)
    BGABanner banner;
    @Bind(R.id.layout_loan)
    LinearLayout layoutLoan;
    @Bind(R.id.layout_lottery)
    LinearLayout layoutLottery;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        getBanner();
        return view;
    }

    private void initView() {
        banner.setAdapter(new BGABanner.Adapter<ImageView, Banner>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Banner model, int position) {
                Glide.with(getActivity())
                        .load(model.getPictrue())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
    }

    private void getBanner() {
        ApiService.GET_SERVICE(Api.BANNER, new JSONObject(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                try {
                    String data = json.getString("data");
                    Gson gson = new Gson();
                    Banner[] banners = gson.fromJson(data, Banner[].class);
                    if(banners.length>0){
                        List<Banner> banners1 = Arrays.asList(banners);
                        banner.setData(banners1, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });
    }



    @OnClick({R.id.layout_big, R.id.layout_smart, R.id.layout_card,
            R.id.apply, R.id.layout_loan, R.id.layout_lottery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_big:
                ProductActivity.launch(getActivity(),"4");
                break;
            case R.id.layout_smart:
                ProductActivity.launch(getActivity(),"9");
                break;
            case R.id.layout_card:
                ProductActivity.launch(getActivity(),"11");
                break;
            case R.id.apply:
                ProductActivity.launch(getActivity(),"7");
                break;
            case R.id.layout_loan:
                break;
            case R.id.layout_lottery:
                break;
            default:
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
