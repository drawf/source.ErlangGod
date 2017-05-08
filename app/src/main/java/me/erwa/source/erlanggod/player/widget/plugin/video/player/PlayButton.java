package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 27/04/2017.
 * ------------------------------
 */

public class PlayButton extends BaseVideoPlayerPlugin implements View.OnClickListener {

    public static PlayButton newInstance() {
        return new PlayButton();
    }


    private boolean mIsPlaying;

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);

        mBinding.includeBottomBar.ibPlay.setEnabled(false);
        mBinding.includeBottomBar.ibPlay.setOnClickListener(this);
    }

    @Override
    public void onLifePause() {
        super.onLifePause();
        pause();
    }

    @Override
    public void onLifeResume() {
        super.onLifeResume();
//        start();
    }

    private void updateUI() {
        mBinding.includeBottomBar.ibPlay.setImageResource(mIsPlaying ?
                R.drawable.ic_pause_white : R.drawable.ic_play_arrow_gray);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBinding.includeBottomBar.ibPlay.getId()) {
            if (mPlayer.isPlaying()) {
                pause();
            } else {
                start();
            }
        }
    }

    private void pause() {
        if (mPlayer != null) {
            mIsPlaying = false;
            mPlayer.pause();
            updateUI();
        }
    }

    private void start() {
        if (mPlayer != null) {
            mIsPlaying = true;
            mPlayer.start();
            updateUI();
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
                mIsPlaying = true;
                updateUI();
                break;
        }
    }

}
