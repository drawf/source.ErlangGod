package me.erwa.source.erlanggod.player.widget.plugin;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.view.MotionEvent;

import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 26/04/2017.
 * ------------------------------
 */

public class BasePlugin<B extends ViewDataBinding> implements MediaControllerBoard.IPlugin {

    protected MediaControllerBoard mBoard;
    protected Context mContext;
    protected IMediaController.MediaPlayerControl mPlayer;
    protected B mBinding;

    @CallSuper
    @Override
    public void init(MediaControllerBoard board) {
        this.mBoard = board;
        this.mContext = board.mContext;
        this.mPlayer = board.mPlayer;
        this.mBinding = (B) board.mBinding;
    }

    @Override
    public void doInit() {

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
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {

    }

    @CallSuper
    @Override
    public void onLifePause(Context context) {
        this.mContext = context;
    }

    @CallSuper
    @Override
    public void onLifeResume(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    protected static final int BASE_ACTION_PLAY_BUTTON = 100;
    protected static final int BASE_ACTION_PROGRESS_BAR = 200;
    protected static final int BASE_ACTION_OPERATION_BAR = 300;
    protected static final int BASE_ACTION_VIDEO_INFO = 400;
    protected static final int BASE_ACTION_GESTURE_PANEL = 500;
    protected static final int BASE_ACTION_CLOSE_BUTTON = 600;
    protected static final int BASE_ACTION_NET_STATE_MONITOR = 700;

    /**
     * 发出的动作命令
     *
     * @param action
     */
    @Override
    public void doAction(int action) {
        mBoard.triggerPluginOnAction(action);
    }

    /**
     * 订阅的动作命令
     *
     * @param action
     */
    @Override
    public void onAction(int action) {

    }

    /**
     * 发出单向获取数据命令
     *
     * @param action
     * @return
     */
    @Override
    public Object fetchData(int action) {
        return mBoard.triggerPluginOnFetchData(action);
    }

    /**
     * 响应数据获取命令
     *
     * @param action
     * @return
     */
    @Override
    public Object replyFetchData(int action) {
        return null;
    }

}
