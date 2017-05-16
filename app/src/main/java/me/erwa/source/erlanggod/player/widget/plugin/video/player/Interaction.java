package me.erwa.source.erlanggod.player.widget.plugin.video.player;

import java.util.ArrayList;
import java.util.List;

import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.utils.LogUtils;

/**
 * Created by drawf on 2017/5/12.
 * ------------------------------
 */

public class Interaction extends BaseVideoPlayerPlugin<IInteraction> implements IProgressBar {

    public static Interaction newInstance() {
        return new Interaction();
    }

    private List<IInteraction> mSubscribers = new ArrayList<>();

    @Override
    public void init(MediaControllerBoard board) {
        super.init(board);

    }

    @Override
    public void onProgressUpdate() {
        LogUtils.trace("onProgressUpdate");
    }

    @Override
    public void addSubscriber(IInteraction plugin) {
        super.addSubscriber(plugin);
        mSubscribers.add(plugin);
    }

    private void triggerPluginPlay() {
        if (!mSubscribers.isEmpty()) {
            for (IInteraction p : mSubscribers) {
                p.play();
            }
        }
    }

    private void triggerPluginPlause() {
        if (!mSubscribers.isEmpty()) {
            for (IInteraction p : mSubscribers) {
                p.pause();
            }
        }
    }
}

interface IInteraction extends IPlayButtonReverse {

}
