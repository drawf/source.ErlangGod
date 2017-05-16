package me.erwa.source.erlanggod.player;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.FragmentPlayerBoardBinding;
import me.erwa.source.erlanggod.player.widget.Couple;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.BackButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Download;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Interaction;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.LoadingPanel;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.OperationBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.PlayButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.ProgressBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.StatePanel;

/**
 * Created by drawf on 2017/3/22.
 * ------------------------------
 */

public class VideoPlayerFragment extends Fragment implements View.OnClickListener {

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
    }

    private FragmentPlayerBoardBinding mBinding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        mBinding.videoView.setAVOptions(OptionsManager.newInstance().setAutoStart(true).build());
//        mBinding.videoView.setVideoPath("https://hls.media.yangcong345.com/mobileM/mobileM_58c26cbb36eaf35866aae116.m3u8");
        mBinding.videoView.setVideoPath("https://o558dvxry.qnssl.com/pcM/pcM_584f96fbefdf207b0822cf7a.m3u8");
                mediaControllerBoard = new MediaControllerBoard(getActivity(), R.layout.media_controller_board);
        mBinding.videoView.setMediaController(mediaControllerBoard);

        mediaControllerBoard.addPlugin(OperationBar.newInstance());
        mediaControllerBoard.addPlugin(ProgressBar.newInstance());
        mediaControllerBoard.addPlugin(LoadingPanel.newInstance());
        mediaControllerBoard.addPlugin(Download.newInstance());
        mediaControllerBoard.addPlugin(BackButton.newInstance());
        mediaControllerBoard.addPlugin(StatePanel.newInstance());

        mediaControllerBoard.addPlugin(Interaction.newInstance(),
                Couple.in(ProgressBar.class, false));

        mediaControllerBoard.addPlugin(PlayButton.newInstance(),
                Couple.in(StatePanel.class, true),
                Couple.in(Interaction.class, false));

        mBinding.btnShow.setOnClickListener(this);
        mBinding.btnHide.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mediaControllerBoard.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaControllerBoard.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.videoView.stopPlayback();
    }

    private MediaControllerBoard mediaControllerBoard;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
//                mediaControllerBoard.show();
                mBinding.videoView.start();
                break;
            case R.id.btn_hide:
                mBinding.videoView.pause();
//                mediaControllerBoard.hide();
                break;
        }
    }
}
