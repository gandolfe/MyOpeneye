package yys.com.myopeneye.view.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import yys.com.myopeneye.R;
import yys.com.myopeneye.view.fragment.DailyFragment;
import yys.com.myopeneye.view.fragment.HotFragment;
import yys.com.myopeneye.view.fragment.MoreFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DailyFragment mDailyFragment;
    private HotFragment mHotFragment;
    private MoreFragment mMoreFragment;
    private ImageView main_bg_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);


        initFragment();

        bottomNavigationView = findViewById(R.id.bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.day_select:
                        showFragment(mDailyFragment);
                        break;
                    case R.id.find_more:
                        showFragment(mMoreFragment);
                        break;
                    case R.id.hot_top:
                        showFragment(mHotFragment);
                        break;
                }

                return true;
            }
        });

        showFragment(mDailyFragment);
    }

    private void initFragment(){
        mDailyFragment = DailyFragment.getInstance(this);
        mMoreFragment = new MoreFragment();
        mHotFragment = new HotFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(!mDailyFragment.isAdded()){
            fragmentManager.beginTransaction().add(R.id.content,mDailyFragment).commit();
        }
        if(!mMoreFragment.isAdded()){
            fragmentManager.beginTransaction().add(R.id.content,mMoreFragment).commit();
        }
        if(!mHotFragment.isAdded()){
            fragmentManager.beginTransaction().add(R.id.content,mHotFragment).commit();
        }

    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragment instanceof DailyFragment){
            fragmentManager.beginTransaction()
                    .hide(mMoreFragment)
                    .hide(mHotFragment)
                    .show(fragment)
                    .commit();
        }else if(fragment instanceof MoreFragment){
            fragmentManager.beginTransaction()
                    .hide(mDailyFragment)
                    .hide(mHotFragment)
                    .show(fragment)
                    .commit();
        }else if(fragment instanceof HotFragment){
            fragmentManager.beginTransaction()
                    .hide(mDailyFragment)
                    .hide(mMoreFragment)
                    .show(fragment)
                    .commit();
        }
    }


}
