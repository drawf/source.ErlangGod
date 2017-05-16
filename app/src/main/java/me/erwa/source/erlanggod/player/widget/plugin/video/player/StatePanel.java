package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pili.pldroid.player.PLMediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 2017/5/9.
 * ------------------------------
 */

public class StatePanel extends BaseVideoPlayerPlugin<IStatePanel> implements IPlayButton {

    public static StatePanel newInstance() {
        return new StatePanel();
    }

    private GestureDetector mGestureDetector;
    private MyGestureListener mMyGestureListener;
    private List<IStatePanel> mSubscribers = new ArrayList<>();

    private static int sTimeout = 1500;
    private static final int FLAG_STATE_PANEL_HIDE = 0;
    private static final int FLAG_VOLUME_BRIGHTNESS_HIDE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case FLAG_STATE_PANEL_HIDE:
                    viewFadeOutAnim(mBinding.includeStatePanel.container);
                    break;
                case FLAG_VOLUME_BRIGHTNESS_HIDE:
                    viewFadeOutAnim(mBinding.includeStatePanel.containerBrightness);
                    viewFadeOutAnim(mBinding.includeStatePanel.containerVolume);
                    break;
            }

        }
    };

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        mMyGestureListener = new MyGestureListener();
        mGestureDetector = new GestureDetector(mContext, mMyGestureListener);
        mBoard.show();//must call show
        mBinding.includeStatePanel.container.setVisibility(View.GONE);
        mBinding.includeStatePanel.containerBrightness.setVisibility(View.GONE);
        mBinding.includeStatePanel.containerVolume.setVisibility(View.GONE);

        currentVolume = mBoard.mAM.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return true;//should be true
    }

    @Override
    public void onPlay() {
        mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
        viewFadeInAnim(mBinding.includeStatePanel.container);
        mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

        mHandler.sendEmptyMessageDelayed(FLAG_STATE_PANEL_HIDE, sTimeout);
        mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_play);
    }

    @Override
    public void onPause() {
        mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
        viewFadeInAnim(mBinding.includeStatePanel.container);
        mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

        mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_pause);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private int slop;
        public boolean toSeek, toVorB;

        public MyGestureListener() {
            this.slop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }

        //must override PLVideoView toggleShowHide
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mBoard.toggleShowHide();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //playing state control
            triggerPluginTogglePlayPause();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float d = Math.abs(distanceX) - Math.abs(distanceY);
            if (Math.abs(d) > slop) {
                toSeek = d >= 0;
                toVorB = d < 0;

                if (toSeek && currentPosition == -1) currentPosition = mPlayer.getCurrentPosition();
            }
            LogUtils.trace("%s,%s,%s,%s,%s", mBoard.getHeight(), distanceY, toSeek, slop, "iii");

            if (toSeek) {
                onProgressSlide(-distanceX / mBoard.getWidth());
            } else if (toVorB && Math.abs(distanceY) >= 1) {

                boolean toVolume = e1.getX() >= mBoard.getWidth() * 0.5f;
                float percent = distanceY / (mBoard.getHeight() / 2);
                if (toVolume) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private int tempProgress;
    private long currentPosition = -1;
    private long newPosition = -1;
    private boolean isSeekEnd;

    private void onProgressSlide(float percent) {
        long duration = mPlayer.getDuration();
        tempProgress += percent * duration / 2;
//        LogUtils.trace("%s,%s,%s,%s", percent, tempProgress, "ppp", currentPosition);
        if (Math.abs(tempProgress) < 100) return;

        isSeekEnd = false;
        newPosition = tempProgress + currentPosition;
        if (newPosition < 0 && !isSeekEnd) {
            newPosition = 0;
        } else {
            newPosition = (duration - newPosition) < 9000 ? duration - 9000 : newPosition;
        }

        currentPosition = newPosition;
        mBinding.includeStatePanel.tvDuration.setVisibility(View.VISIBLE);
        if (mBoard.mIsPlaying) {
            viewFadeInAnim(mBinding.includeStatePanel.container);
            mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);
            mHandler.sendEmptyMessageDelayed(FLAG_STATE_PANEL_HIDE, sTimeout);
        }

        mBinding.includeStatePanel.tvDuration.setText(generateTime(newPosition));
        mBinding.includeStatePanel.ivState.setImageResource(percent >= 0
                ? R.drawable.ic_media_controller_state_fast_forward
                : R.drawable.ic_media_controller_state_rewind);

        tempProgress = 0;
    }

    private float tempVolume;
    private float currentVolume;


    private void onVolumeSlide(float percent) {
        int maxVolume = mBoard.mAM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        tempVolume += maxVolume * percent;

//        LogUtils.trace("%s,%s,%s,%s", tempVolume, percent, "vvv", tempVolume + currentVolume);

        if (Math.abs(tempVolume) < 0.01) return;

        float newVolume = tempVolume + currentVolume;
        if (newVolume <= 0) {
            newVolume = 0;
        } else {
            newVolume = newVolume > maxVolume ? maxVolume : newVolume;
        }

        viewFadeInAnim(mBinding.includeStatePanel.containerVolume);
        mHandler.removeMessages(FLAG_VOLUME_BRIGHTNESS_HIDE);
        mHandler.sendEmptyMessageDelayed(FLAG_VOLUME_BRIGHTNESS_HIDE, sTimeout);

        currentVolume = newVolume;
        mBoard.mAM.setStreamVolume(AudioManager.STREAM_MUSIC, (int) newVolume, 0);

        mBinding.includeStatePanel.pbVolume.setMax(maxVolume * 100);
        mBinding.includeStatePanel.pbVolume.setProgress((int) (newVolume * 100));
        tempVolume = 0;
//        LogUtils.trace("%s,%s,%s,%s", currentVolume, (int) newVolume, (int) (newVolume * 100), "mmm");
    }

    private float tempBrightness;
    private static final float LIGHT_THRESHOLD = 0f;

    private void onBrightnessSlide(float percent) {
        tempBrightness += WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL * percent;

//        LogUtils.trace("%s,%s,%s", tempBrightness, percent, "ppp");

        if (Math.abs(tempBrightness) < 0.01) return;

        if (mContext instanceof Activity) {
            viewFadeInAnim(mBinding.includeStatePanel.containerBrightness);
            mHandler.removeMessages(FLAG_VOLUME_BRIGHTNESS_HIDE);
            mHandler.sendEmptyMessageDelayed(FLAG_VOLUME_BRIGHTNESS_HIDE, sTimeout);

            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = tempBrightness + layoutParams.screenBrightness;

            if (layoutParams.screenBrightness > WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL) {
                layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            } else if (layoutParams.screenBrightness < WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF + LIGHT_THRESHOLD) {
                layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF + LIGHT_THRESHOLD;
            }
            window.setAttributes(layoutParams);

            mBinding.includeStatePanel.pbBrightness.setMax(1000);
            mBinding.includeStatePanel.pbBrightness.setProgress((int) (layoutParams.screenBrightness * 1000));
        }
        tempBrightness = 0;
    }

    private void endGesture() {
        if (newPosition >= 0) {
            mPlayer.seekTo(newPosition);
        }

        mMyGestureListener.toSeek = false;
        mMyGestureListener.toVorB = false;
    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        super.onSeekComplete(plMediaPlayer);
        isSeekEnd = true;
        newPosition = -1;
        currentPosition = -1;
        if (!mBoard.mIsPlaying) {
            mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_pause);
            mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
        }
    }

    private String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private Animation mFadeInAnim;
    private Animation mFadeOutAnim;

    private void viewFadeInAnim(final View view) {
        if (mFadeInAnim == null) {
            mFadeInAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            mFadeInAnim.setDuration(200);
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.startAnimation(mFadeInAnim);
        }
        view.setVisibility(View.VISIBLE);
    }

    private void viewFadeOutAnim(final View view) {
        if (mFadeOutAnim == null) {
            mFadeOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
            mFadeOutAnim.setDuration(200);
        }
        if (view.getVisibility() != View.GONE) {
            view.startAnimation(mFadeOutAnim);
        }
        view.setVisibility(View.GONE);
    }


    @Override
    public void addSubscriber(IStatePanel plugin) {
        super.addSubscriber(plugin);
        mSubscribers.add(plugin);
    }

    private void triggerPluginTogglePlayPause() {
        if (!mSubscribers.isEmpty()) {
            for (IStatePanel p : mSubscribers) {
                p.togglePlayPause();
            }
        } else {
            LogUtils.w("togglePlayPause has no subscriber!");
        }
    }
}

//双向订阅时不能定义内部接口
interface IStatePanel extends IPlayButtonReverse {
}
