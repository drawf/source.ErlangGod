package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.NetUtils;

/**
 * Created by drawf on 2017/5/19.
 * ------------------------------
 * 首次播放、网络状态变化的时候
 * 1. 判读是否要弹警告
 * 2. 弹出之后暂停播放
 * 3. 根据用户选择做操作
 */

public class CellularWarning extends BaseVideoPlayerPlugin {

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case NetStateMonitor.ACTION_ON_NET_STATE_CHANGED:
                working();
                break;
        }
    }

    @Override
    public void onPreparedListener(PLMediaPlayer plMediaPlayer) {
        super.onPreparedListener(plMediaPlayer);
        LogUtils.trace("on prepared");
        working();
    }

    private boolean shouldWarning() {
        //读取用户设置偏好 UserPreferences.getBoolean(UserPreferences.KEY_CELLULAR_VIDEO_OPERATION_ENABLE);
        //当前是否为非WIFI状态
        return !NetUtils.getType().equals(NetUtils.TYPE_WIFI);
    }

    private void working() {
        if (!shouldWarning()) return;

        doAction(PlayButton.ACTION_DO_PAUSE);
        new AlertDialog.Builder(mContext)
                .setTitle("网络提示")
                .setMessage("当前使用的2G/3G/4G网络，是否继续播放")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doAction(PlayButton.ACTION_DO_PLAY);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doAction(CloseButton.ACTION_DO_CLOSE);
                    }
                })
                .show();
    }

    // TODO: drawf 2017/5/22 process user preferences 

}
