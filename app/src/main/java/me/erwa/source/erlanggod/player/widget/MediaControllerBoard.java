package me.erwa.source.erlanggod.player.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.pili.pldroid.player.IMediaController;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;

/**
 * Created by drawf on 2017/3/24.
 * ------------------------------
 */

public class MediaControllerBoard extends FrameLayout implements IMediaController {

    public MediaControllerBoard(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MediaControllerBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaControllerBoard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context mContext;
    private AudioManager mAM;
    private View mAnchor;
    private PopupWindow mWindow;
    private MediaControllerBoardBinding mBinding;

    private boolean mInitFromXML;
    private boolean mOperationShowing;

    private static int sOperationTimeout = 5000;

    private void init(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (!mInitFromXML) {
            mWindow = new PopupWindow(mContext);
            mWindow.setFocusable(false);
            mWindow.setBackgroundDrawable(null);
            mWindow.setOutsideTouchable(true);
            mWindow.setTouchable(false);
        }
    }

    //实现接口方法
    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {

    }

    @Override
    public void show() {
        show(sOperationTimeout);
    }

    @Override
    public void show(int timeout) {
        if (!mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != VISIBLE) {
                mBinding.includeTopBar.container.setVisibility(VISIBLE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != VISIBLE) {
                mBinding.includeBottomBar.container.setVisibility(VISIBLE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
            }

            mOperationShowing = true;
        }
    }

    @Override
    public void hide() {
        if (mOperationShowing) {

            if (mBinding.includeTopBar.container.getVisibility() != GONE) {
                mBinding.includeTopBar.container.setVisibility(GONE);
                mBinding.includeTopBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
            }
            if (mBinding.includeBottomBar.container.getVisibility() != GONE) {
                mBinding.includeBottomBar.container.setVisibility(GONE);
                mBinding.includeBottomBar.container.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_bottom));
            }

            mOperationShowing = false;
        }
    }

    @Override
    public boolean isShowing() {
        return mOperationShowing;
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * <p>
     * - This can for example be a VideoView, or your Activity's main view.
     * - AudioPlayer has no anchor view, so the view parameter will be null.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    @Override
    public void setAnchorView(View view) {
        this.mAnchor = view;
        if (!mInitFromXML) {
            removeAllViews();
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                    R.layout.media_controller_board, null, false);

            mWindow.setContentView(mBinding.getRoot());
            mWindow.setWidth(LayoutParams.MATCH_PARENT);
            mWindow.setHeight(LayoutParams.MATCH_PARENT);
            mWindow.showAtLocation(mAnchor, Gravity.CENTER, 0, 0);
        }
    }
}
