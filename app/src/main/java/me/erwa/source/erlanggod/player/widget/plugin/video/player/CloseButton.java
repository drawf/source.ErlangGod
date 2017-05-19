package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.app.Activity;
import android.view.View;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class CloseButton extends BaseVideoPlayerPlugin {

    public static final int ACTION_DO_CLOSE = BASE_ACTION_CLOSE_BUTTON + 10;

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_CLOSE:
                doClose();
                break;
        }
    }

    @Override
    public void doInit() {
        super.doInit();
        this.mBinding.includeTopBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClose();
            }
        });
    }

    private void doClose() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }
}
