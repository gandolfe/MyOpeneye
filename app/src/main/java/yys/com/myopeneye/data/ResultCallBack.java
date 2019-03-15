package yys.com.myopeneye.data;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangys on 2019/3/4.
 */

public interface ResultCallBack {

    public void onError(Request request,Exception e);

    public void onResponse(Response response);

}
