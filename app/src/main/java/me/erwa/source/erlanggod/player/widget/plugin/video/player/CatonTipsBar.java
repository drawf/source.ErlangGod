package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.pili.pldroid.player.PLMediaPlayer;

/**
 * Created by drawf on 2017/5/24.
 * ------------------------------
 * 卡顿提示
 */

public class CatonTipsBar extends BaseVideoPlayerPlugin {

    private boolean noTips = false;//本次播放不再提示
    private static final int FLAG_CATON_TIME_OUT = 1;
    private static final int CATON_TIME_OUT = 4 * 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_CATON_TIME_OUT:
                    working();
                    break;
            }
        }
    };

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeCatonTips.container.setVisibility(View.GONE);
        mBinding.includeCatonTips.tvNoTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noTips = true;
                mBinding.includeCatonTips.container.setVisibility(View.GONE);
            }
        });

        mBinding.includeCatonTips.tvLowMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction(QualityMode.ACTION_DO_SWITCH_MODE_LOW);
                mBinding.includeCatonTips.container.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        super.onInfoListener(plMediaPlayer, what, extra);
        switch (what) {
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                mHandler.sendEmptyMessageDelayed(FLAG_CATON_TIME_OUT, CATON_TIME_OUT);
                break;
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                mHandler.removeMessages(FLAG_CATON_TIME_OUT);
                mBinding.includeCatonTips.container.setVisibility(View.GONE);
                break;
        }
    }

    private void working() {
        if (!noTips) {
            String quality = (String) fetchData(QualityMode.ACTION_FETCH_QUALITY_MODE);
            mBinding.includeCatonTips.container.setVisibility(View.VISIBLE);
            if (quality.equals("low")) {
                mBinding.includeCatonTips.tvLowMode.setVisibility(View.GONE);
                mBinding.includeCatonTips.tvTips.setText("卡住了？请确保网络良好或尝试调整进度。");
            } else {
                mBinding.includeCatonTips.tvLowMode.setVisibility(View.VISIBLE);
                mBinding.includeCatonTips.tvTips.setText("洋葱君卡住了，建议切换到流畅模式。");
            }
        }
    }

}
