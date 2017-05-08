package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;
import android.view.animation.AnimationUtils;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;


/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class OperationBar extends BaseVideoPlayerPlugin {

    public static OperationBar newInstance() {
        return new OperationBar();
    }

    private boolean mOperationShowing;

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);

    }

    @Override
    public void onShow() {
        super.onShow();
        if (!mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != View.VISIBLE) {
                mBinding.includeTopBar.container.setVisibility(View.VISIBLE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != View.VISIBLE) {
                mBinding.includeBottomBar.container.setVisibility(View.VISIBLE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
            }

            mOperationShowing = true;
        }

    }

    @Override
    public void onHide() {
        super.onHide();
        if (mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != View.GONE) {
                mBinding.includeTopBar.container.setVisibility(View.GONE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != View.GONE) {
                mBinding.includeBottomBar.container.setVisibility(View.GONE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_bottom));
            }

            mOperationShowing = false;
        }
    }
}
