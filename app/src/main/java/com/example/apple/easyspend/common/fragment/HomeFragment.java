package com.example.apple.easyspend.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.adapter.HotAdapter;
import com.example.apple.easyspend.common.ActivityUtils;
import com.example.apple.easyspend.common.activity.CreditActivity;
import com.example.apple.easyspend.common.activity.HtmlActivity;
import com.example.apple.easyspend.adapter.ProductAdapter;
import com.example.apple.easyspend.bean.Banner;
import com.example.apple.easyspend.bean.Product;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.activity.LoginActivity;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.common.activity.ProductActivity;
import com.example.apple.easyspend.utils.BrowsingHistory;
import com.example.apple.easyspend.utils.RecycleViewDivider;
import com.example.apple.easyspend.utils.SpacesItemDecoration;
import com.example.apple.easyspend.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 主页
 * <p>
 * A simple {@link Fragment} subclass.
 *
 * @author apple
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    private BGABanner banner;
    @Bind(R.id.recylerview)
    RecyclerView recylerview;
    private ProductAdapter mAdapter;
    private HotAdapter mHotAdapter;

    private ArrayList<Product> mProductList = new ArrayList<>();
    private Product mRecommendProduct;
    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        getBanner();
        getDate();
        setListener();
        return view;
    }

    private void initView() {
        mAdapter = new ProductAdapter(null);
        mHotAdapter=new HotAdapter(null);

        recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerview.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.recycler_divider));

        recylerview.setAdapter(mAdapter);
        View footerView = setView();
        mAdapter.addHeaderView(footerView, 0);
        mHotRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mHotRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        mHotRecyclerView.setAdapter(mHotAdapter);
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

    private void getDate() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("type","8");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**product_list**/
        ApiService.GET_SERVICE(Api.URL, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.finishRefresh();
                }
                try {
                    String data = json.getString("data");
                    List<Product> mRecommendList = new Gson().fromJson(data, new TypeToken<List<Product>>() {
                    }.getType());
                    if(mRecommendList.size()>10){
                        mAdapter.setNewData(mRecommendList.subList(0,10));
                    }else {
                        mAdapter.setNewData(mRecommendList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void requestFailure(int code, String msg) {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.finishRefresh();
                }
                ToastUtils.showToast(getActivity(),msg);
            }
        });

        JSONObject object =new JSONObject();
        try {
            object.put("type","7");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.GET_SERVICE(Api.URL, object , new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                try {
                    String data = json.getString("data");
                    List<Product> mRecommendList = new Gson().fromJson(data, new TypeToken<List<Product>>() {
                    }.getType());
                    if(!mRecommendList.isEmpty()){
                        mHotAdapter.setNewData(mRecommendList);
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

    private void getBanner() {
      /*  *//**banner**//*
        ApiService.GET_SERVICE(Api.BANNER, new HashMap<String, String>(), new OnRequestDataListener() {
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
        });*/
    }

    /**
     * RecyclerView   header
     *
     * @return
     */
    private RecyclerView mHotRecyclerView;


    private View setView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.header_layout, null);
        mHotRecyclerView=view.findViewById(R.id.hot_recycler);

        banner = (BGABanner) view.findViewById(R.id.banner_fresco_demo_content);
        ButterKnife.findById(view,R.id.layout_credit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(CreditActivity.class);
            }
        });
        ButterKnife.findById(view,R.id.layout_spped).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"1");
            }
        });
        ButterKnife.findById(view,R.id.layout_capacity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"3");
            }
        });
        ButterKnife.findById(view,R.id.layout_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"4");
            }
        });

        return view;

    }

    private void setListener() {
        banner.setDelegate(new BGABanner.Delegate<ImageView, Banner>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, Banner model, int position) {
                String token = SPUtil.getString( Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("title",model.getAdvername());
                    intent.putExtra("link",model.getApp());
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(), HtmlActivity.class);
                    intent.putExtra("title",model.getAdvername());
                    intent.putExtra("link",model.getApp());
                    startActivity(intent);
                }
            }
        });


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Product product = mAdapter.getData().get(position);
                String token = SPUtil.getString( Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("title",product.getName());
                    intent.putExtra("link",product.getLink());
                    intent.putExtra("id",product.getId());
                    startActivity(intent);
                }else {
                    new BrowsingHistory().execute(product.getId());
                    Intent intent=new Intent(getActivity(), HtmlActivity.class);
                    intent.putExtra("title",product.getName());
                    intent.putExtra("link",product.getLink());
                    startActivity(intent);
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBanner();
                getDate();
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
