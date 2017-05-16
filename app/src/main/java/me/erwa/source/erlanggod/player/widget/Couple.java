package me.erwa.source.erlanggod.player.widget;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 */

public class Couple {
    public Class clazz;
    public boolean bilateral;

    /**
     * @param clazz     插件
     * @param bilateral 双向订阅
     */
    private Couple(Class clazz, boolean bilateral) {
        this.clazz = clazz;
        this.bilateral = bilateral;
    }

    public static Couple in(Class clazz, boolean bilateral) {
        return new Couple(clazz, bilateral);
    }
}
