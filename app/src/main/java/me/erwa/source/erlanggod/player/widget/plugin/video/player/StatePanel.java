package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

public class StatePanel extends BaseVideoPlayerPlugin<IStatePanel> implements PlayButton.IPlayButton {

    public static StatePanel newInstance() {
        return new StatePanel();
    }

    private GestureDetector mGestureDetector;
    private List<IStatePanel> mSubscribers = new ArrayList<>();
    private long newPosition = -1;

    private static int sTimeout = 1500;
    private static final int FLAG_STATE_PANEL_HIDE = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewFadeOutAnim(mBinding.includeStatePanel.container);
        }
    };

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
        mGestureDetector = new GestureDetector(mContext, new MyGestureListener());
        mBoard.show();//must call show
        mBinding.includeStatePanel.container.setVisibility(View.GONE);
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
    public void play() {
        mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
        viewFadeInAnim(mBinding.includeStatePanel.container);
        mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

        mHandler.sendEmptyMessageDelayed(FLAG_STATE_PANEL_HIDE, sTimeout);
        mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_play);
    }

    @Override
    public void pause() {
        mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
        viewFadeInAnim(mBinding.includeStatePanel.container);
        mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

        mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_pause);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

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
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            boolean toSeek = Math.abs(distanceX) >= Math.abs(distanceY);

            if (toSeek) {
                onProgressSlide(-deltaX / mBoard.getWidth());
            }


            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void onProgressSlide(float percent) {
        long currentPosition = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        long deltaMax = duration / 3;
        long delta = (long) (deltaMax * percent);

        newPosition = delta + currentPosition;

        if (newPosition <= 0) {
            newPosition = 0;
            delta = -currentPosition;
        } else {
            newPosition = (duration - newPosition) < 9000 ? duration - 9000 : newPosition;
        }
        int showDelta = (int) (delta / 1000);
        if (showDelta != 0) {
            mBinding.includeStatePanel.tvDuration.setVisibility(View.VISIBLE);
            viewFadeInAnim(mBinding.includeStatePanel.container);
            mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

            mHandler.sendEmptyMessageDelayed(FLAG_STATE_PANEL_HIDE, sTimeout);
            mBinding.includeStatePanel.tvDuration.setText(generateTime(newPosition));
            mBinding.includeStatePanel.ivState.setImageResource(percent >= 0
                    ? R.drawable.ic_media_controller_state_fast_forward
                    : R.drawable.ic_media_controller_state_rewind);
        }
    }

    private void endGesture() {
        if (newPosition >= 0) {
            mPlayer.seekTo(newPosition);
        }
    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        super.onSeekComplete(plMediaPlayer);
        newPosition = -1;
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
            mFadeInAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.startAnimation(mFadeInAnim);
        }
    }

    private void viewFadeOutAnim(final View view) {
        if (mFadeOutAnim == null) {
            mFadeOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
            mFadeOutAnim.setDuration(200);
            mFadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (view.getVisibility() != View.GONE) {
            view.startAnimation(mFadeOutAnim);
        }
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
interface IStatePanel {
    void togglePlayPause();
}
