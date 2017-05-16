package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 */

public class Interaction extends BaseVideoPlayerPlugin {

    public static Interaction newInstance() {
        return new Interaction();
    }

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
                LogUtils.trace("ACTION_ON_UPDATE_PROGRESS:" + mPlayer.getCurrentPosition());
                break;
        }
    }

}

