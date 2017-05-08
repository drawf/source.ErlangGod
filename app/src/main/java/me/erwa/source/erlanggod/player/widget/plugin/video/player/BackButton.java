package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.app.Activity;
import android.view.View;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class BackButton extends BaseVideoPlayerPlugin {

    public static BackButton newInstance() {
        return new BackButton();
    }

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);

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
