package com.yys.playercore;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yys.mynicevideoplayer.R;

/**
 * Created by yangys on 2019/2/12.
 */

public class MyNiceVideoPlayerController extends BaseVideoPlayerController
        implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{


    private LinearLayout mLoadingLayout;
    private TextView mLoadtext;

    private LinearLayout mChangePositionLayout;
    private TextView mChangePositionText;
    private ProgressBar mChangePositionProgress;

    private LinearLayout mChangeBrightLayout;
    private ProgressBar mChangeBrightProgress;

    private LinearLayout mChangeVolumeLayout;
    private ProgressBar mChangeVolumeProgress;

    private LinearLayout mCompletedLayout;
    private TextView mReplay;
    private TextView mShare;

    private LinearLayout mErrorLayout;
    private TextView mRetry;

    private LinearLayout mTopLayout;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mBatteryTimeLayout;
    private ImageView mBattery;
    private TextView mTime;

    private LinearLayout mBottomLayout;
    private ImageView mPlayPause;
    private TextView mSeekPosition;
    private TextView mDuration;
    private SeekBar mSeekBar;
    private ImageView mFullScreen;

    private TextView mLengthTime;

    private ImageView mCenter_bg_imgview;  //开始的背景图片
    private  ImageView mCenterStart;

    private boolean topbottomVisible = true;

    private CountDownTimer dissmissTopBottomTimer;


    private static final String TAG = "MyNiceVideoPlayerController";
    public MyNiceVideoPlayerController(@NonNull Context context) {
        super(context);
        init();
    }


    private void init(){
        LayoutInflater.from(mContext).inflate(R.layout.mycontroller_layout,this,true);
        mLoadingLayout = findViewById(R.id.loading);
        mLoadtext = findViewById(R.id.load_text);
        mChangePositionLayout = findViewById(R.id.change_position);
        mChangePositionText = findViewById(R.id.change_position_current);
        mChangePositionProgress = findViewById(R.id.change_position_progress);
        mChangeBrightLayout = findViewById(R.id.change_brightness);
        mChangeBrightProgress = findViewById(R.id.change_brightness_progress);
        mChangeVolumeLayout = findViewById(R.id.change_volume);
        mChangeVolumeProgress = findViewById(R.id.change_volume_progress);
        mCompletedLayout = findViewById(R.id.completed);
        mReplay = findViewById(R.id.replay);
        mShare = findViewById(R.id.share);
        mErrorLayout = findViewById(R.id.error);
        mRetry = findViewById(R.id.retry);
        mTopLayout = findViewById(R.id.top);
        mBack = findViewById(R.id.back);
        mTitle = findViewById(R.id.title);
        mBatteryTimeLayout = findViewById(R.id.battery_time);
        mBattery = findViewById(R.id.battery);
        mTime = findViewById(R.id.time);
        mBottomLayout = findViewById(R.id.bottom);
        mPlayPause = findViewById(R.id.restart_or_pause);
        mSeekPosition = findViewById(R.id.position);
        mDuration = findViewById(R.id.duration);
        mSeekBar = findViewById(R.id.seek);
        mFullScreen = findViewById(R.id.full_screen);
        mLengthTime = findViewById(R.id.length);
        mCenterStart = findViewById(R.id.center_start);
        mCenter_bg_imgview = findViewById(R.id.center_bg_imgview);

        mReplay.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mPlayPause.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mFullScreen.setOnClickListener(this);
        mCenterStart.setOnClickListener(this);
        this.setOnClickListener(this);

        mBottomLayout.setVisibility(GONE);
    }




    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setImage(int resId) {

    }

    @Override
    public ImageView imageView() {
        return mCenter_bg_imgview;
    }

    @Override
    public void setLenght(long length) {
        mLengthTime.setText(NiceUtil.getTimeFromMill(length));
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        switch(playState){
            case NiceVideoPlayer.STATE_IDLE:
                break;

            case NiceVideoPlayer.STATE_PREPARING:
                mCenterStart.setVisibility(View.GONE);
                mCenter_bg_imgview.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(VISIBLE);
                mLoadtext.setText("正在准备...");
                mCompletedLayout.setVisibility(GONE);
                mErrorLayout.setVisibility(GONE);
                mLengthTime.setVisibility(GONE);
                setTopBottomVisible(false);
                break;
            case NiceVideoPlayer.STATE_PREPARED:
                startUpdateProgressTimer();
                break;
            case NiceVideoPlayer.STATE_PLAYING:
                mLoadingLayout.setVisibility(GONE);
                mPlayPause.setImageResource(R.drawable.ic_player_pause);
                startDissMissTopBottomTimer();
                break;
            case NiceVideoPlayer.STATE_PAUSED:
                mLoadingLayout.setVisibility(GONE);
                mPlayPause.setImageResource(R.drawable.ic_player_start);
                cancelDissMissTopBottomTimer();
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PLAYING:
                mLoadingLayout.setVisibility(VISIBLE);
                mLoadtext.setText("正在缓冲...");
                break;
            case NiceVideoPlayer.STATE_BUFFERING_PAUSED:
                mLoadingLayout.setVisibility(VISIBLE);
                mPlayPause.setImageResource(R.drawable.ic_player_start);
                mLoadtext.setText("正在缓冲...");
                break;
            case NiceVideoPlayer.STATE_COMPLETED:
                cancelUpdateProgressTimer();
                mCompletedLayout.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    protected void onPlayModeChanged(int playMode) {
        if(playMode == NiceVideoPlayer.MODE_NORMAL){
            mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
        }else if(playMode == NiceVideoPlayer.MODE_FULL_SCREEN){
            mBack.setVisibility(View.GONE);
            mTitle.setVisibility(View.GONE);
            mFullScreen.setImageResource(R.drawable.ic_player_shrink);
        }else if(playMode == NiceVideoPlayer.MODE_TINY_WINDOW){

        }
    }

    @Override
    protected void reset() {
        cancelUpdateProgressTimer();
        cancelDissMissTopBottomTimer();
        mSeekBar.setProgress(0);
        mSeekBar.setSecondaryProgress(0);

        mCenterStart.setVisibility(VISIBLE);
        mCenter_bg_imgview.setVisibility(VISIBLE);
    }

    @Override
    protected void showChangePosition(long duration, int newPositionProgress) {

    }

    @Override
    protected void hideChangePosition() {

    }

    @Override
    protected void updateProgress() {
        long position = mNiceVideoPlayer.getCurrentPosition();
        long duration = mNiceVideoPlayer.getDuration();
        int bufferPercent = mNiceVideoPlayer.getBufferPercentage();
        mSeekBar.setSecondaryProgress(bufferPercent);
        int progress = (int) (position/(duration *1.0f) *100);
        mSeekBar.setProgress(progress);
        mSeekPosition.setText(NiceUtil.getTimeFromMill(position));
        mDuration.setText(NiceUtil.getTimeFromMill(duration));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.center_start) {
            if (mNiceVideoPlayer.isIdle()) {
                mNiceVideoPlayer.start();
            }

        } else if (i == R.id.restart_or_pause) {
            if (mNiceVideoPlayer.isPlaying() || mNiceVideoPlayer.isBufferingPlaying()) {
                mNiceVideoPlayer.pause();
            } else if (mNiceVideoPlayer.isPaused() || mNiceVideoPlayer.isBufferingPaused()) {
                mNiceVideoPlayer.restart();
            }

        } else if (i == R.id.full_screen) {
            if (mNiceVideoPlayer.isFullScreen()) {
                mNiceVideoPlayer.exitFullScreen();
            } else {
                mNiceVideoPlayer.enterFullScreen();
            }

        }else if(i == R.id.replay){
            //重新播放
            mNiceVideoPlayer.restart();
        }else if(i == R.id.share){
            //分享

        }

        if(view == this){
            Log.i(TAG,"click controller container");
            if(mNiceVideoPlayer.isPlaying()
                    || mNiceVideoPlayer.isPaused()
                    || mNiceVideoPlayer.isBufferingPaused()
                    || mNiceVideoPlayer.isBufferingPlaying()){
                setTopBottomVisible(!topbottomVisible);
            }
        }

    }


    private void setTopBottomVisible(boolean visible){
        mBottomLayout.setVisibility(visible?VISIBLE:GONE);
        mTopLayout.setVisibility(visible?VISIBLE:GONE);
        topbottomVisible = visible;

        if(visible){
            if(!mNiceVideoPlayer.isBufferingPaused() && !mNiceVideoPlayer.isPaused()){
                startDissMissTopBottomTimer();
            }
        }else{
            cancelDissMissTopBottomTimer();
        }
    }


    private void startDissMissTopBottomTimer(){
        cancelDissMissTopBottomTimer();
        if(dissmissTopBottomTimer == null){
            dissmissTopBottomTimer = new CountDownTimer(8000,8000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    setTopBottomVisible(false);
                }
            };
        }
        dissmissTopBottomTimer.start();
    }

    private void cancelDissMissTopBottomTimer(){
        if(dissmissTopBottomTimer!=null){
            dissmissTopBottomTimer.cancel();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mNiceVideoPlayer.isBufferingPaused() || mNiceVideoPlayer.isPaused()) {
            mNiceVideoPlayer.restart();
        }

        Log.i(TAG,"seekBar.getProgress:"+seekBar.getProgress());
        long position = (long) (mNiceVideoPlayer.getDuration() * (seekBar.getProgress() / 100.0f));
        Log.i(TAG,"position:"+position);
        mNiceVideoPlayer.seekTo(position);
    }
}
