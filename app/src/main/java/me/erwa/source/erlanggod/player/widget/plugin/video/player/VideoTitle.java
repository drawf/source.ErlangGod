package me.erwa.source.erlanggod.player.widget.plugin.video.player;

/**
 * Created by drawf on 2017/5/17.
 * ------------------------------
 */

public class VideoTitle extends BaseVideoPlayerPlugin {

    @Override
    public void doInit() {
        super.doInit();
        String name = (String) fetchData(VideoData.ACTION_FETCH_NAME);
        mBinding.includeTopBar.tvTitle.setText(name);
    }
}
