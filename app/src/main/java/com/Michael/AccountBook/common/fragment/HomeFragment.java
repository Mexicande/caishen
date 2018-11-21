package com.Michael.AccountBook.freecash.common.fragment;


import android.content.Intent;
import android.net.Uri;
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
import com.Michael.AccountBook.freecash.adapter.HomeAdapter;
import com.Michael.AccountBook.freecash.adapter.HotAdapter;
import com.Michael.AccountBook.freecash.bean.Banner;
import com.Michael.AccountBook.freecash.bean.Product;
import com.Michael.AccountBook.freecash.utils.BrowsingHistory;
import com.Michael.AccountBook.freecash.utils.RecycleViewDivider;
import com.Michael.AccountBook.freecash.utils.SpacesItemDecoration;
import com.Michael.AccountBook.freecash.utils.ToastUtils;
import com.Michael.AccountBook.freecash.R;
import com.Michael.AccountBook.freecash.adapter.HomeAdapter;
import com.Michael.AccountBook.freecash.adapter.HotAdapter;
import com.Michael.AccountBook.freecash.common.ActivityUtils;
import com.Michael.AccountBook.freecash.common.activity.CreditActivity;
import com.Michael.AccountBook.freecash.common.activity.HomeActivity;
import com.Michael.AccountBook.freecash.common.activity.HtmlActivity;
import com.Michael.AccountBook.freecash.adapter.ProductAdapter;
import com.Michael.AccountBook.freecash.bean.Banner;
import com.Michael.AccountBook.freecash.bean.Product;
import com.Michael.AccountBook.freecash.common.Api;
import com.Michael.AccountBook.freecash.common.ApiService;
import com.Michael.AccountBook.freecash.common.Contacts;
import com.Michael.AccountBook.freecash.common.activity.LoginActivity;
import com.Michael.AccountBook.freecash.common.OnRequestDataListener;
import com.Michael.AccountBook.freecash.common.SPUtil;
import com.Michael.AccountBook.freecash.common.activity.ProductActivity;
import com.Michael.AccountBook.freecash.utils.BrowsingHistory;
import com.Michael.AccountBook.freecash.utils.RecycleViewDivider;
import com.Michael.AccountBook.freecash.utils.SpacesItemDecoration;
import com.Michael.AccountBook.freecash.utils.ToastUtils;
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
    private HomeAdapter mAdapter;
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
        mAdapter = new HomeAdapter(null);
        mHotAdapter=new HotAdapter(null);

        recylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylerview.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.recycler_divider));

        recylerview.setAdapter(mAdapter);
        View footerView = setView();
        mAdapter.addHeaderView(footerView, 0);
        View foot = getActivity().getLayoutInflater().inflate(R.layout.foot_item, null);
        ButterKnife.findById(foot,R.id.tv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.navigationController.setSelect(1);
            }
        });
        mAdapter.addFooterView(foot);
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

                    if(mRecommendList.size()>8){
                        mHotAdapter.setNewData(mRecommendList.subList(0,8));
                    }else {
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
                ProductActivity.launch(getActivity(),"11");
            }
        });


        ButterKnife.findById(view,R.id.layout_spped).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"4");
            }
        });
        ButterKnife.findById(view,R.id.layout_capacity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"7");
            }
        });
        ButterKnife.findById(view,R.id.layout_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.launch(getActivity(),"9");
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
                   /* Uri uri = Uri.parse(model.getApp());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
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
                   /* Uri uri = Uri.parse(product.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
                }
            }
        });

        mHotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Product product = mHotAdapter.getData().get(position);
                String token = SPUtil.getString( Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("title",product.getName());
                    intent.putExtra("link",product.getLink());
                    intent.putExtra("id",product.getId());
                    startActivity(intent);
                }else {
                    new BrowsingHistory().execute(product.getId());

                    Uri uri = Uri.parse(product.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
