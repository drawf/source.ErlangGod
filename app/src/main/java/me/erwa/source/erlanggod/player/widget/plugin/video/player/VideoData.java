package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.Map;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.Mapper;

/**
 * Created by drawf on 2017/5/17.
 * ------------------------------
 */

public class VideoData extends BaseVideoPlayerPlugin {

    public static final int ACTION_ON_FETCH_INFO = BASE_ACTION_VIDEO_INFO + 30;
    public static final int ACTION_ON_FETCH_VIDEO_NAME = BASE_ACTION_VIDEO_INFO + 31;

    private Map<String, Object> data;

    public VideoData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);
    }

    @Override
    public Object onFetchData(int action) {
        switch (action) {
            case ACTION_ON_FETCH_INFO:
                return this.data;
            case ACTION_ON_FETCH_VIDEO_NAME:
                return Mapper.from(this.data).to("name").getString();
            default:
                return null;
        }
    }
}
