package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

/**
 * Created by drawf on 2017/5/24.
 * ------------------------------
 * 当自动使用体验券播放时提示
 */

public class VipTipsBar extends BaseVideoPlayerPlugin {

    private static final int FLAG_VIP_TIPS_HIDE = 1;
    private static final int VIP_TIME_OUT = 10 * 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_VIP_TIPS_HIDE:
                    mBinding.includeVipTips.container.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        super.onInfoListener(plMediaPlayer, what, extra);
        switch (what) {
            case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                working();
                break;
        }
    }

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeVipTips.container.setVisibility(View.GONE);
        mBinding.includeVipTips.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.includeVipTips.container.setVisibility(View.GONE);
            }
        });

        mBinding.includeVipTips.tvBeVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开vip页面
            }
        });
    }


    private void working() {
        //判断是否需要显示
        int play = (int) fetchData(PlayerController.ACTION_FETCH_CURRENT_PLAY);
        if (play == PlayerController.CURRENT_PLAY_NORMAL) {
            mBinding.includeVipTips.container.setVisibility(View.VISIBLE);
            mHandler.removeMessages(FLAG_VIP_TIPS_HIDE);
            mHandler.sendEmptyMessageDelayed(FLAG_VIP_TIPS_HIDE, VIP_TIME_OUT);
        }
    }

}
