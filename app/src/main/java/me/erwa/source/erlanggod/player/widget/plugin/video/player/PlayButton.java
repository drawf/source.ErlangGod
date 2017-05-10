package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import java.util.ArrayList;
import java.util.List;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 27/04/2017.
 * ------------------------------
 */

public class PlayButton extends BaseVideoPlayerPlugin<PlayButton.IPlayButton> implements View.OnClickListener, IStatePanel {

    public static PlayButton newInstance() {
        return new PlayButton();
    }

    private List<IPlayButton> mSubscribers = new ArrayList<>();

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
                start();
            }
        }
    }

    private void pause() {
        if (mPlayer != null) {
            mBoard.mIsPlaying = false;
            mPlayer.pause();
            updateUI();

            triggerPluginPause();
        }
    }

    private void start() {
        if (mPlayer != null) {
            mBoard.mIsPlaying = true;
            mPlayer.start();
            updateUI();

            triggerPluginPlay();
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

    @Override
    public void togglePlayPause() {
        if (mBoard.mIsPlaying) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public void addSubscriber(IPlayButton plugin) {
        super.addSubscriber(plugin);
        mSubscribers.add(plugin);
    }

    public interface IPlayButton {
        void play();

        void pause();
    }

    private void triggerPluginPlay() {
        if (!mSubscribers.isEmpty()) {
            for (IPlayButton p : mSubscribers) {
                p.play();
            }
        }
    }

    private void triggerPluginPause() {
        if (!mSubscribers.isEmpty()) {
            for (IPlayButton p : mSubscribers) {
                p.pause();
            }
        }
    }


}
