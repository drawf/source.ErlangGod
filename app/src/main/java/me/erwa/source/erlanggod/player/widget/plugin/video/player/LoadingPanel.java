package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import java.text.DecimalFormat;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class LoadingPanel extends BaseVideoPlayerPlugin {

    public static LoadingPanel newInstance() {
        return new LoadingPanel();
    }

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        mBinding.includeLoadingPanel.tvBitrate.setVisibility(View.GONE);
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

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
//                mBinding.includeLoadingPanel.tvBitrate.setText(formatBitrate());
                break;
        }
    }

    private void togglePanel(boolean b) {
        mBinding.includeLoadingPanel.container.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private String formatBitrate() {
        long bitrate = mBoard.mVideoView.getVideoBitrate();
        double result = bitrate * 1.0 / (1024 * 8);
        String unit = "KB/s";
        if (result > 1024) {
            result = bitrate * 1.0 / (1024 * 1024 * 8);
            unit = "MB/s";
        }
        return new DecimalFormat("#0.00").format(result) + unit;
    }

}
