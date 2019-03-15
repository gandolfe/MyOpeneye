package com.yys.playercore;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangys on 2019/2/12.
 * 控制器的抽象类
 */

public abstract class BaseVideoPlayerController extends FrameLayout implements View.OnTouchListener{


    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;


    protected Context mContext;
    protected INiceVideoPlayer mNiceVideoPlayer;

    public BaseVideoPlayerController(@NonNull Context context) {
        super(context);
        mContext = context;
        this.setOnTouchListener(this);
    }

    public void setNiceVideoPlayer(INiceVideoPlayer niceVideoPlayer){
        mNiceVideoPlayer = niceVideoPlayer;
    }

    /**
     * 设置播放的视频的标题
     *
     * @param title 视频标题
     */
    public abstract void setTitle(String title);

    /**
     * 视频底图
     *
     * @param resId 视频底图资源
     */
    public abstract void setImage(@DrawableRes int resId);

    /**
     * 视频底图ImageView控件，提供给外部用图片加载工具来加载网络图片
     *
     * @return 底图ImageView
     */
    public abstract ImageView imageView();

    /**
     * 设置总时长.
     */
    public abstract void setLenght(long length);

    /**
     * 当播放器的播放状态发生变化，在此方法中国你更新不同的播放状态的UI
     *
     * @param playState 播放状态：
     */
    protected abstract void onPlayStateChanged(int playState);

    /**
     * 当播放器的播放模式发生变化，在此方法中更新不同模式下的控制器界面。
     *
     * @param playMode 播放器的模式：普通模式、全屏模式、小屏模式
     */
    protected abstract void onPlayModeChanged(int playMode);

    /**
     * 重置控制器，将控制器恢复到初始状态。
     */
    protected abstract void reset();

    /**
     * 手势左右滑动改变播放位置时，显示控制器中间的播放位置变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param duration            视频总时长ms
     * @param newPositionProgress 新的位置进度，取值0到100。
     */
    protected abstract void showChangePosition(long duration, int newPositionProgress);

    /**
     * 手势左右滑动改变播放位置后，手势up或者cancel时，隐藏控制器中间的播放位置变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract void hideChangePosition();


    /**
     * 开始更新进度条的计时器
     */
    protected void startUpdateProgressTimer(){
        cancelUpdateProgressTimer();
        if(mUpdateProgressTimer == null){
            mUpdateProgressTimer = new Timer();
        }
        if(mUpdateProgressTimerTask == null){
            mUpdateProgressTimerTask = new TimerTask() {
                @Override
                public void run() {
                    BaseVideoPlayerController.this.post(new Runnable() {
                        @Override
                        public void run() {
                            updateProgress();
                        }
                    });
                }
            };
        }
        mUpdateProgressTimer.schedule(mUpdateProgressTimerTask,0,1000);
    }

    /**
     * 取消更新进度条的计时器
     */
    protected void cancelUpdateProgressTimer(){
        if(mUpdateProgressTimer!=null){
            mUpdateProgressTimer.cancel();
            mUpdateProgressTimer = null;
        }
        if(mUpdateProgressTimerTask!=null){
            mUpdateProgressTimerTask.cancel();
            mUpdateProgressTimerTask = null;
        }
    }

    /**
     * 更新进度条的抽象方法，需要具体类实现
     */
    protected abstract void updateProgress();


    /**
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
