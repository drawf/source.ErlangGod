package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 2017/5/17.
 * ------------------------------
 */

public class VideoTitle extends BaseVideoPlayerPlugin {

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        String name = fetchData(VideoData.ACTION_FETCH_NAME);
        mBinding.includeTopBar.tvTitle.setText(name);
    }

}
