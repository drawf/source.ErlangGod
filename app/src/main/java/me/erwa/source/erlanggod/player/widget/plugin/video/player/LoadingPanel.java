package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.net.TrafficStats;
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

    private long lastBytes;

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

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
                setCacheSpeed();
                break;
        }
    }

    private void togglePanel(boolean b) {
        mBinding.includeLoadingPanel.container.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void setCacheSpeed() {
        long l = TrafficStats.getTotalRxBytes() - lastBytes;
        lastBytes = TrafficStats.getTotalRxBytes();
        mBinding.includeLoadingPanel.tvBitrate.setText(formatBitrate(l));
    }

    private String formatBitrate(long bitrate) {
        double result = bitrate * 1.0 / 1024;
        String unit = "KB/s";
        if (result > 1024) {
            result /= 1024;
            unit = "MB/s";
        }
        return new DecimalFormat("#0.00").format(result) + unit;
    }

}
