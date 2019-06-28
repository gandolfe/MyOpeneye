package yys.com.myopeneye.view.activity;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import yys.com.myopeneye.R;

/**
 * Created by yangys on 2018/11/20.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_img)
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);
        initView();
        startAnimation();
    }

    private void initView(){

        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_WEEK);
        switch (d){
            case 0:
                imageView.setImageResource(R.drawable.wallpaper_7);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wallpaper_3);
                break;
            case 2:
                imageView.setImageResource(R.drawable.wallpaper_5);
                break;
            case 3:
                imageView.setImageResource(R.drawable.wallpaper_4);
                break;
            case 4:
                imageView.setImageResource(R.drawable.wallpaper_2);
                break;
            case 5:
                imageView.setImageResource(R.drawable.wallpaper_8);
                break;
            case 6:
                imageView.setImageResource(R.drawable.wallpaper_6);
                break;

        }
    }

    private void startAnimation(){
        View splashview = findViewById(R.id.splash_img);

        ValueAnimator animator = ValueAnimator.ofObject(new FloatEvaluator(),1.1f,1.3f);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float)valueAnimator.getAnimatedValue();
                if(value!=1.3f){
                    imageView.setScaleX(value);
                    imageView.setScaleY(value);
                }else{
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    overridePendingTransition(0,android.R.anim.fade_out);  //设置activity淡出效果
                    finish();
                }
            }
        });
        animator.start();
    }
}
