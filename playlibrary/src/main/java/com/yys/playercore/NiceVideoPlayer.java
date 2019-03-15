package com.yys.playercore;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by yangys on 2019/2/12.
 * 播放器类
 */

public class NiceVideoPlayer  extends FrameLayout implements INiceVideoPlayer,TextureView.SurfaceTextureListener{

    /**
     * IjkPlayer
     **/
    public static final int TYPE_IJK = 0;

    /**
     * MediaPlayer
     **/
    public static final int TYPE_NATIVE = 1;



    /**
     * 播放错误
     **/
    public static final int STATE_ERROR = -1;
    /**
     * 播放未开始
     **/
    public static final int STATE_IDLE = 0;
    /**
     * 播放准备中
     **/
    public static final int STATE_PREPARING = 1;
    /**
     * 播放准备就绪
     **/
    public static final int STATE_PREPARED = 2;
    /**
     * 正在播放
     **/
    public static final int STATE_PLAYING = 3;
    /**
     * 暂停播放
     **/
    public static final int STATE_PAUSED = 4;
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     **/
    public static final int STATE_BUFFERING_PLAYING = 5;
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
     **/
    public static final int STATE_BUFFERING_PAUSED = 6;
    /**
     * 播放完成
     **/
    public static final int STATE_COMPLETED = 7;

    /**
     * 普通模式
     **/
    public static final int MODE_NORMAL = 10;
    /**
     * 全屏模式
     **/
    public static final int MODE_FULL_SCREEN = 11;
    /**
     * 小窗口模式
     **/
    public static final int MODE_TINY_WINDOW = 12;

    private static final String TAG = "NiceVideoPlayer";

    private int mPlayerType = TYPE_IJK;
    private int mCurrentState = STATE_IDLE;
    private int mCurrentMode = MODE_NORMAL;
    private int mBufferPercent;
    private Context mContext;
    private FrameLayout mContainer;

    private BaseVideoPlayerController mController;

    private String mUrl;

    private AudioManager mAudioManager;
    private NiceTextureView mTextureView;
    private IMediaPlayer mMediaPlayer;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    public NiceVideoPlayer(@NonNull Context context){
        this(context,null);
    }

