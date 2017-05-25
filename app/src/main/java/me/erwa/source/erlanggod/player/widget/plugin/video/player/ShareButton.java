package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;

import me.erwa.source.erlanggod.utils.ToastUtils;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class ShareButton extends BaseVideoPlayerPlugin implements View.OnClickListener {

    public static final int ACTION_DO_ENABLED = BASE_ACTION_SHARE_BUTTON + 20;
    public static final int ACTION_DO_DISABLED = BASE_ACTION_SHARE_BUTTON + 21;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeTopBar.ibShare.setOnClickListener(this);
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_ENABLED:
                doEnabled();
                break;
            case ACTION_DO_DISABLED:
                doDisabled();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBinding.includeTopBar.ibShare.getId()) {
            ToastUtils.show("点击了share");
            if (mContext instanceof Activity) {
                if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
            }
        }
    }


    private void doEnabled() {
        mBinding.includeTopBar.ibShare.setVisibility(View.VISIBLE);
    }

    private void doDisabled() {
        mBinding.includeTopBar.ibShare.setVisibility(View.GONE);
    }
}
