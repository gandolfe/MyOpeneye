package yys.com.myopeneye.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yys.playercore.MyNiceVideoPlayerController;
import com.yys.playercore.NiceVideoPlayer;

import jp.wasabeef.glide.transformations.BlurTransformation;
import yys.com.myopeneye.R;
import yys.com.myopeneye.utils.Util;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private NiceVideoPlayer mNiceVideoPlayer;
    private ImageView play_bgimg;
    private TextView categary_time;
    private TextView descriptTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        initData(getIntent());
    }

    private void initView(){

//        Toolbar toolbar = findViewById(R.id.mytoolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mNiceVideoPlayer = findViewById(R.id.nicevideoplayer);
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK);
        play_bgimg = findViewById(R.id.play_bgimg);
        categary_time = findViewById(R.id.categary_time);
        descriptTextView = findViewById(R.id.descript);
    }

    private void initData(Intent intent) {

        long duration = intent.getLongExtra("duration",0);
        String categary = intent.getStringExtra("categary");
        String imgurl = intent.getStringExtra("img");
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        String descript = intent.getStringExtra("descript");

        MyNiceVideoPlayerController controller = new MyNiceVideoPlayerController(this);
        controller.setLenght(duration); //设置时长
        controller.setTitle(title);
        Glide.with(this)
                .load(imgurl)
                .into(controller.imageView());
        mNiceVideoPlayer.setUrlSource(url);
        mNiceVideoPlayer.setController(controller);

        Glide.with(this)
                .load(imgurl)
                .bitmapTransform(new BlurTransformation(this,23,4))
                .into(play_bgimg);

        categary_time.setText("#" + categary +" "+ Util.getTimeString(duration));
        descriptTextView.setText(descript);
    }

    @Override
    protected void onPause() {
        if(mNiceVideoPlayer!=null){
            mNiceVideoPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mNiceVideoPlayer.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(mNiceVideoPlayer.onBackPress()) return;
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
