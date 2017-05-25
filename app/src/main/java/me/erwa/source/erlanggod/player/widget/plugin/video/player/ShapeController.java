package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.res.Configuration;

/**
 * Created by drawf on 2017/5/25.
 * ------------------------------
 */

public class ShapeController extends BaseVideoPlayerPlugin {


    @Override
    public void doInit() {
        super.doInit();
        onLifeConfigurationChanged(mContext.getResources().getConfiguration());
    }

    @Override
    public void onLifeConfigurationChanged(Configuration newConfig) {
        super.onLifeConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            doAction(QualityMode.ACTION_DO_ENABLED);
        } else {
            doAction(QualityMode.ACTION_DO_DISABLED);
        }

    }
}
