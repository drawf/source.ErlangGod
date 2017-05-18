package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerInteractionChoiceBinding;
import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.Mapper;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 * 交互题插件
 * 一个视频可能有多道交互题
 * 一道题可能有多个选项
 * <p>
 * 触发交互题时，播放暂停、屏幕上只能操作交互题
 * 答题后，执行是否seek、继续播放（在线视频且非WIFI时弹窗）、还原状态
 */

public class Interaction extends BaseVideoPlayerPlugin {

    private List<Map> list;
    private int choiceHeight;
    private int choiceMargin;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeInteractionPanel.container.setVisibility(View.GONE);
        choiceHeight = (int) mContext.getResources().getDimension(R.dimen.media_controller_interaction_choice_height);
        choiceMargin = (int) mContext.getResources().getDimension(R.dimen.media_controller_interaction_choice_margin);

        list = (List<Map>) fetchData(VideoData.ACTION_FETCH_INTERACTIONS);
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ProgressBar.ACTION_ON_UPDATE_PROGRESS:
                detectInteraction();
                break;
        }
    }

    private Map<String, Object> problem;

    private void detectInteraction() {
        if (list.isEmpty()) return;

        long position = mPlayer.getCurrentPosition();
        for (Map<String, Object> map : list) {
            double point = Mapper.from(map).to("time").getDouble() * 1000;
            if (position >= point - 500 && position < point + 500) {
                this.problem = map;
                showView();
            }
        }
    }

    private void showView() {
        doAction(OperationBar.ACTION_DO_DISABLED);
        doAction(GesturePanel.ACTION_DO_DISABLED);
        doAction(PlayButton.ACTION_DO_PAUSE);

        inflateChoices();
        mBinding.includeInteractionPanel.container.setVisibility(View.VISIBLE);

        LogUtils.trace("showView");
    }

    private void inflateChoices() {
        mBinding.includeInteractionPanel.containerChoices.removeAllViews();
        List<Map> choices = Mapper.from(problem).to("choices").getList();

        for (int i = 0; i < choices.size(); i++) {
            MediaControllerInteractionChoiceBinding choiceBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(mContext), R.layout.media_controller_interaction_choice, null, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, choiceHeight);
            if (i != 0) {
                lp.leftMargin = choiceMargin;
            }

            Map choice = choices.get(i);
            choiceBinding.choice.setText(Mapper.from(choice).to("body").getString());
            choiceBinding.choice.setTag(choice);
            choiceBinding.choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideView();
                    // TODO: drawf 2017/5/18  
                }
            });

            mBinding.includeInteractionPanel.containerChoices.addView(choiceBinding.getRoot(), lp);
        }
    }

    private void hideView() {
        doAction(OperationBar.ACTION_DO_ENABLED);
        doAction(GesturePanel.ACTION_DO_ENABLED);
        doAction(PlayButton.ACTION_DO_PLAY);

        mBinding.includeInteractionPanel.container.setVisibility(View.GONE);
    }


}

