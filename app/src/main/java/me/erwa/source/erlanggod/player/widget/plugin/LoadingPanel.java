package me.erwa.source.erlanggod.player.widget.plugin;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class LoadingPanel extends BasePlugin {

    public static LoadingPanel newInstance() {
        return new LoadingPanel();
    }

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        togglePanel(false);
    }

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        super.onInfoListener(plMediaPlayer, what, extra);
        switch (what) {
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                togglePanel(true);
                break;
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                togglePanel(false);
                break;
        }
    }

    private void togglePanel(boolean b) {
        mBinding.includeLoadingPanel.container.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
