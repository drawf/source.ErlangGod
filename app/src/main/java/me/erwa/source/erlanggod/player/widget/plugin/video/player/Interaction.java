package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.List;

import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 * 交互题插件
 * 一个视频可能有多道交互题
 * 一道题可能有多个选项
 *
 * 触发交互题时，播放暂停、屏幕上只能操作交互题
 * 答题后，执行是否seek、继续播放（在线视频且非WIFI时弹窗）、还原状态
 */

public class Interaction extends BaseVideoPlayerPlugin {

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
//                LogUtils.trace("ACTION_ON_UPDATE_PROGRESS:" + mPlayer.getCurrentPosition());
                foo();
                break;
        }
    }

    private void foo() {
        List list = fetchData(VideoData.ACTION_FETCH_INTERACTIONS);
        LogUtils.trace(list);
    }

    private void triggerShow() {

    }


}

