package yys.com.myopeneye.mvp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import yys.com.myopeneye.data.API;
import yys.com.myopeneye.data.OKHttpEngine;
import yys.com.myopeneye.data.ResultCallBack;
import yys.com.myopeneye.data.model.DailyEntity;
import yys.com.myopeneye.data.model.DailyEntity.IssueEntity.ItemListEntity;

/**
 * Created by yangys on 2019/3/5.
 */

public class DailyPresenter implements DailyContract.Presenter {

    private DailyContract.View view;
    private OKHttpEngine mOKOkHttpEngine;
    private String nextPageUrl = API.DAILY; //下一页请求的URL
    private List<ItemListEntity> mItemEntityList;
    private static final String TAG = "DailyPresenter";

    public DailyPresenter(Context context){
        mOKOkHttpEngine = OKHttpEngine.getmInstance(context.getApplicationContext());
        mItemEntityList = new ArrayList<ItemListEntity>();
    }

    @Override
    public void setView(DailyContract.View view) {
        this.view = view;
    }

    @Override
    public void getDailyData() {
        mItemEntityList.clear();
        mOKOkHttpEngine.getDataSynce(API.DAILY,resultCallBack);
    }

    @Override
    public void nextPageData() {
        mOKOkHttpEngine.getDataSynce(nextPageUrl,resultCallBack);
    }

    private Handler handler = new Handler();

    private ResultCallBack resultCallBack = new ResultCallBack() {

        Gson gson;
        @Override
        public void onError(Request request, Exception e) {
            view.onError();
            view.setLodingIndicator(false);
        }

        @Override
        public void onResponse(Response response) {
            Log.i(TAG,"response:"+response.toString() +response.message());
            Log.i(TAG,"response message:"+response.message().toString());

            if(gson == null) gson = new Gson();
            DailyEntity mDailyEntity = null;
            try {
                mDailyEntity = gson.fromJson(response.body().string(),DailyEntity.class);
                Log.i(TAG,"response body:"+response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if(mDailyEntity == null){
                Log.i(TAG,"mDailyEntity is null");
                return;
            }
            nextPageUrl = mDailyEntity.getNextPageUrl();
            if(mDailyEntity.getIssueList()!=null && mDailyEntity.getIssueList().size()>=1){
                mItemEntityList.addAll(mDailyEntity.getIssueList().get(0).getItemList());
            }


            handler.post(new Runnable() {
                @Override
                public void run() {
                    view.updateData(mItemEntityList);
                    view.setLodingIndicator(false);
                }
            });

        }
    };
}
