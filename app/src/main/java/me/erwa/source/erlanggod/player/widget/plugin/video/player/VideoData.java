package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.Map;

import me.erwa.source.erlanggod.utils.Mapper;

/**
 * Created by drawf on 2017/5/17.
 * ------------------------------
 */

public class VideoData extends BaseVideoPlayerPlugin {

    public static final int ACTION_FETCH_INFO = BASE_ACTION_VIDEO_INFO + 30;
    public static final int ACTION_FETCH_NAME = BASE_ACTION_VIDEO_INFO + 31;
    public static final int ACTION_FETCH_INTERACTIONS = BASE_ACTION_VIDEO_INFO + 32;

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
            default:
                return null;
        }
    }
}
