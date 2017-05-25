package me.erwa.source.erlanggod.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by drawf on 2017/5/25.
 * ------------------------------
 */

public class PlayerUtils {

    /**
     * 获取竖屏播放器的高度
     *
     * @param context
     * @return
     */
    public static int getTinyHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);

        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）

        return (int) (width * 1f / 16 * 9);
    }
}
