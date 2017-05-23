package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.NetUtils;

/**
 * Created by drawf on 2017/5/23.
 * ------------------------------
 */

public class ErrorHandler extends BaseVideoPlayerPlugin {

//    public static final int ACTION_FETCH_LAST_POSITION = BASE_ACTION_ERROR_HANDLER + 30;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeErrorPage.container.setVisibility(View.GONE);
    }

    @Override
    public void onCompletionListener(PLMediaPlayer plMediaPlayer) {
        super.onCompletionListener(plMediaPlayer);
        if (mPlayer.getCurrentPosition() + 9000 < mPlayer.getDuration() && !NetUtils.isConnected()) {
            onErrorListener(null, PLMediaPlayer.ERROR_CODE_IO_ERROR);
        }
    }

    @Override
    public void onErrorListener(PLMediaPlayer plMediaPlayer, int errorCode) {
        super.onErrorListener(plMediaPlayer, errorCode);
        boolean retry = false;
        String tip = "发生未知错误";

        LogUtils.e("Error happened, errorCode = " + errorCode);
        switch (errorCode) {
            case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                LogUtils.e("Invalid URL !");
                tip = "无效的播放地址";
                break;
            case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                LogUtils.e("404 resource not found !");
                tip = "（404）未找到资源";
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                LogUtils.e("Connection refused !");
                tip = "视频连接被拒绝";
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                LogUtils.e("Connection timeout !");
                tip = "视频连接超时";
                retry = true;
                break;
            case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                LogUtils.e("Empty playlist !");
                tip = "视频播放列表为空";
                break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                LogUtils.e("Stream disconnected !");
                tip = "视频流断开了";
                retry = true;
                break;
            case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                LogUtils.e("Network IO Error !");
                tip = "网络IO错误，请先检查网络";
                retry = true;
                break;
            case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                LogUtils.e("Unauthorized Error !");
                tip = "未授权错误";
                break;
            case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                LogUtils.e("Prepare timeout !");
                tip = "播放器初始化超时";
                retry = true;
                break;
            case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                LogUtils.e("Read frame timeout !");
                tip = "读取视频流超时";
                retry = true;
                break;
            case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
//                setOptions(AVOptions.MEDIA_CODEC_SW_DECODE);
//                retry = true;
                break;
            case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
            default:
                LogUtils.e("unknown error !");
                tip = "发生未知错误";
                break;
        }

        handleError(retry, tip);
    }

    private void handleError(boolean retry, String tip) {
        mBoard.mVideoView.stopPlayback();
        doAction(OperationBar.ACTION_DO_DISABLED);
        doAction(GesturePanel.ACTION_DO_DISABLED);

        mBinding.includeErrorPage.tvTip.setText(tip);
        mBinding.includeErrorPage.container.setVisibility(View.VISIBLE);
        if (retry) {
            mBinding.includeErrorPage.btnAction.setText("重试");
            mBinding.includeErrorPage.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBinding.includeErrorPage.container.setVisibility(View.GONE);
                    doAction(PlayerController.ACTION_DO_RETRY_PLAY);

                    doAction(OperationBar.ACTION_DO_ENABLED);
                    doAction(GesturePanel.ACTION_DO_ENABLED);
                }
            });
        } else {
            mBinding.includeErrorPage.btnAction.setText("退出");
            mBinding.includeErrorPage.btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doAction(CloseButton.ACTION_DO_CLOSE);
                }
            });
        }
    }
}
