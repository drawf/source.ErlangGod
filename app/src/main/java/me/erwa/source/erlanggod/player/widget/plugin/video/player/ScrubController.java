package me.erwa.source.erlanggod.player.widget.plugin.video.player;

/**
 * Created by drawf on 2017/5/24.
 * ------------------------------
 * 播放主视频前做预处理
 * 播放后做扫尾处理
 */

public class ScrubController extends BaseVideoPlayerPlugin {

    public static final int ACTION_ON_PROCESS_PRE_FINISHED = BASE_ACTION_SCRUB_CONTROLLER + 10;
    public static final int ACTION_ON_PROCESS_END_FINISHED = BASE_ACTION_SCRUB_CONTROLLER + 11;

    public static final int ACTION_DO_PROCESS_PRE = BASE_ACTION_SCRUB_CONTROLLER + 20;
    public static final int ACTION_DO_PROCESS_END = BASE_ACTION_SCRUB_CONTROLLER + 21;

    public static final int ACTION_FETCH_CURRENT_URL = BASE_ACTION_SCRUB_CONTROLLER + 30;


    private String currentUrl;

    @Override
    public void doInit() {
        super.doInit();
        currentUrl = (String) fetchData(VideoData.ACTION_FETCH_CURRENT_QUALITY_URL);
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_PROCESS_PRE:
                doPre();
                break;
            case ACTION_DO_PROCESS_END:
                doEnd();
                break;
        }
    }

    @Override
    public Object replyFetchData(int action) {
        switch (action) {
            case ACTION_FETCH_CURRENT_URL:
                return currentUrl;
            default:
                return null;
        }
    }

    private void doPre() {
        //文件未下载，下载不禁用，不更换地址
        //文件下载中或排队中，下载禁用，不更换地址
        //文件下载完毕，下载禁用，更换地址

        //如果播放本地视频，做解密操作

//        doAction(DownloadButton.ACTION_DO_DISABLED);
//        doAction(QualityMode.ACTION_DO_DISABLED);

        doAction(ACTION_ON_PROCESS_PRE_FINISHED);
    }

    private void doEnd() {

        doAction(ACTION_ON_PROCESS_END_FINISHED);
    }

}
