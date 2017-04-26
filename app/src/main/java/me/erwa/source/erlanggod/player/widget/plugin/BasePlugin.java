package me.erwa.source.erlanggod.player.widget.plugin;

import android.support.annotation.CallSuper;

import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class BasePlugin implements MediaControllerBoard.IPlugin{

    protected MediaControllerBoard mBoard;
    protected IMediaController.MediaPlayerControl mPlayer;
    protected MediaControllerBoardBinding mBinding;

    @CallSuper
    @Override
    public void init(MediaControllerBoard board){
        this.mBoard = board;
        this.mPlayer = board.mPlayer;
        this.mBinding = board.mBinding;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {

    }

    @Override
    public void onErrorListener(PLMediaPlayer plMediaPlayer, int errorCode) {

    }

    @Override
    public void onCompletionListener(PLMediaPlayer plMediaPlayer) {

    }
}
