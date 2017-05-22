package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import java.text.DecimalFormat;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class LoadingPanel extends BaseVideoPlayerPlugin {
    private long lastBytes;
    private static final int FLAG_UPDATE_CACHE_SPEED = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_UPDATE_CACHE_SPEED:
                    sendMessageDelayed(obtainMessage(FLAG_UPDATE_CACHE_SPEED), 1000);
                    setCacheSpeed();
                    break;
            }
        }
    };

    @Override
    public void doInit() {
        super.doInit();
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
        if (b) {
            lastBytes = TrafficStats.getTotalRxBytes();
            mHandler.sendEmptyMessage(FLAG_UPDATE_CACHE_SPEED);
        } else {
            mHandler.removeMessages(FLAG_UPDATE_CACHE_SPEED);
        }
    }

    private void setCacheSpeed() {
        long l = TrafficStats.getTotalRxBytes() - lastBytes;
        lastBytes = TrafficStats.getTotalRxBytes();
        mBinding.includeLoadingPanel.tvSpeed.setText(formatBytes(l));
    }

    private String formatBytes(long bytes) {
        double result = bytes * 1.0 / 1024;
        String unit = "KB/s";
        if (result > 1024) {
            result /= 1024;
            unit = "MB/s";
        }
        return new DecimalFormat("#0").format(result) + unit;
    }

}
