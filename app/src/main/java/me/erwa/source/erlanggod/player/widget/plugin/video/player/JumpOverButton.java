package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.view.View;

/**
 * Created by drawf on 2017/5/23.
 * ------------------------------
 */

public class JumpOverButton extends BaseVideoPlayerPlugin {

    public static final int ACTION_DO_ENABLED = BASE_ACTION_JUMP_OVER_BUTTON + 10;
    public static final int ACTION_DO_DISABLED = BASE_ACTION_JUMP_OVER_BUTTON + 11;

    @Override
    public void doInit() {
        super.doInit();
        mBinding.includeJumpOver.container.setVisibility(View.GONE);
        mBinding.includeJumpOver.btnJumpOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction(PlayerController.ACTION_DO_JUMP_OVER);
            }
        });
    }

    @Override
    public void onAction(int action) {
        super.onAction(action);
        switch (action) {
            case ACTION_DO_ENABLED:
                mBinding.includeJumpOver.container.setVisibility(View.VISIBLE);
                break;
            case ACTION_DO_DISABLED:
                mBinding.includeJumpOver.container.setVisibility(View.GONE);
                break;
        }
    }
}
