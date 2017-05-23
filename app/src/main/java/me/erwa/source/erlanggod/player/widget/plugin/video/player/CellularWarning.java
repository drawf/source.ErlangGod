package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerCellularWarningBinding;
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
        working();
    }

    private boolean shouldWarning() {
        //读取用户设置偏好 UserPreferences.getBoolean(UserPreferences.KEY_CELLULAR_VIDEO_OPERATION_ENABLE);
        //当前是否为蜂窝网络状态
        return NetUtils.getType().equals(NetUtils.TYPE_CELLULAR);
    }

    private void working() {
        if (!shouldWarning()) return;

        doAction(PlayButton.ACTION_DO_PAUSE);

        final MediaControllerCellularWarningBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.media_controller_cellular_warning, null, false);

        new AlertDialog.Builder(mContext)
                .setTitle("网络提示")
                .setIcon(R.mipmap.ic_launcher)
                .setView(binding.getRoot())
                .setPositiveButton("播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doAction(PlayButton.ACTION_DO_PLAY);
                        //设置用户偏好
//                        binding.cbAuth.isChecked()
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doAction(CloseButton.ACTION_DO_CLOSE);
                    }
                })
                .setCancelable(false)
                .show();
    }

    // TODO: drawf 2017/5/22 process user preferences

}
