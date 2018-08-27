package com.example.apple.easyspend.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.apple.easyspend.R;
import com.example.apple.easyspend.adapter.CreditAdapter;
import com.example.apple.easyspend.bean.CreditBean;
import com.example.apple.easyspend.common.Api;
import com.example.apple.easyspend.common.ApiService;
import com.example.apple.easyspend.common.Contacts;
import com.example.apple.easyspend.common.OnRequestDataListener;
import com.example.apple.easyspend.common.SPUtil;
import com.example.apple.easyspend.utils.RecycleViewDivider;
import com.example.apple.easyspend.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author apple
 * 信用卡
 */
public class CreditActivity extends AppCompatActivity {

    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;
    @Bind(R.id.credit_srl)
    SwipeRefreshLayout mRefreshLayout;
    private CreditAdapter mCreditAdapter;
    private View errorView;
    private View notDataView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();

    }

    private void setListener() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        mCreditAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String token = SPUtil.getString(Contacts.TOKEN);
                CreditBean creditBean = mCreditAdapter.getData().get(position);
                if(!TextUtils.isEmpty(token)){
                    Intent intent=new Intent(CreditActivity.this, HtmlActivity.class);
                    intent.putExtra("title",creditBean.getName());
                    intent.putExtra("link",creditBean.getLink());
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(CreditActivity.this, LoginActivity.class);
                    intent.putExtra("title",creditBean.getName());
                    intent.putExtra("link",creditBean.getLink());
                    startActivity(intent);
                }
            }
        });
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initView() {
        mCreditAdapter = new CreditAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));
        mRecylerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.recycler_divider));
        mRecylerview.setAdapter(mCreditAdapter);

        notDataView =getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecylerview.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.view_error, (ViewGroup) mRecylerview.getParent(), false);

    }

    private void initData() {
        ApiService.GET_SERVICE(Api.CREDIT, null, new OnRequestDataListener() {

            @Override
            public void requestSuccess(int code, JSONObject json) {
                if(mRefreshLayout.isRefreshing()){
                    mRefreshLayout.setRefreshing(false);
                }
                try {
                    JSONArray data = json.getJSONArray("data");
                    List<CreditBean> list = new Gson().fromJson(data.toString(), new TypeToken<List<CreditBean>>() {
                    }.getType());
                    if(list.isEmpty()){
                        mCreditAdapter.setEmptyView(notDataView);
                    }
                    mCreditAdapter.setNewData(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                if(mRefreshLayout.isRefreshing()){
                    mRefreshLayout.setRefreshing(false);
                }
                mCreditAdapter.setEmptyView(errorView);
                ToastUtils.showToast(CreditActivity.this,msg);
            }
        });
    }
}
