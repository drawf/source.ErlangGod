package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 2017/5/9.
 * ------------------------------
 */

public class StatePanel extends BaseVideoPlayerPlugin<StatePanel.IStatePanel> {

    public static StatePanel newInstance() {
        return new StatePanel();
    }

    private GestureDetector mGestureDetector;
    private List<IStatePanel> mSubscribers = new ArrayList<>();

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
        mGestureDetector.onTouchEvent(event);
        return true;//should be true
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

            mBinding.includeStatePanel.tvDuration.setVisibility(View.GONE);
            viewFadeInAnim(mBinding.includeStatePanel.container);
            mHandler.removeMessages(FLAG_STATE_PANEL_HIDE);

            if (mBoard.mIsPlaying) {
                mHandler.sendEmptyMessageDelayed(FLAG_STATE_PANEL_HIDE, sTimeout);
                mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_play);
            } else {
                mBinding.includeStatePanel.ivState.setImageResource(R.drawable.ic_media_controller_state_pause);
            }

            return true;
        }
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

    public interface IStatePanel {
        void togglePlayPause();
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
        }
    }
}
