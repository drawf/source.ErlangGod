package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;


/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class OperationBar extends BaseVideoPlayerPlugin {

    public static final int ACTION_ON_SHOW = BASE_ACTION_OPERATION_BAR + 1;
    public static final int ACTION_ON_HIDE = BASE_ACTION_OPERATION_BAR + 2;
    public static final int ACTION_DO_SHOW = BASE_ACTION_OPERATION_BAR + 10;
    public static final int ACTION_DO_HIDE = BASE_ACTION_OPERATION_BAR + 11;
    public static final int ACTION_DO_TOGGLE_SHOW_HIDE = BASE_ACTION_OPERATION_BAR + 12;
    public static final int ACTION_DO_REMOVE_AUTO_HIDE = BASE_ACTION_OPERATION_BAR + 13;

    public static OperationBar newInstance() {
        return new OperationBar();
    }

    private static final int sOperationTimeout = 5000;
    private static final int FLAG_OPERATION_HIDE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_OPERATION_HIDE:
                    doHide();
                    break;
            }
        }
    };

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
    }


    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_SHOW:
                doShow();
                break;
            case ACTION_DO_HIDE:
                doHide();
                break;
            case ACTION_DO_TOGGLE_SHOW_HIDE:
                doToggleShowHide();
                break;
            case ACTION_DO_REMOVE_AUTO_HIDE:
                doRemoveAutoHide();
                break;
        }
    }

    private void doShow() {
        doShow(sOperationTimeout);
    }

    private void doShow(int timeout) {
        //trigger hide when timeout
        if (timeout > 0) {
            mHandler.removeMessages(FLAG_OPERATION_HIDE);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FLAG_OPERATION_HIDE), timeout);
        }

        if (!mBoard.mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != View.VISIBLE) {
                mBinding.includeTopBar.container.setVisibility(View.VISIBLE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != View.VISIBLE) {
                mBinding.includeBottomBar.container.setVisibility(View.VISIBLE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
            }

            mBoard.mOperationShowing = true;
            doAction(ACTION_ON_SHOW);
        }

    }

    private void doHide() {
        if (mBoard.mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != View.GONE) {
                mBinding.includeTopBar.container.setVisibility(View.GONE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != View.GONE) {
                mBinding.includeBottomBar.container.setVisibility(View.GONE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_bottom));
            }

            mBoard.mOperationShowing = false;
            doAction(ACTION_ON_HIDE);
        }
    }

    private void doToggleShowHide() {
        if (mBoard.mOperationShowing) {
            doHide();
        } else {
            doShow();
        }
    }

    private void doRemoveAutoHide() {
        mHandler.removeMessages(FLAG_OPERATION_HIDE);
    }

}
