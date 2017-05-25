package me.erwa.source.erlanggod.activity;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.ActivityMainBinding;
import me.erwa.source.erlanggod.player.VideoPlayerFragment;
import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.PlayerUtils;

public class MainActivity extends AppCompatActivity {

    //https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        replaceFragment(VideoPlayerFragment.newInstance());
        LogUtils.trace("onCreate-->");
    }

    private void replaceFragment(Fragment fragment) {
        onConfigurationChanged(getResources().getConfiguration());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(mBinding.playerHolder.getId(), fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.trace("onConfigurationChanged-->");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mBinding.playerHolder.setLayoutParams(params);
        } else {
            int height = PlayerUtils.getTinyHeight(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            mBinding.playerHolder.setLayoutParams(params);
        }
    }

}
