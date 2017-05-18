package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.app.Activity;
import android.view.View;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class CloseButton extends BaseVideoPlayerPlugin {

    @Override
    public void doInit() {
        super.doInit();
        this.mBinding.includeTopBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }
        });
    }

}
