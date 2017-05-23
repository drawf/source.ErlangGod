package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.player.OptionsManager;
import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.ToastUtils;

/**
 * Created by drawf on 2017/5/22.
 * ------------------------------
 */

public class PlayerController extends BaseVideoPlayerPlugin {

    public static final int ACTION_DO_RETRY_PLAY = BASE_ACTION_PLAYER_CONTROLLER + 20;
    public static final int CURRENT_PLAY_PRE = 0;
    public static final int CURRENT_PLAY_NORMAL = 1;
    public static final int CURRENT_PLAY_END = 2;

    private boolean isPaused;
    private int currentPlay = CURRENT_PLAY_NORMAL;


    @Override
    public void doInit() {
        super.doInit();
        mBoard.mVideoView.setAVOptions(OptionsManager.newInstance().build());
        doPlay(12000);
    }


    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_RETRY_PLAY:
                doPlay(0);
                break;
            case QualityMode.ACTION_ON_MODIFY_QUALITY_MODE:
                isPaused = !mBoard.mIsPlaying;

                long pos = mPlayer.getCurrentPosition();
                doPlay(pos);
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

    @Override
    public void onCompletionListener(PLMediaPlayer plMediaPlayer) {
        super.onCompletionListener(plMediaPlayer);
        LogUtils.trace("onCompletion:" + mPlayer.getCurrentPosition() + "==>" + mPlayer.getDuration());

        switch (currentPlay) {
            case CURRENT_PLAY_PRE:
                break;
            case CURRENT_PLAY_NORMAL:
                ToastUtils.show("播放完毕");
                //判断是否播放片尾视频
                if (true) {
                    doPlayEnd();
                }
                break;
            case CURRENT_PLAY_END:
                ToastUtils.show("片尾也完了");
                break;
        }

    }

    private void doPlay(long seek) {
        currentPlay = CURRENT_PLAY_NORMAL;
        String url = (String) fetchData(VideoData.ACTION_FETCH_CURRENT_QUALITY_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(url);
        if (seek > 0) {
            mBoard.mVideoView.seekTo(seek);
        }
    }

    private void doPlayEnd() {
        currentPlay = CURRENT_PLAY_END;
        String url = (String) fetchData(VideoData.ACTION_FETCH_END_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(url);

//        doAction(GesturePanel.ACTION_DO_DISABLED);
    }
}
