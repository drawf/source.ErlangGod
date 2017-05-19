package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by drawf on 2017/5/19.
 * ------------------------------
 */

public class NetStateMonitor extends BaseVideoPlayerPlugin {
    public static final int ACTION_ON_NET_STATE_CHANGED = BASE_ACTION_NET_STATE_MONITOR;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doAction(ACTION_ON_NET_STATE_CHANGED);
        }
    };
    private IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    @Override
    public void onLifeResume(Context context) {
        super.onLifeResume(context);
        mContext.registerReceiver(receiver, filter);
    }

    @Override
    public void onLifePause(Context context) {
        super.onLifePause(context);
        mContext.unregisterReceiver(receiver);
    }
}
