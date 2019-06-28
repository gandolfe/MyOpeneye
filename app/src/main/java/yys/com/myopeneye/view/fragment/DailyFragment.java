package yys.com.myopeneye.view.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import yys.com.myopeneye.R;
import yys.com.myopeneye.adapter.DailyAdapter;
import yys.com.myopeneye.data.model.DailyEntity.IssueEntity.ItemListEntity;
import yys.com.myopeneye.mvp.DailyContract;
import yys.com.myopeneye.mvp.DailyPresenter;

/**
 * 每日精选
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class DailyFragment extends Fragment implements DailyContract.View{

    private Context context;
    private DailyContract.Presenter presenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NestedScrollView mNestedScrollView;
    private RecyclerView mRecyclerView;
    private DailyAdapter mDailyAdapter;
    private static DailyFragment instance;

    private TextView noDataTextView;


    private DailyFragment(Context context) {
        this.context = context;
        presenter = new DailyPresenter(context);
        presenter.setView(this);
    }

    public static DailyFragment getInstance(Context context){
        if(instance == null){
            instance = new DailyFragment(context);
        }
        return instance;
    }

    Handler handler = new Handler(){

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        initView(view);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.getDailyData();
            }
        },200);
        return view;
    }

    private void initView(View view){
        mSwipeRefreshLayout = view.findViewById(R.id.refreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        mNestedScrollView = view.findViewById(R.id.mynested_scroll_view);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                presenter.getDailyData();
            }
        });

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //滑到底加载更多
                if(scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())){
                    presenter.nextPageData();
                }
            }
        });

        noDataTextView = view.findViewById(R.id.no_data_textview);
    }

    @Override
    public void setPresenter(DailyContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateData(int lastsize,List<ItemListEntity> data) {
        if (mDailyAdapter == null){
            mDailyAdapter = new DailyAdapter(context,data);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mDailyAdapter);
        }else{
            if(lastsize>=1){
                mDailyAdapter.notifyItemInserted(lastsize);
            }else{
                mDailyAdapter.notifyDataSetChanged();
            }

        }

        noDataTextView.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        Toast.makeText(context,"网络请求错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLodingIndicator(final boolean isshow) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isshow);
            }
        });
    }
}
