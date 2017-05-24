package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerTopBarQualityBinding;
import me.erwa.source.erlanggod.utils.NetUtils;

/**
 * Created by drawf on 2017/5/22.
 * ------------------------------
 * 清晰度切换
 */

public class QualityMode extends BaseVideoPlayerPlugin implements View.OnClickListener {

    public static final int ACTION_ON_SWITCH_QUALITY_MODE = BASE_ACTION_QUALITY_MODE + 10;

    public static final int ACTION_DO_SWITCH_MODE_LOW = BASE_ACTION_QUALITY_MODE + 20;
    public static final int ACTION_DO_SWITCH_MODE_MIDDLE = BASE_ACTION_QUALITY_MODE + 21;
    public static final int ACTION_DO_ENABLED = BASE_ACTION_QUALITY_MODE + 22;
    public static final int ACTION_DO_DISABLED = BASE_ACTION_QUALITY_MODE + 23;

    public static final int ACTION_FETCH_QUALITY_MODE = BASE_ACTION_QUALITY_MODE + 30;

    @Override
    public void doInit() {
        super.doInit();
        //数据初始化式根据当前网络状态 确定播放的清晰度
        qualityMode = NetUtils.getType().equals(NetUtils.TYPE_WIFI) ? "middle" : "low";
        makePop();
        mBinding.includeTopBar.tvQuality.setOnClickListener(this);
    }

    private int white40Alpha;
    private MediaControllerTopBarQualityBinding qualityBinding;
    private PopupWindow pop;
    private String qualityMode = "low";

    private void makePop() {
        white40Alpha = mContext.getResources().getColor(R.color.white_40alpha);
        qualityBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.media_controller_top_bar_quality, null, false);
        pop = new PopupWindow(qualityBinding.getRoot(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setOutsideTouchable(true);
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));

        qualityBinding.tvLow.setOnClickListener(this);
        qualityBinding.tvMiddle.setOnClickListener(this);
    }

    private void showPop(View view) {

        if (qualityMode.equals("low")) {
            qualityBinding.tvLow.setTextColor(white40Alpha);
            qualityBinding.tvMiddle.setTextColor(Color.WHITE);
        } else {
            qualityBinding.tvLow.setTextColor(Color.WHITE);
            qualityBinding.tvMiddle.setTextColor(white40Alpha);
        }

        pop.showAsDropDown(view);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == qualityBinding.tvLow.getId()) {
            doSwitchLow();
        } else if (id == qualityBinding.tvMiddle.getId()) {
            doSwitchMiddle();
        } else if (id == mBinding.includeTopBar.tvQuality.getId()) {
            showPop(view);
        }
    }

    @Override
    public Object replyFetchData(int action) {
        switch (action) {
            case ACTION_FETCH_QUALITY_MODE:
                return qualityMode;
            default:
                return null;
        }
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case OperationBar.ACTION_ON_HIDE:
                pop.dismiss();
                break;
            case ACTION_DO_SWITCH_MODE_LOW:
                doSwitchLow();
                break;
            case ACTION_DO_SWITCH_MODE_MIDDLE:
                doSwitchMiddle();
                break;
            case ACTION_DO_ENABLED:
                doEnabled();
                break;
            case ACTION_DO_DISABLED:
                doDisabled();
                break;
        }
    }

    private void doSwitchLow() {
        if (!qualityMode.equals("low")) {
            mBinding.includeTopBar.tvQuality.setText("流畅");
            qualityMode = "low";
            doAction(ACTION_ON_SWITCH_QUALITY_MODE);
            pop.dismiss();
        }
    }

    private void doSwitchMiddle() {
        if (!qualityMode.equals("middle")) {
            mBinding.includeTopBar.tvQuality.setText("高清");
            qualityMode = "middle";
            doAction(ACTION_ON_SWITCH_QUALITY_MODE);
            pop.dismiss();
        }
    }

    private void doEnabled() {
        mBinding.includeTopBar.tvQuality.setVisibility(View.VISIBLE);
    }

    private void doDisabled() {
        mBinding.includeTopBar.tvQuality.setVisibility(View.GONE);
    }
}
