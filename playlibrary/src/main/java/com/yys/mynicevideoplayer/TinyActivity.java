package com.yys.mynicevideoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yys.playercore.MyNiceVideoPlayerController;
import com.yys.playercore.NiceVideoPlayer;


/**
 * Created by yangys on 2019/2/12.
 */

public class TinyActivity extends AppCompatActivity{

    private NiceVideoPlayer mNiceVideoPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tinyactivity);
        init();
    }

    private void init(){
        mNiceVideoPlayer = findViewById(R.id.nicevideoplayer);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK);
        String videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
        mNiceVideoPlayer.setUrlSource(videoUrl);
        MyNiceVideoPlayerController controller = new MyNiceVideoPlayerController(this);
        controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");
        controller.setLenght(98000);
        mNiceVideoPlayer.setController(controller);
    }

    @Override
    public void onBackPressed() {
        if(mNiceVideoPlayer!=null && mNiceVideoPlayer.onBackPress()) return;

        super.onBackPressed();
    }
}
