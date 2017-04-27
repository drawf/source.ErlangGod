package me.erwa.source.erlanggod.player.widget.plugin;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 27/04/2017.
 * ------------------------------
 */

public class PlayButton extends BasePlugin implements View.OnClickListener {

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

    private void updateUI() {
        mBinding.includeBottomBar.ibPlay.setImageResource(mIsPlaying ?
                R.drawable.ic_pause_white : R.drawable.ic_play_arrow_gray);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBinding.includeBottomBar.ibPlay.getId()) {
            if (mPlayer.isPlaying()) {
                mIsPlaying = false;
                mPlayer.pause();
            } else {
                mIsPlaying = true;
                mPlayer.start();
            }
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
