package com.Michael.AccountBook.common.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.utils.BrowsingHistory;
import com.Michael.AccountBook.utils.RecyclerViewDecoration;
import com.Michael.AccountBook.utils.ToastUtils;
import com.Michael.AccountBook.R;
import com.Michael.AccountBook.adapter.ProductAdapter;
import com.Michael.AccountBook.bean.Product;
import com.Michael.AccountBook.common.Contacts;
import com.Michael.AccountBook.common.SPUtil;
import com.Michael.AccountBook.common.activity.HtmlActivity;
import com.Michael.AccountBook.adapter.WelfareAdapter;
import com.Michael.AccountBook.bean.WelfareBean;
import com.Michael.AccountBook.common.Api;
import com.Michael.AccountBook.common.ApiService;
import com.Michael.AccountBook.common.OnRequestDataListener;
import com.Michael.AccountBook.common.activity.LoginActivity;
import com.Michael.AccountBook.utils.BrowsingHistory;
import com.Michael.AccountBook.utils.RecyclerViewDecoration;
import com.Michael.AccountBook.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author apple
 *         iv_welfare_select
 */
public class WelfareFragment extends Fragment {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;
    @Bind(R.id.Swip)
    SwipeRefreshLayout mSwip;
    private ProductAdapter mProductAdapter;
    private String status;
    private String url;
    private String name;
    public WelfareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welfare, container, false);
        ButterKnife.bind(this, view);
        initView();
        getData();
        setListener();
        return view;
    }

    private void setListener() {

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Product product = mProductAdapter.getData().get(position);
                String token = SPUtil.getString( Contacts.TOKEN);
                if(TextUtils.isEmpty(token)){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("title",product.getName());
                    intent.putExtra("link",product.getLink());
                    intent.putExtra("id",product.getId());
                    startActivity(intent);
                }else {
                    new BrowsingHistory().execute(product.getId());
                    /*Uri uri = Uri.parse(product.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
                    Intent intent=new Intent(getActivity(), HtmlActivity.class);
                    intent.putExtra("title",product.getName());
                    intent.putExtra("link",product.getLink());
                    startActivity(intent);
                }
            }
        });
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void initView() {
        title.setText("贷款");
        mProductAdapter = new ProductAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewDecoration decoration = new RecyclerViewDecoration(10);
        mRecylerview.addItemDecoration(decoration);
        mRecylerview.setAdapter(mProductAdapter);

    }


    private void getData() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("type","8");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiService.GET_SERVICE(Api.URL, jsonObject, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
                try {
                    String data = json.getString("data");

                    List<Product> mRecommendList = new Gson().fromJson(data, new TypeToken<List<Product>>() {
                    }.getType());
                        mProductAdapter.setNewData(mRecommendList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
