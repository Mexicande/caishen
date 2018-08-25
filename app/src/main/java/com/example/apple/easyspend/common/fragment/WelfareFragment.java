package com.example.apple.easyspend.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.common.activity.HtmlActivity;
import com.example.apple.easyspend.adapter.WelfareAdapter;
import com.example.apple.easyspend.bean.WelfareBean;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.utils.RecyclerViewDecoration;
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
    private WelfareAdapter mWelfareAdapter;

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

        mWelfareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WelfareBean product = mWelfareAdapter.getData().get(position);
                startActivity(new Intent(getContext(), HtmlActivity.class).putExtra("html", product.getLink()));
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
        title.setText("福利");
        mWelfareAdapter = new WelfareAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewDecoration decoration = new RecyclerViewDecoration(10);
        mRecylerview.addItemDecoration(decoration);

        mRecylerview.setAdapter(mWelfareAdapter);

    }

    private void getData() {

        ApiService.GET_SERVICE(Api.WELFARE, new HashMap<String, String>(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
                try {
                    String data = json.getString("data");
                    Gson gson = new Gson();
                    WelfareBean[] welfare = gson.fromJson(data, WelfareBean[].class);
                    if (welfare.length > 0) {
                        List<WelfareBean> welfare1 = Arrays.asList(welfare);
                        mWelfareAdapter.setNewData(welfare1);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
