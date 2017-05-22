package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.Context;
import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.R;

/**
 * Created by drawf on 27/04/2017.
 * ------------------------------
 */

public class PlayButton extends BaseVideoPlayerPlugin implements View.OnClickListener {

    public static final int ACTION_ON_PLAY = BASE_ACTION_PLAY_BUTTON;
    public static final int ACTION_ON_PAUSE = BASE_ACTION_PLAY_BUTTON + 1;

    public static final int ACTION_DO_PLAY = BASE_ACTION_PLAY_BUTTON + 10;
    public static final int ACTION_DO_PAUSE = BASE_ACTION_PLAY_BUTTON + 11;
    public static final int ACTION_DO_TOGGLE_PLAY_PAUSE = BASE_ACTION_PLAY_BUTTON + 12;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeBottomBar.ibPlay.setEnabled(false);
        mBinding.includeBottomBar.ibPlay.setOnClickListener(this);
    }

    @Override
    public void onLifePause(Context context) {
        super.onLifePause(context);
        pause();
    }

    @Override
    public void onLifeResume(Context context) {
        super.onLifeResume(context);
//        start();
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_PLAY:
                play();
                break;
            case ACTION_DO_PAUSE:
                pause();
                break;
            case ACTION_DO_TOGGLE_PLAY_PAUSE:
                togglePlayPause();
                break;
        }
    }

    private void updateUI() {
        mBinding.includeBottomBar.ibPlay.setImageResource(mBoard.mIsPlaying ?
                R.drawable.ic_media_controller_state_play_small : R.drawable.ic_media_controller_state_pause_small);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBinding.includeBottomBar.ibPlay.getId()) {
            if (mPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
        }
    }

    private void play() {
        if (mPlayer != null) {
            mBoard.mIsPlaying = true;
            mPlayer.start();
            updateUI();

            doAction(ACTION_ON_PLAY);
        }
    }

    private void pause() {
        if (mPlayer != null) {
            mBoard.mIsPlaying = false;
            mPlayer.pause();
            updateUI();

            doAction(ACTION_ON_PAUSE);
        }
    }

    private void togglePlayPause() {
        if (mBoard.mIsPlaying) {
            pause();
        } else {
            play();
        }
    }

    @Override
    public void onPreparedListener(PLMediaPlayer plMediaPlayer) {
        super.onPreparedListener(plMediaPlayer);
        mBinding.includeBottomBar.ibPlay.setEnabled(true);
    }

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        super.onInfoListener(plMediaPlayer, what, extra);
        switch (what) {
            case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mBoard.mIsPlaying = true;
                updateUI();
                break;
        }
    }

}
