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
    public static final int ACTION_DO_JUMP_OVER = BASE_ACTION_PLAYER_CONTROLLER + 21;

    public static final int ACTION_FETCH_CURRENT_PLAY = BASE_ACTION_PLAYER_CONTROLLER + 30;

    public static final int CURRENT_PLAY_PRE = 0;
    public static final int CURRENT_PLAY_NORMAL = 1;
    public static final int CURRENT_PLAY_END = 2;

    private boolean isPaused;
    private int currentPlay = CURRENT_PLAY_NORMAL;


    @Override
    public void doInit() {
        super.doInit();
        mBoard.mVideoView.setAVOptions(OptionsManager.newInstance().build());
        //判断是否播片头
        doPlayPre();
//        doPlayNormal(12000);
    }


    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_RETRY_PLAY:
                doPlayNormal(0);
                break;
            case ACTION_DO_JUMP_OVER:
                onCompletionListener(null);
                break;
            case QualityMode.ACTION_ON_SWITCH_QUALITY_MODE:
                isPaused = !mBoard.mIsPlaying;

                long pos = mPlayer.getCurrentPosition();
                doPlayNormal(pos);
                break;
            case ScrubController.ACTION_ON_PROCESS_PRE_FINISHED:
                onPlayPreFinished();
                break;
            case ScrubController.ACTION_ON_PROCESS_END_FINISHED:
                break;
        }
    }

    @Override
    public Object replyFetchData(int action) {
        switch (action) {
            case ACTION_FETCH_CURRENT_PLAY:
                return currentPlay;
        }
        return null;
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
                doPlayNormal(0);
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

    private long seekTemp;

    private void doPlayNormal(long seek) {
        seekTemp = seek;
        currentPlay = CURRENT_PLAY_NORMAL;

        doAction(OperationBar.ACTION_DO_ENABLED);
        doAction(GesturePanel.ACTION_DO_ENABLED);
        doAction(JumpOverButton.ACTION_DO_DISABLED);

        doAction(ScrubController.ACTION_DO_PROCESS_PRE);

    }

    private void onPlayPreFinished() {
        String url = (String) fetchData(ScrubController.ACTION_FETCH_CURRENT_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(url);
        if (seekTemp > 0) {
            mBoard.mVideoView.seekTo(seekTemp);
        }
    }

    private void doPlayEnd() {
        currentPlay = CURRENT_PLAY_END;

        doAction(OperationBar.ACTION_DO_DISABLED);
        doAction(GesturePanel.ACTION_DO_DISABLED);
        doAction(JumpOverButton.ACTION_DO_ENABLED);

        String url = (String) fetchData(VideoData.ACTION_FETCH_END_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(url);
    }

    private void doPlayPre() {
        currentPlay = CURRENT_PLAY_PRE;

        doAction(OperationBar.ACTION_DO_DISABLED);
        doAction(GesturePanel.ACTION_DO_DISABLED);
        doAction(JumpOverButton.ACTION_DO_ENABLED);

        String url = (String) fetchData(VideoData.ACTION_FETCH_END_URL);
        mBoard.mVideoView.stopPlayback();
        mBoard.mVideoView.setVideoPath(url);
    }
}
