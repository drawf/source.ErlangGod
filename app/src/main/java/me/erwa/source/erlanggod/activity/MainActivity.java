package me.erwa.source.erlanggod.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.ActivityMainBinding;
import me.erwa.source.erlanggod.player.VideoPlayerFragment;

public class MainActivity extends AppCompatActivity {

    //https://developer.qiniu.com/pili/sdk/1210/the-android-client-sdk
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        replaceFragment(VideoPlayerFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
