package com.yys.playercore;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by yangys on 2019/2/18.
 */

public class NiceUtil {

    public static String getTimeFromMill(long milliseconds){
      if(milliseconds <=0 || milliseconds>=24*60*60*1000){
          return "00:00";
      }

      long totalSeconds = milliseconds/1000;
      long seconds = totalSeconds % 60;
      long minutes = (totalSeconds/60) % 60;
      long hours = totalSeconds / 3600;
      StringBuffer mStringBuffer = new StringBuffer();
      Formatter formatter = new Formatter(mStringBuffer, Locale.getDefault());
      if(hours>0){
          return formatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
      }else{
          return formatter.format("%02d:%02d",minutes,seconds).toString();
      }

    };

    public static void hideActionBar(Context context){
        ActionBar actionBar = getAppCompatActivity(context).getSupportActionBar();
        if(actionBar!=null){
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.hide();
        }

        scanForActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
        ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void ShowActionBar(Context context){
        ActionBar actionBar = getAppCompatActivity(context).getSupportActionBar();
        if(actionBar!=null){
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.show();
        }

        scanForActivity(context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }


    public static AppCompatActivity getAppCompatActivity(Context context){
        if(context instanceof AppCompatActivity){
            return (AppCompatActivity) context;
        }else if(context instanceof ContextWrapper){
            return getAppCompatActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
