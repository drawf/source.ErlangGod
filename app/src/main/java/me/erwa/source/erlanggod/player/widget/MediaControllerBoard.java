package me.erwa.source.erlanggod.player.widget;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.pili.pldroid.player.IMediaController;

import java.util.ArrayList;
import java.util.List;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.MediaControllerBoardBinding;

/**
 * Created by drawf on 2017/3/24.
 * ------------------------------
 */

public class MediaControllerBoard extends FrameLayout implements IMediaController, View.OnClickListener {

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

    public Context mContext;
    public AudioManager mAM;
    public MediaPlayerControl mPlayer;
    public MediaControllerBoardBinding mBinding;
    private ViewGroup mAnchor;

    private boolean mInitFromXML;//暂不支持从xml初始化
    private List<IPlugin> mPlugins = new ArrayList<>();

    private boolean mOperationShowing;
    private static int sOperationTimeout = 5000;
    private static final int FLAG_OPERATION_HIDE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_OPERATION_HIDE:
                    hide();
                    break;
            }
        }
    };

    public void removeOperationHide() {
        mHandler.removeMessages(FLAG_OPERATION_HIDE);
    }

    private void init(@NonNull Context context) {
        mContext = context;
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    //实现接口方法
    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
        mPlayer = mediaPlayerControl;
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

        //trigger hide when timeout
        if (timeout > 0) {
            mHandler.removeMessages(FLAG_OPERATION_HIDE);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FLAG_OPERATION_HIDE), timeout);
        }

        triggerPluginShow();
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

        triggerPluginHide();
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
        this.mAnchor = (ViewGroup) view;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.mAnchor.addView(this, layoutParams);

        if (!mInitFromXML) {
            removeAllViews();
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                    R.layout.media_controller_board, null, false);
            addView(mBinding.getRoot(), layoutParams);

            initPlugins();
        }
    }

    private void initPlugins() {
        mBinding.includeTopBar.ibBack.setOnClickListener(this);

        triggerPluginInit();
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == mBinding.includeTopBar.ibBack.getId()) {
            onBackClick();
        }
    }

    private void onBackClick() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finish();
        }
    }

    public interface IPlugin {
        void init(MediaControllerBoard board);

        void onShow();

        void onHide();
    }

    public void addPlugin(IPlugin plugin) {
        mPlugins.add(plugin);
    }

    private void triggerPluginInit() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.init(this);
            }
        }
    }

    private void triggerPluginShow() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onShow();
            }
        }
    }

    private void triggerPluginHide() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onHide();
            }
        }
    }


}
