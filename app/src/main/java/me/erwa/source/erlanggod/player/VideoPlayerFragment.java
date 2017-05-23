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
import me.erwa.source.erlanggod.player.widget.plugin.video.player.CellularWarning;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.CloseButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Download;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.GesturePanel;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.Interaction;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.LoadingPanel;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.NetStateMonitor;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.OperationBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.PlayButton;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.PlayerController;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.ProgressBar;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.QualityMode;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.VideoData;
import me.erwa.source.erlanggod.player.widget.plugin.video.player.VideoTitle;

/**
 * Created by drawf on 2017/3/22.
 * ------------------------------
 * 主播放器
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
//        mBinding.videoView.setAVOptions(OptionsManager.newInstance().setAutoStart(true).build());
        mBinding.videoView.setVideoPath("");//need to call this to init plugins
//        mBinding.videoView.setVideoPath("https://hls.media.yangcong345.com/mobileL/mobileL_586d5aa4065b7e9d714294fc.m3u8");
//        mBinding.videoView.setVideoPath("https://hls.media.yangcong345.com/mobileM/mobileM_58c26cbb36eaf35866aae116.m3u8");
//        mBinding.videoView.setVideoPath("https://o558dvxry.qnssl.com/pcM/pcM_584f96fbefdf207b0822cf7a.m3u8");
        mediaControllerBoard = new MediaControllerBoard(getActivity(), R.layout.media_controller_board);
        mBinding.videoView.setMediaController(mediaControllerBoard);

//        mBinding.videoView.seekTo(128000);


        String info = "{\n" +
                "        \"_id\": \"57ff2b1a5a149bc467ce922a\",\n" +
                "        \"duration\": 400.22,\n" +
                "        \"finishTime\": 400.22,\n" +
                "        \"titleTime\": 8.068,\n" +
                "        \"video\": \"57fef7ef5b4b792c56dec81a\",\n" +
                "        \"name\": \"特殊直角三角形的三边关系\",\n" +
                "        \"__v\": 0,\n" +
                "        \"thumbnail\": \"https://course.yangcong345.com/%E5%8B%BE%E8%82%A1%E5%AE%9A%E7%90%86_A14_%E7%89%B9%E6%AE%8A%E7%9B%B4%E8%A7%92%E4%B8%89%E8%A7%92%E5%BD%A2%E7%9A%84%E4%B8%89%E8%BE%B9%E5%85%B3%E7%B3%BB.bmp\",\n" +
                "        \"replace\": true,\n" +
                "        \"desc\": \"\",\n" +
                "        \"sections\": [],\n" +
                "        \"interactions\": [\n" +
                "            {\n" +
                "                \"jump\": 138.477,\n" +
                "                \"time\": 133.477,\n" +
                "                \"_id\": \"57ff2f56ac0d19594b4c7c49\",\n" +
                "                \"choices\": [\n" +
                "                    {\n" +
                "                        \"correct\": true,\n" +
                "                        \"body\": \"A\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"correct\": false,\n" +
                "                        \"body\": \"B\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"jump\": 0,\n" +
                "                \"time\": 229.477,\n" +
                "                \"_id\": \"57ff2f56ac0d19594b4c7c48\",\n" +
                "                \"choices\": [\n" +
                "                    {\n" +
                "                        \"correct\": true,\n" +
                "                        \"body\": \"A\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"correct\": false,\n" +
                "                        \"body\": \"B\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"jump\": 0,\n" +
                "                \"time\": 255.477,\n" +
                "                \"_id\": \"57ff2f56ac0d19594b4c7c47\",\n" +
                "                \"choices\": [\n" +
                "                    {\n" +
                "                        \"correct\": false,\n" +
                "                        \"body\": \"A\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"correct\": true,\n" +
                "                        \"body\": \"B\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"url\": {\n" +
                "            \"mobile\": {\n" +
                "                \"mp4_middle\": \"http://private.media.yangcong345.com/mobileM/mobileM_586d5aa4065b7e9d714294fc.mp4\",\n" +
                "                \"hls_low\": \"https://hls.media.yangcong345.com/mobileL/mobileL_586d5aa4065b7e9d714294fc.m3u8\",\n" +
                "                \"hls_middle\": \"https://o558dvxry.qnssl.com/pcM/pcM_584f96fbefdf207b0822cf7a.m3u8\",\n" +
                "                \"mp4_middle_md5\": \"d4d09be61e3e172125ffd31501e76660\"\n" +
                "            },\n" +
                "            \"pc\": {\n" +
                "                \"hls_high\": \"https://hls.media.yangcong345.com/high/high_586d5aa4065b7e9d714294fc.m3u8\",\n" +
                "                \"hls_low\": \"https://hls.media.yangcong345.com/pcL/pcL_586d5aa4065b7e9d714294fc.m3u8\",\n" +
                "                \"hls_middle\": \"https://hls.media.yangcong345.com/pcM/pcM_586d5aa4065b7e9d714294fc.m3u8\",\n" +
                "                \"mp4_middle\": \"http://private.media.yangcong345.com/pcM/pcM_586d5aa4065b7e9d714294fc.mp4\",\n" +
                "                \"mp4_high\": \"http://private.media.yangcong345.com/high/high_586d5aa4065b7e9d714294fc.mp4\"\n" +
                "            }\n" +
                "        }\n" +
                "    }";

//        https://hls.media.yangcong345.com/mobileM/mobileM_586d5aa4065b7e9d714294fc.m3u8


        mediaControllerBoard.addPlugin(new VideoTitle());
        mediaControllerBoard.addPlugin(new VideoData(json2Map(info)));

        mediaControllerBoard.addPlugin(new CloseButton());
        mediaControllerBoard.addPlugin(new OperationBar());
        mediaControllerBoard.addPlugin(new ProgressBar());
        mediaControllerBoard.addPlugin(new LoadingPanel());
        mediaControllerBoard.addPlugin(new Download());
        mediaControllerBoard.addPlugin(new GesturePanel());
        mediaControllerBoard.addPlugin(new Interaction());
        mediaControllerBoard.addPlugin(new PlayButton());
        mediaControllerBoard.addPlugin(new CellularWarning());
        mediaControllerBoard.addPlugin(new NetStateMonitor());
        mediaControllerBoard.addPlugin(new QualityMode());
        mediaControllerBoard.addPlugin(new PlayerController());

        mBinding.btnShow.setOnClickListener(this);
        mBinding.btnHide.setOnClickListener(this);

//        mBinding.videoView.setVideoPath("https://hls.media.yangcong345.com/mobileL/mobileL_586d5aa4065b7e9d714294fc.m3u8");
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
