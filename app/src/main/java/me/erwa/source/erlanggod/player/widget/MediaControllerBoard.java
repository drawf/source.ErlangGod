package me.erwa.source.erlanggod.player.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.ToastUtils;

/**
 * Created by drawf on 2017/3/24.
 * ------------------------------
 */

public class MediaControllerBoard extends FrameLayout implements IMediaController {

    public MediaControllerBoard(@NonNull Context context, @NonNull int layoutId) {
        super(context);
        init(context, layoutId);
    }

    public MediaControllerBoard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaControllerBoard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInitFromXML = true;
        init(context, 0);
    }

    public Context mContext;
    public AudioManager mAM;
    public MediaPlayerControl mPlayer;
    public ViewDataBinding mBinding;
    public boolean mIsPlaying;

    private ViewGroup mAnchor;
    private int mLayoutId;
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

    private void init(@NonNull Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
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
            mOperationShowing = false;
        }

        triggerPluginHide();
    }

    public void toggleShowHide() {
        if (mOperationShowing) {
            hide();
        } else {
            show();
        }
    }

    public void onPause() {
        triggerPluginLifePause();
    }

    public void onResume() {
        triggerPluginLifeResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return triggerPluginTouchEvent(event) || super.onTouchEvent(event);
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
        if (this.getParent() != null) {
            ViewGroup parent = (ViewGroup) this.getParent();
            parent.removeView(this);
        }
        this.mAnchor.addView(this, layoutParams);

        if (!mInitFromXML) {
            removeAllViews();
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), mLayoutId, null, false);
            addView(mBinding.getRoot(), layoutParams);

            initPlugins();
            initListener();

        } else {
            throw new RuntimeException("I don't support init from XML!");
        }
    }

    private void initListener() {
        final PLVideoView videoView = (PLVideoView) this.mAnchor.findViewById(R.id.video_view);

        videoView.setOnInfoListener(new PLMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
                switch (what) {
                    case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        mIsPlaying = true;
                        break;
                }
                triggerPluginOnInfoListener(plMediaPlayer, what, extra);
                return false;
            }
        });

        videoView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
                triggerPluginOnErrorListener(plMediaPlayer, errorCode);
                // Return true means the error has been handled
                // If return false, then `onCompletion` will be called
                return true;
            }
        });

        videoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                triggerPluginOnCompletionListener(plMediaPlayer);

                LogUtils.trace("onCompletion:" + videoView.getCurrentPosition() + "==>" + videoView.getDuration());
                ToastUtils.show("播放完毕");
            }
        });

        videoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                triggerPluginOnPreparedListener(plMediaPlayer);
            }
        });
    }

    private void initPlugins() {
        triggerPluginInit();
    }

    public interface IPlugin<P> {
        void init(MediaControllerBoard board);

        void onShow();

        void onHide();

        void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra);

        void onErrorListener(PLMediaPlayer plMediaPlayer, int errorCode);

        void onCompletionListener(PLMediaPlayer plMediaPlayer);

        void onPreparedListener(PLMediaPlayer plMediaPlayer);

        void addSubscriber(P plugin);

        void onLifePause();

        void onLifeResume();

        boolean onTouchEvent(MotionEvent event);
    }


    public void addPlugin(IPlugin plugin, Class... clazz) {
        addPlugin(false, plugin, clazz);
    }

    /**
     * 用于MediaControllerBoard添加插件
     *
     * @param bilateral 双向订阅
     * @param plugin    插件
     * @param clazz     要订阅的插件
     */
    public void addPlugin(boolean bilateral, IPlugin plugin, Class... clazz) {
        if (clazz != null && !mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                for (Class c : Arrays.asList(clazz)) {
                    if (p.getClass().equals(c)) {
                        p.addSubscriber(plugin);
                        if (bilateral) {
                            plugin.addSubscriber(p);
                        }
                    }
                }
            }
        }
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

    private void triggerPluginOnInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onInfoListener(plMediaPlayer, what, extra);
            }
        }
    }

    private void triggerPluginOnErrorListener(PLMediaPlayer plMediaPlayer, int errorCode) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onErrorListener(plMediaPlayer, errorCode);
            }
        }
    }

    private void triggerPluginOnCompletionListener(PLMediaPlayer plMediaPlayer) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onCompletionListener(plMediaPlayer);
            }
        }
    }

    private void triggerPluginOnPreparedListener(PLMediaPlayer plMediaPlayer) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onPreparedListener(plMediaPlayer);
            }
        }
    }

    private void triggerPluginLifePause() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onLifePause();
            }
        }
    }

    private void triggerPluginLifeResume() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onLifeResume();
            }
        }
    }

    private boolean triggerPluginTouchEvent(MotionEvent event) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                boolean b = p.onTouchEvent(event);
                if (b) return b;
            }
        }
        return false;
    }

}
