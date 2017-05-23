package me.erwa.source.erlanggod.player.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.AudioManager;
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
import java.util.Collections;
import java.util.List;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.player.widget.plugin.BasePlugin;
import me.erwa.source.erlanggod.utils.LogUtils;
import me.erwa.source.erlanggod.utils.ToastUtils;

/**
 * Created by drawf on 2017/3/24.
 * ------------------------------
 */

public class MediaControllerBoard extends FrameLayout implements IMediaController {

    public static final int COMMAND_DO_SHOW = 110 * 1000;
    public static final int COMMAND_DO_HIDE = 111 * 1000;


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
    public PLVideoView mVideoView;
    // TODO: drawf 2017/5/16 考虑将状态数据封装起来
    public boolean mIsPlaying;
    public boolean mOperationShowing;

    private ViewGroup mAnchor;
    private int mLayoutId;
    private boolean mInitFromXML;//暂不支持从xml初始化
    private List<IPlugin> mPlugins = new ArrayList<>();

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

    //将默认控制转移到OperationBar控制
    @Override
    public void show() {
        show(0);
    }

    @Override
    public void show(int timeout) {
        triggerPluginOnAction(COMMAND_DO_SHOW);
    }

    @Override
    public void hide() {
        triggerPluginOnAction(COMMAND_DO_HIDE);
    }

    public void onLifePause() {
        triggerPluginLifePause(mContext);
    }

    public void onLifeResume() {
        triggerPluginLifeResume(mContext);
    }

    // TODO: drawf 2017/5/16 事件优先级
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
        if (this.mVideoView == null) {

            this.mVideoView = (PLVideoView) mAnchor.findViewById(R.id.video_view);

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
    }

    private void initListener() {
        mVideoView.setOnInfoListener(new PLMediaPlayer.OnInfoListener() {
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

        mVideoView.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
                triggerPluginOnErrorListener(plMediaPlayer, errorCode);
                // Return true means the error has been handled
                // If return false, then `onCompletion` will be called
                return true;
            }
        });

        mVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer plMediaPlayer) {
                triggerPluginOnCompletionListener(plMediaPlayer);

                LogUtils.trace("onCompletion:" + mVideoView.getCurrentPosition() + "==>" + mVideoView.getDuration());
                ToastUtils.show("播放完毕");
            }
        });

        mVideoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(PLMediaPlayer plMediaPlayer) {
                triggerPluginOnPreparedListener(plMediaPlayer);
            }
        });

        mVideoView.setOnSeekCompleteListener(new PLMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
                triggerPluginOnSeekCompleteListener(plMediaPlayer);
            }
        });
    }

    private void initPlugins() {
        triggerPluginInit();
    }

    public interface IPlugin extends Comparable {
        void init(MediaControllerBoard board);

        void doInit();

        void onInfoListener(PLMediaPlayer plMediaPlayer, int what, int extra);

        void onErrorListener(PLMediaPlayer plMediaPlayer, int errorCode);

        void onCompletionListener(PLMediaPlayer plMediaPlayer);

        void onPreparedListener(PLMediaPlayer plMediaPlayer);

        void onSeekComplete(PLMediaPlayer plMediaPlayer);

        void onLifePause(Context context);

        //最先执行，注意此时还未init
        void onLifeResume(Context context);

        boolean onTouchEvent(MotionEvent event);

        void doAction(int action);

        void onAction(int action);

        Object fetchData(int action);

        Object replyFetchData(int action);
    }

    /**
     * @param plugin 要添加的插件
     */
    public void addPlugin(IPlugin plugin) {
        mPlugins.add(plugin);
        Collections.sort(mPlugins);
    }

    public void addPlugin(IPlugin plugin, int weight) {
        ((BasePlugin) plugin).setWeight(weight);
        addPlugin(plugin);
    }

    private void triggerPluginInit() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.init(this);
            }
            triggerPluginDoInit();
        }
    }

    private void triggerPluginDoInit() {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.doInit();
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

    private void triggerPluginOnSeekCompleteListener(PLMediaPlayer plMediaPlayer) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onSeekComplete(plMediaPlayer);
            }
        }
    }

    private void triggerPluginLifePause(Context context) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onLifePause(context);
            }
        }
    }

    private void triggerPluginLifeResume(Context context) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onLifeResume(context);
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

    public void triggerPluginOnAction(int action) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                p.onAction(action);
            }
        }
    }

    public Object triggerPluginOnFetchData(int action) {
        if (!mPlugins.isEmpty()) {
            for (IPlugin p : mPlugins) {
                Object o = p.replyFetchData(action);
                if (o != null) {
                    return o;
                }
            }
        }
        return null;
    }

}
