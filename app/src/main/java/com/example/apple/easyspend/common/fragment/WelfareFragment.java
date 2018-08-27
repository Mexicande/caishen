package com.example.apple.easyspend.common.fragment;


import android.content.Intent;
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
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.adapter.ProductAdapter;
import com.example.apple.easyspend.bean.Product;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.common.activity.HtmlActivity;
import com.example.apple.easyspend.adapter.WelfareAdapter;
import com.example.apple.easyspend.bean.WelfareBean;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.common.activity.LoginActivity;
import com.example.apple.easyspend.utils.RecyclerViewDecoration;
import com.example.apple.easyspend.utils.ToastUtils;
import com.google.gson.Gson;

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
                    intent.putExtra("title",product.getP_name());
                    intent.putExtra("link",product.getUrl());
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(), HtmlActivity.class);
                    intent.putExtra("title",product.getP_name());
                    intent.putExtra("link",product.getUrl());
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
        title.setText("贷款大全");
        mProductAdapter = new ProductAdapter(null);
        mProductAdapter.setHeaderView(getHeader());
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewDecoration decoration = new RecyclerViewDecoration(10);
        mRecylerview.addItemDecoration(decoration);
        mRecylerview.setAdapter(mProductAdapter);

    }

    private View getHeader() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.welfare_header, null);
        ButterKnife.findById(view,R.id.help)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if("1".equals(status)&& !TextUtils.isEmpty(url)){
                            Intent intent=new Intent(getActivity(),HtmlActivity.class);
                            intent.putExtra("title","帮你借");
                            intent.putExtra("link",url);
                            startActivity(intent);
                        }else {
                            ToastUtils.showToast(getActivity(),"升级维护中...");
                        }
                    }
                });
        return view;
    }

    private void getData() {
        ApiService.GET_SERVICE(Api.LSIT, new HashMap<String, String>(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
                try {
                    String data = json.getString("data");
                    Gson gson = new Gson();
                    Product[] welfare = gson.fromJson(data, Product[].class);
                    if (welfare.length > 0) {
                        List<Product> welfare1 = Arrays.asList(welfare);
                        mProductAdapter.setNewData(welfare1);
                    }
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

        ApiService.GET_SERVICE(Api.HELP, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject object = data.getJSONObject("data");
                    status = object.getString("status");
                    url = object.getString("url");
                    name = object.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
