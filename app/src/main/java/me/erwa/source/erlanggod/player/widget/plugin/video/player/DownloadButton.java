package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerDownloadBinding;

/**
 * Created by drawf on 2017/5/8.
 * ------------------------------
 */

public class DownloadButton extends BaseVideoPlayerPlugin implements View.OnClickListener {

    public static final int ACTION_DO_ENABLED = BASE_ACTION_DOWNLOAD_BUTTON + 22;
    public static final int ACTION_DO_DISABLED = BASE_ACTION_DOWNLOAD_BUTTON + 23;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeTopBar.ibDownload.setOnClickListener(this);
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
        if (id == mBinding.includeTopBar.ibDownload.getId()) {
            showDialog();
        }
    }


    // TODO: drawf 2017/5/24 不再提示有没有机会再设置回来
    // TODO: drawf 2017/5/24 弹窗的style
    private void showDialog() {
        //读取用户偏好，是否忽略弹窗，直接下载
        doAction(PlayButton.ACTION_DO_PAUSE);

        final MediaControllerDownloadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.media_controller_download, null, false);

        new AlertDialog.Builder(mContext)
                .setView(binding.getRoot())
                .setPositiveButton("确认下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doAction(PlayButton.ACTION_DO_PLAY);
                        //设置用户偏好
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void doEnabled() {
        mBinding.includeTopBar.ibDownload.setVisibility(View.VISIBLE);
    }

    private void doDisabled() {
        mBinding.includeTopBar.ibDownload.setVisibility(View.GONE);
    }
}
