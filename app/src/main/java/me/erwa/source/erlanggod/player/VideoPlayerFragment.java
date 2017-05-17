package me.erwa.source.erlanggod.player;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;

import me.erwa.source.erlanggod.R;
import me.erwa.source.erlanggod.databinding.FragmentPlayerBoardBinding;
import me.erwa.source.erlanggod.player.widget.MediaControllerBoard;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.CloseButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Download;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Interaction;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.LoadingPanel;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.OperationBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.PlayButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.ProgressBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.StatePanel;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.VideoData;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.VideoTitle;

/**
 * Created by drawf on 2017/3/22.
 * ------------------------------
 */

public class VideoPlayerFragment extends Fragment implements View.OnClickListener {

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
    }

    private FragmentPlayerBoardBinding mBinding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_board, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mBinding.videoView.setAVOptions(OptionsManager.newInstance().setAutoStart(true).build());
        mBinding.videoView.setVideoPath("https://hls.media.yangcong345.com/mobileM/mobileM_58c26cbb36eaf35866aae116.m3u8");
//        mBinding.videoView.setVideoPath("https://o558dvxry.qnssl.com/pcM/pcM_584f96fbefdf207b0822cf7a.m3u8");
        mediaControllerBoard = new MediaControllerBoard(getActivity(), R.layout.media_controller_board);
        mBinding.videoView.setMediaController(mediaControllerBoard);

        mBinding.videoView.seekTo(10000);


        String info = "{\n" +
                "        \"_id\": \"54cc72a0abc5bbb971f99bb2\",\n" +
                "        \"name\": \"有理数引入\",\n" +
                "        \"thumbnail\": \"https://ol00xuufp.qnssl.com/%E6%9C%89%E7%90%86%E6%95%B0%E7%9A%84%E5%BC%95%E5%85%A5.jpg\",\n" +
                "        \"video\": \"5666910618b9b5ff9992a619\",\n" +
                "        \"titleTime\": 8.346,\n" +
                "        \"finishTime\": 383.056,\n" +
                "        \"duration\": 383.056,\n" +
                "        \"replace\": true,\n" +
                "        \"sections\": [],\n" +
                "        \"interactions\": [],\n" +
                "        \"url\": {\n" +
                "            \"mobile\": {\n" +
                "                \"mp4_middle\": \"http://private.media.yangcong345.com/mobileM/mobileM_57d6b6d9ba53a54020ced8d0.mp4\",\n" +
                "                \"hls_low\": \"https://o558dvxry.qnssl.com/mobileL/mobileL_57d6b6d9ba53a54020ced8d0.m3u8\",\n" +
                "                \"hls_middle\": \"https://o558dvxry.qnssl.com/mobileM/mobileM_57d6b6d9ba53a54020ced8d0.m3u8\",\n" +
                "                \"mp4_middle_md5\": \"a48771c9b910e68308e5d75e1430beca\"\n" +
                "            },\n" +
                "            \"pc\": {\n" +
                "                \"hls_high\": \"https://o558dvxry.qnssl.com/high/high_57d6b6d9ba53a54020ced8d0.m3u8\",\n" +
                "                \"hls_low\": \"https://o558dvxry.qnssl.com/pcL/pcL_57d6b6d9ba53a54020ced8d0.m3u8\",\n" +
                "                \"hls_middle\": \"https://o558dvxry.qnssl.com/pcM/pcM_57d6b6d9ba53a54020ced8d0.m3u8\",\n" +
                "                \"mp4_middle\": \"http://private.media.yangcong345.com/pcM/pcM_57d6b6d9ba53a54020ced8d0.mp4\",\n" +
                "                \"mp4_high\": \"http://private.media.yangcong345.com/high/high_57d6b6d9ba53a54020ced8d0.mp4\"\n" +
                "            }\n" +
                "        }\n" +
                "    }";

        mediaControllerBoard.addPlugin(new VideoData(json2Map(info)));
        mediaControllerBoard.addPlugin(new VideoTitle());

        mediaControllerBoard.addPlugin(new CloseButton());
        mediaControllerBoard.addPlugin(new OperationBar());
        mediaControllerBoard.addPlugin(new ProgressBar());
        mediaControllerBoard.addPlugin(new LoadingPanel());
        mediaControllerBoard.addPlugin(new Download());
        mediaControllerBoard.addPlugin(new StatePanel());
        mediaControllerBoard.addPlugin(new Interaction());
        mediaControllerBoard.addPlugin(new PlayButton());

        mBinding.btnShow.setOnClickListener(this);
        mBinding.btnHide.setOnClickListener(this);

        mBinding.videoView.getVideoBitrate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaControllerBoard.onLifeResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaControllerBoard.onLifePause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.videoView.stopPlayback();
    }

    private MediaControllerBoard mediaControllerBoard;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
//                mediaControllerBoard.show();
                mBinding.videoView.start();
                break;
            case R.id.btn_hide:
                mBinding.videoView.pause();
//                mediaControllerBoard.hide();
                break;
        }
    }

    public static HashMap<String, Object> json2Map(String jsonStr) {
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(jsonStr, type);
        return map;
    }
}
