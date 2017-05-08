package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.support.annotation.CallSuper;

import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.player.widget.plugin.BasePlugin;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class BaseVideoPlayerPlugin<P> extends BasePlugin<MediaControllerBoardBinding, P> {

    protected MediaControllerBoardBinding mBinding;

    @CallSuper
    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        mBinding = super.mBinding;
    }
}
