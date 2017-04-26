package me.erwa.source.erlanggod.player.widget.plugin;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import com.pili.pldroid.player.IMediaController;

import java.util.Locale;

import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class ProgressBar implements MediaControllerBoard.IPlugin, SeekBar.OnSeekBarChangeListener {

    public static ProgressBar newInstance() {
        return new ProgressBar();
    }

    private static final int FLAG_UPDATE_PROGRESS = 1;

    private MediaControllerBoard mBoard;
    private IMediaController.MediaPlayerControl mPlayer;
    private MediaControllerBoardBinding mBinding;

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

    @Override
    public void init(MediaControllerBoard board) {
        this.mBoard = board;
        this.mPlayer = board.mPlayer;
        this.mBinding = board.mBinding;

        mBinding.includeBottomBar.sbProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onShow() {
        mHandler.sendEmptyMessage(FLAG_UPDATE_PROGRESS);
    }

    @Override
    public void onHide() {
        mHandler.removeMessages(FLAG_UPDATE_PROGRESS);
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
        long position = duration * progress / 1000;

        mBinding.includeBottomBar.sbProgress.setSecondaryProgress(mPlayer.getBufferPercentage() * 10);
        mBinding.includeBottomBar.tvTime.setText(String.format("%s/%s", generateTime(position), generateTime(duration)));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mDragging = false;
        mPlayer.seekTo(seekBar.getProgress() * mPlayer.getDuration() / 1000);
        mBoard.show();
    }
}
