package me.erwa.source.erlanggod.player.widget.plugin;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.widget.SeekBar;

import com.pili.pldroid.player.PLMediaPlayer;

import java.util.Locale;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class ProgressBar extends BasePlugin implements SeekBar.OnSeekBarChangeListener {

    public static ProgressBar newInstance() {
        return new ProgressBar();
    }

    private static final int FLAG_UPDATE_PROGRESS = 1;

    private boolean mDragging;//拖动进度条时
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_UPDATE_PROGRESS:
                    updateProgress();
                    if (!mDragging && mPlayer.isPlaying()) {
                        sendMessageDelayed(obtainMessage(FLAG_UPDATE_PROGRESS), 1000);
                    }
                    break;
            }
        }
    };

    @CallSuper
    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        mBinding.includeBottomBar.sbProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onShow() {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(FLAG_UPDATE_PROGRESS), 1000);
    }

    @Override
    public void onHide() {
        mHandler.removeMessages(FLAG_UPDATE_PROGRESS);
    }

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {

    }

    private void updateProgress() {
        if (mPlayer == null || mDragging) return;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (duration > 0) {
            long pos = 1000L * position / duration;
            LogUtils.trace(pos);
            mBinding.includeBottomBar.sbProgress.setProgress((int) pos);
        }
        mBinding.includeBottomBar.sbProgress.setSecondaryProgress(mPlayer.getBufferPercentage() * 10);
        mBinding.includeBottomBar.tvTime.setText(String.format("%s/%s", generateTime(position), generateTime(duration)));

        LogUtils.trace("%s,,%s", position, duration);
    }

    private String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mDragging = true;
        mHandler.removeMessages(FLAG_UPDATE_PROGRESS);//取消进度更新
        mBoard.removeOperationHide();//取消操作栏自动隐藏
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;

        long duration = mPlayer.getDuration();
        long position = calculateNewPosition(duration, progress);

        mBinding.includeBottomBar.sbProgress.setSecondaryProgress(mPlayer.getBufferPercentage() * 10);
        mBinding.includeBottomBar.tvTime.setText(String.format("%s/%s", generateTime(position), generateTime(duration)));
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
        mPlayer.seekTo(calculateNewPosition(mPlayer.getDuration(), seekBar.getProgress()));
        mBoard.show();
        mDragging = false;
    }

    private long calculateNewPosition(long duration, int progress) {
        long position = duration * progress / 1000L;
        return (duration - position) < 9000 ? duration - 9000 : position;
    }
}
