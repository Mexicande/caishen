package com.example.apple.fivebuy.fragment;


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
import com.example.apple.fivebuy.R;
import com.example.apple.fivebuy.activity.HtmlActivity;
import com.example.apple.fivebuy.adapter.ProductAdapter;
import com.example.apple.fivebuy.bean.Banner;
import com.example.apple.fivebuy.bean.Product;
import com.example.apple.fivebuy.common.Api;
import com.example.apple.fivebuy.common.ApiService;
import com.example.apple.fivebuy.common.Contacts;
import com.example.apple.fivebuy.common.LoginActivity;
import com.example.apple.fivebuy.common.OnRequestDataListener;
import com.example.apple.fivebuy.common.SPUtil;
import com.example.apple.fivebuy.utils.SPUtils;
import com.example.apple.fivebuy.utils.ToastUtils;
import com.google.gson.Gson;
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
    @Bind(R.id.title)
    TextView title;
    private BGABanner banner;

    @Bind(R.id.recylerview)
    RecyclerView recylerview;
    private ProductAdapter mAdapter;
    private ArrayList<Product> mProductList = new ArrayList<>();

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
        getDate(0);
        setListener();
        return view;
    }

    private void initView() {
        title.setText("首页");
        mAdapter = new ProductAdapter(null);
        recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        recylerview.setAdapter(mAdapter);
        View footerView = setView();
        mAdapter.addHeaderView(footerView, 0);
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

    private void getDate(final int limit) {
        /**product_list**/
        Map<String, String> map = new HashMap<>();
        map.put("offset", String.valueOf(limit));
        map.put("number", "10");

        ApiService.GET_SERVICE(Api.PRODUCT_LSIT, map, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                try {
                    String data = json.getString("data");
                    Gson gson = new Gson();
                    Product[] banners = gson.fromJson(data, Product[].class);
                    if (limit == 0) {
                        mProductList.clear();
                        mAdapter.getData().clear();
                    }
                    if(banners.length>0){
                        List<Product> products = Arrays.asList(banners);
                        mProductList.addAll(products);
                        mAdapter.addData(products);
                    }
                    if (mRefreshLayout.isRefreshing()) {
                        mRefreshLayout.finishRefresh();
                    }
                    if (mRefreshLayout.isLoading()) {
                        mRefreshLayout.finishLoadmore();
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
                if (mRefreshLayout.isLoading()) {
                    mRefreshLayout.finishLoadmore();
                }
            }
        });
    }

    private void getBanner() {
        /**banner**/
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
        });
    }

    /**
     * RecyclerView   header
     *
     * @return
     */
    private View setView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.header_layout, null);
        banner = (BGABanner) view.findViewById(R.id.banner_fresco_demo_content);
        return view;

    }

    private void setListener() {
        banner.setDelegate(new BGABanner.Delegate<ImageView, Banner>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, Banner model, int position) {
                String token = SPUtil.getString(getActivity(), Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    startActivity(new Intent(getContext(), LoginActivity.class).putExtra("html", model.getApp()));
                }else {
                    startActivity(new Intent(getContext(), HtmlActivity.class).putExtra("html", model.getApp()));
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Product product = mAdapter.getData().get(position);
                String token = SPUtil.getString(getActivity(), Contacts.TOKEN);
                    if (view.getId() == R.id.go) {
                        if(TextUtils.isEmpty(token)){
                            startActivity(new Intent(getContext(), LoginActivity.class).putExtra("html", product.getUrl()));
                        }else {
                            startActivity(new Intent(getContext(), HtmlActivity.class).putExtra("html", product.getUrl()));
                        }
                    }
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBanner();
                getDate(0);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getDate(mProductList.size());
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
