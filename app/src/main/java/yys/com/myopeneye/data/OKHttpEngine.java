package yys.com.myopeneye.data;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangys on 2019/3/4.
 */

public class OKHttpEngine {
    private static OKHttpEngine mInstance;
    private OkHttpClient mOKHttpClient;
    public static OKHttpEngine getmInstance(Context context){
        if(mInstance == null){
            synchronized (OKHttpEngine.class){
                if(mInstance == null){
                    mInstance = new OKHttpEngine(context);
                }
            }
        }
        return mInstance;
    }

    private OKHttpEngine(Context context){

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        File sdcachedir = context.getApplicationContext().getExternalCacheDir();
        int cachesize = 10 * 1024 * 1024;
        builder.cache(new Cache(sdcachedir,cachesize));
        mOKHttpClient = builder.build();
//        mOKHttpClient = new OkHttpClient();
    }

    /**
     * 异步get请求
     * @param url
     * @param resultCallBack
     */
    public void getDataSynce(@NonNull String url, final ResultCallBack resultCallBack){
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                        if(resultCallBack!=null){
                            resultCallBack.onError(request,e);
                        }

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                        if(resultCallBack!=null){
                            resultCallBack.onResponse(response);
                        }

            }
        });
    }



}
