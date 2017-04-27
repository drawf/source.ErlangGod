package me.erwa.source.erlanggod.player.widget.plugin;

import android.support.annotation.CallSuper;

import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class BasePlugin<T> implements MediaControllerBoard.IPlugin<T> {

    protected MediaControllerBoard mBoard;
    protected IMediaController.MediaPlayerControl mPlayer;
    protected MediaControllerBoardBinding mBinding;

    @CallSuper
    @Override
    public void init(MediaControllerBoard board) {
        this.mBoard = board;
        this.mPlayer = board.mPlayer;
        this.mBinding = board.mBinding;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @CallSuper
    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        LogUtils.d("onInfo: " + what + ", " + extra);
    }

    @CallSuper
    @Override
    public void onErrorListener(PLMediaPlayer plMediaPlayer, int errorCode) {
        boolean isNeedReconnect = false;
        LogUtils.e("Error happened, errorCode = " + errorCode);
        switch (errorCode) {
            case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                LogUtils.e("Invalid URL !");
                break;
            case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                LogUtils.e("404 resource not found !");
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                LogUtils.e("Connection refused !");
                break;
            case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                LogUtils.e("Connection timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                LogUtils.e("Empty playlist !");
                break;
            case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                LogUtils.e("Stream disconnected !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                LogUtils.e("Network IO Error !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                LogUtils.e("Unauthorized Error !");
                break;
            case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                LogUtils.e("Prepare timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                LogUtils.e("Read frame timeout !");
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
//                        setOptions(AVOptions.MEDIA_CODEC_SW_DECODE);
                isNeedReconnect = true;
                break;
            case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                LogUtils.e("unknown error !");
                break;
            default:
                LogUtils.e("unknown error !");
                break;
        }
        // Todo pls handle the error status here, reconnect or call finish()
        if (isNeedReconnect) {
//                    sendReconnectMessage();
        } else {
//                    finish();
        }
    }

    @Override
    public void onCompletionListener(PLMediaPlayer plMediaPlayer) {

    }

    @Override
    public void onPreparedListener(PLMediaPlayer plMediaPlayer) {

    }

    @Override
    public void addSubscriber(T plugin) {

    }
}
