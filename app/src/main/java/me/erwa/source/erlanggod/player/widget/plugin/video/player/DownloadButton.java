package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import me.erwa.source.erlanggod.utils.ToastUtils;

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

    private void showDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("下载确认")
                .setMessage("开始下载知识点内的x个视频")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.show("确认下载");
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
