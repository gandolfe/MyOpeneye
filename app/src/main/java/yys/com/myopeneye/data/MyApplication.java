package yys.com.myopeneye.data;

import android.app.Application;
import android.content.Context;

/**
 * Created by yangys on 2019/3/4.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
