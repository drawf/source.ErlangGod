package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

/**
 * Created by drawf on 2017/5/24.
 * ------------------------------
 * 练习题提示
 */

public class PracticeTipsBar extends BaseVideoPlayerPlugin {

    private static final int FLAG_PRACTICE_TIPS_HIDE = 1;
    private static final int PRACTICE_TIME_OUT = 10 * 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_PRACTICE_TIPS_HIDE:
                    mBinding.includePracticeTips.container.setVisibility(View.GONE);
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
        mBinding.includePracticeTips.container.setVisibility(View.GONE);
        mBinding.includePracticeTips.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.includePracticeTips.container.setVisibility(View.GONE);
            }
        });
    }


    private void working() {
        //判断是否需要显示
        int play = (int) fetchData(PlayerController.ACTION_FETCH_CURRENT_PLAY);
        if (play == PlayerController.CURRENT_PLAY_NORMAL) {
            mBinding.includePracticeTips.container.setVisibility(View.VISIBLE);
            mHandler.removeMessages(FLAG_PRACTICE_TIPS_HIDE);
            mHandler.sendEmptyMessageDelayed(FLAG_PRACTICE_TIPS_HIDE, PRACTICE_TIME_OUT);
        }
    }

}
