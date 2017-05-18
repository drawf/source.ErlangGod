package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;
import me.erwa.source.erlanggod.player.widget.plugin.BasePlugin;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class BaseVideoPlayerPlugin extends BasePlugin<MediaControllerBoardBinding> {

    protected MediaControllerBoardBinding mBinding;

    @Override
    public void doInit() {
        super.doInit();
        mBinding = super.mBinding;
    }
}
