package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.player.OptionsManager;

/**
 * Created by drawf on 2017/5/22.
 * ------------------------------
 */

public class PlayerController extends BaseVideoPlayerPlugin {

    public static final int ACTION_DO_RETRY_PLAY = BASE_ACTION_PLAYER_CONTROLLER + 20;

    @Override
    public void doInit() {
        super.doInit();

        mBoard.mVideoView.setAVOptions(OptionsManager.newInstance().build());
        String s = (String) fetchData(VideoData.ACTION_FETCH_CURRENT_QUALITY_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(s);
        mBoard.mVideoView.seekTo(12000);

    }

    private boolean isPaused;

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_RETRY_PLAY:
                String s = (String) fetchData(VideoData.ACTION_FETCH_CURRENT_QUALITY_URL);
                mBoard.mVideoView.stopPlayback();
                mBoard.mVideoView.setVideoPath(s);
                break;
            case QualityMode.ACTION_ON_MODIFY_QUALITY_MODE:
                isPaused = !mBoard.mIsPlaying;

                long position1 = mPlayer.getCurrentPosition();
                String s1 = (String) fetchData(VideoData.ACTION_FETCH_CURRENT_QUALITY_URL);
                mBoard.mVideoView.stopPlayback();
                mBoard.mVideoView.setVideoPath(s1);
                mBoard.mVideoView.seekTo(position1);

                break;
        }
    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        super.onSeekComplete(plMediaPlayer);
        if (isPaused) {
            isPaused = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doAction(PlayButton.ACTION_DO_PAUSE);
                }
            }, 600);
        }
    }
}
