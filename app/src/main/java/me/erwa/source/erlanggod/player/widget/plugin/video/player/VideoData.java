package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.Map;

import me.erwa.source.erlanggod.utils.Mapper;

/**
 * Created by drawf on 2017/5/17.
 * ------------------------------
 * 视频数据
 */

public class VideoData extends BaseVideoPlayerPlugin {

    public static final int ACTION_FETCH_INFO = BASE_ACTION_VIDEO_INFO + 30;
    public static final int ACTION_FETCH_NAME = BASE_ACTION_VIDEO_INFO + 31;
    public static final int ACTION_FETCH_INTERACTIONS = BASE_ACTION_VIDEO_INFO + 32;
    public static final int ACTION_FETCH_CURRENT_QUALITY_URL = BASE_ACTION_VIDEO_INFO + 33;
    public static final int ACTION_FETCH_END_URL = BASE_ACTION_VIDEO_INFO + 34;

    private Map<String, Object> data;

    public VideoData(Map<String, Object> data) {
        this.data = data;
    }


    @Override
    public Object replyFetchData(int action) {
        switch (action) {
            case ACTION_FETCH_INFO:
                return this.data;
            case ACTION_FETCH_NAME:
                return Mapper.from(this.data).to("name").getString();
            case ACTION_FETCH_INTERACTIONS:
                return Mapper.from(this.data).to("interactions").getList();
            case ACTION_FETCH_CURRENT_QUALITY_URL:// TODO: drawf 2017/5/22 check replace url host
                String qualityMode = (String) fetchData(QualityMode.ACTION_FETCH_QUALITY_MODE);
                Object[] to = new Object[]{"url", "mobile", qualityMode.equals("low") ? "hls_low" : "hls_middle"};
                return Mapper.from(this.data).to(to).getString();
            case ACTION_FETCH_END_URL:
                return "https://o558dvxry.qnssl.com/mobileM/mobileM_5860b7ca452a0a6a19061084.m3u8";
            default:
                return null;
        }
    }

}
