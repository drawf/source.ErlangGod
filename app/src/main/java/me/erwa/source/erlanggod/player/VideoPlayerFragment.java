package me.erwa.source.erlanggod.player;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.FragmentPlayerBoardBinding;

/**
 * Created by drawf on 2017/3/22.
 * ------------------------------
 */

public class VideoPlayerFragment extends Fragment {

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
    }

    private FragmentPlayerBoardBinding mBinding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Auto hide and show navigation bar and status bar for API >= 19. Keep screen on.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_board, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mBinding.videoView.setAVOptions(OptionsManager.newInstance().setAutoStart(false).build());
        mBinding.videoView.setVideoPath("https://o558dvxry.qnssl.com/pcM/pcM_584f96fbefdf207b0822cf7a.m3u8");
//        mBinding.videoView.setMediaController(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.videoView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.videoView.stopPlayback();
    }

}