    public NiceVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer,params);
    }

    public void setPlayerType(int playerType){
        mPlayerType = playerType;
    }

    public void setUrlSource(String url){
        this.mUrl = url;
    }

    public void setController(BaseVideoPlayerController controller){
        mContainer.removeView(mController);
        mController = controller;
        mController.reset();
        controller.setNiceVideoPlayer(this);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController,params);
    }


    @Override
    public void setUp(String url, Map<String, String> headers) {
        this.mUrl = url;
    }

    @Override
    public void start() {
        if(mCurrentState == STATE_IDLE){
            initAudioManager();
            initMediaPlayer();
            initTextureView();
            addTextureView();
        }else{
            Log.i("","只能在idle状态使用start");
        }
    }

    @Override
    public void start(long position) {

    }

    @Override
    public void restart() {
        if(mCurrentState == STATE_PAUSED){
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
        }else if(mCurrentState == STATE_BUFFERING_PAUSED){
            mMediaPlayer.start();
            mCurrentState = STATE_BUFFERING_PLAYING;
            mController.onPlayStateChanged(mCurrentState);
        }else if(mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR){
            mMediaPlayer.reset();
            openMediaPlayer();
        }
    }

    @Override
    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer.pause();
            mCurrentState = STATE_BUFFERING_PAUSED;
            mController.onPlayStateChanged(mCurrentState);
        }
    }

    @Override
    public void seekTo(long pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public void setVolume(int volume) {

    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void continueFromLastPosition(boolean continueFromLastPosition) {

    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isPreparing() {
        return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isBufferingPlaying() {
        return mCurrentState == STATE_BUFFERING_PLAYING;
    }

    @Override
    public boolean isBufferingPaused() {
        return mCurrentState == STATE_BUFFERING_PAUSED;
    }

    @Override
    public boolean isPlaying() {
        return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
        return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isError() {
        return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isCompleted() {
        return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public boolean isFullScreen() {
        return mCurrentMode == MODE_FULL_SCREEN;
    }

    @Override
    public boolean isTinyWindow() {
        return mCurrentMode == MODE_TINY_WINDOW;
    }

    @Override
    public boolean isNormal() {
        return mCurrentMode == MODE_NORMAL;
    }

    @Override
    public int getMaxVolume() {
        return 0;
    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercent;
    }

    @Override
    public float getSpeed(float speed) {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }

    /**
     * 主要思路是先把NiceVideoPlayer中的mContainer移除，然后把MContainer添加到此Activity的跟布局中实现全屏
     */
    @Override
    public void enterFullScreen() {
        if(mCurrentMode  == MODE_FULL_SCREEN) return;

        //隐藏状态栏
        NiceUtil.hideActionBar(mContext);
        //横屏
        NiceUtil.getAppCompatActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ViewGroup viewGroup = NiceUtil.scanForActivity(mContext).findViewById(android.R.id.content);


        if(mCurrentMode == MODE_TINY_WINDOW){

        }else{
            this.removeView(mContainer);
        }


        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mContainer,layoutParams);
        mCurrentMode = MODE_FULL_SCREEN;
        mController.onPlayModeChanged(mCurrentMode);

    }

    @Override
    public boolean exitFullScreen() {
        if(mCurrentMode != MODE_FULL_SCREEN) return false;
        ViewGroup viewGroup = NiceUtil.getAppCompatActivity(mContext).findViewById(android.R.id.content);
        viewGroup.removeView(mContainer);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer,params);

        //显示状态栏
        NiceUtil.ShowActionBar(mContext);
        //竖屏
        NiceUtil.getAppCompatActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCurrentMode = MODE_NORMAL;
        mController.onPlayModeChanged(mCurrentMode);
        return true;
    }

    @Override
    public void enterTinyWindow() {

    }

    @Override
    public boolean exitTinyWindow() {
        return false;
    }

    @Override
    public void releasePlayer() {
        if(mAudioManager!=null){
            mAudioManager.abandonAudioFocus(null);
            mAudioManager = null;
        }
        if(mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if(mSurfaceTexture!=null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentState = STATE_IDLE;
    }

    @Override
    public void release() {

    }

    private void initAudioManager(){
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(null,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
    }

    private void initMediaPlayer(){
        if(mMediaPlayer == null){
            if(mPlayerType == TYPE_IJK){
                mMediaPlayer = new IjkMediaPlayer();
            }else if(mPlayerType == TYPE_NATIVE){
                mMediaPlayer = new AndroidMediaPlayer();
            }
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    private void initTextureView(){
        if(mTextureView == null){
            mTextureView = new NiceTextureView(mContext);
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    private void addTextureView(){
        mContainer.removeView(mTextureView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mContainer.addView(mTextureView,0,params);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if(mSurfaceTexture == null){
            Log.i(TAG,"onSurfaceTextureAvailable");
            mSurfaceTexture = surfaceTexture;
            openMediaPlayer();
        }else{
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }

    }

    private void openMediaPlayer(){
        //屏幕常量
        mContainer.setKeepScreenOn(true);
        //设置监听
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnInfoListener(mOnInfoListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);

        //设置资源
        try {
            mMediaPlayer.setDataSource(mContext,Uri.parse(mUrl));
            if(mSurface == null){
                mSurface = new Surface(mSurfaceTexture);
            }
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
            mController.onPlayStateChanged(mCurrentState);
            Log.i(TAG,"播放器准备中");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"打开播放器错误",e);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            mCurrentState = STATE_PREPARED;
            mController.onPlayStateChanged(mCurrentState);
            iMediaPlayer.start();

        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            mCurrentState = STATE_COMPLETED;
            mController.onPlayStateChanged(mCurrentState);
            //关闭屏幕长亮
            mContainer.setKeepScreenOn(false);
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
            if(what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                //播放器开始渲染画面
                mCurrentState = STATE_PLAYING;
                mController.onPlayStateChanged(mCurrentState);
            }


            return true;
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
            mBufferPercent = i;
        }
    };

    public boolean onBackPress(){
        if(mCurrentMode == MODE_FULL_SCREEN){
            return exitFullScreen();
        }
        if(mCurrentMode == MODE_TINY_WINDOW){
            return exitTinyWindow();
        }
        return false;
    }

}
