package me.erwa.source.erlanggod.player;

import com.pili.pldroid.player.AVOptions;

/**
 * Created by drawf on 2017/3/22.
 * ------------------------------
 */

public class OptionsManager {

    public static OptionsManager newInstance() {
        return new OptionsManager();
    }

    private AVOptions options;

    private OptionsManager() {
        this.options = new AVOptions();

        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        // 默认值是：无
        // 现在默认值为 10 * 1000
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        // 读取视频流超时时间，单位是 ms
        // 默认值是：10 * 1000
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
        // 默认值是：0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);

        // 是否开启"延时优化"，只在在线直播流中有效
        // 默认值是：0
        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 0);

        // 默认的缓存大小，单位是 ms
        // 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：4000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        // 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

        // 播放前最大探测流的字节数，单位是 byte
        // 默认值是：128 * 1024 
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);

        // 请在开始播放之前配置
        // mVideoView.setAVOptions(options);
    }

    public AVOptions build() {
        return this.options;
    }

    public OptionsManager setMediaCodec(int codec) {
        this.options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        return this;
    }

    public OptionsManager setPrepareTimeout(int time) {
        this.options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, time);
        return this;
    }

    public OptionsManager setReadStreamTimeout(int time) {
        this.options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, time);
        return this;
    }

    public OptionsManager setLiveStreaming(boolean b) {
        this.options.setInteger(AVOptions.KEY_LIVE_STREAMING, b ? 1 : 0);
        return this;
    }

    public OptionsManager setLiveDelayOptimization(boolean b) {
        this.options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, b ? 1 : 0);
        return this;
    }

    public OptionsManager setCacheBufferDuration(int time) {
        this.options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, time);
        return this;
    }

    public OptionsManager setMaxCacheBufferDuration(int time) {
        this.options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, time);
        return this;
    }

    public OptionsManager setAutoStart(boolean b) {
        this.options.setInteger(AVOptions.KEY_START_ON_PREPARED, b ? 1 : 0);
        return this;
    }

    public OptionsManager setProbesize(int i) {
        this.options.setInteger(AVOptions.KEY_PROBESIZE, i);
        return this;
    }
}
