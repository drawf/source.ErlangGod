package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.List;
import java.util.Map;

import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.Mapper;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 * 交互题插件
 * 一个视频可能有多道交互题
 * 一道题可能有多个选项
 * <p>
 * 触发交互题时，播放暂停、屏幕上只能操作交互题
 * 答题后，执行是否seek、继续播放（在线视频且非WIFI时弹窗）、还原状态
 */

public class Interaction extends BaseVideoPlayerPlugin {

    private List<Map> list;

    @Override
    public void doInit() {
        super.doInit();
        list = (List<Map>) fetchData(VideoData.ACTION_FETCH_INTERACTIONS);
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
                detectInteraction();
                break;
        }
    }

    private void detectInteraction() {
        long position = mPlayer.getCurrentPosition();

        LogUtils.trace(position);

        for (Map<String, Object> map : list) {
            double point = Mapper.from(map).to("time").getDouble() * 1000;
            if (position >= point - 500 && position < point + 500) {
                showView();
            }
        }
    }

    private void showView() {
        doAction(OperationBar.ACTION_DO_DISABLED);
        doAction(GesturePanel.ACTION_DO_DISABLED);
        doAction(PlayButton.ACTION_DO_PAUSE);




        LogUtils.trace("showView");
    }

    private void hideView() {
        doAction(OperationBar.ACTION_DO_ENABLED);
        doAction(GesturePanel.ACTION_DO_ENABLED);
    }


}

