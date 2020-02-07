package com.evilkingmediabeta.player;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;

import ch.srg.mediaplayer.SRGMediaPlayerController;
import ch.srg.mediaplayer.SRGMediaPlayerView;
import io.fabric.sdk.android.Fabric;

public class VideoPlayerActivity extends AppCompatActivity implements SRGMediaPlayerController.Listener {

    private SRGMediaPlayerView videoView;
    private SRGMediaPlayerController mediaPlayerController;
    private ProgressBar progressBar;
    private LinearLayout control_layout;
    private ImageButton playBtn, pauseBtn;
    private SeekBar seekBar;
    private static @SRGMediaPlayerController.SRGStreamType int streamType = 0;

    String URL = "";
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_video_player);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else{
            // do something for phones running an SDK before lollipop
        }

        URL = getIntent().getStringExtra("CHANNEL_URL");

        if(URL.endsWith(".m3u8")){
            streamType = SRGMediaPlayerController.STREAM_HLS;
        } else {
            streamType = SRGMediaPlayerController.STREAM_HTTP_PROGRESSIVE;
        }
        mHandler = new Handler();

        videoView = findViewById(R.id.videoView);
        mediaPlayerController = new SRGMediaPlayerController(this, "ekm_m3u8_player");

        progressBar = findViewById(R.id.progressBar);
        control_layout = findViewById(R.id.control_layout);
        playBtn = findViewById(R.id.play_btn);
        pauseBtn = findViewById(R.id.pause_btn);
        seekBar = findViewById(R.id.seekBar);

        control_layout.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerController.isLive()){
                    if(!mediaPlayerController.isPlaying()){
                        playBtn.setVisibility(View.GONE);
                        pauseBtn.setVisibility(View.VISIBLE);
                        mediaPlayerController.start();
                    }
                }
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control_layout.getVisibility() == View.VISIBLE)
                    control_layout.setVisibility(View.GONE);
                else
                    control_layout.setVisibility(View.VISIBLE);
            }
        });


        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerController.isPlaying()){
                    pauseBtn.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                    mediaPlayerController.pause();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateTask);
                mediaPlayerController.seekTo(seekBar.getProgress());
                updateProgressBar();
            }
        });

//        mediaPlayerController.setVolume(1.0f);


//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Preparing..");
//        progressDialog.show();
//        VideoView videoView = findViewById(R.id.videoView);
//        MediaController mediacontroller = new MediaController(this);
//        mediacontroller.setAnchorView(videoView);
//        Uri uri = Uri.parse(videoUrl);
//
//        videoView.setMediaController(mediacontroller);
//        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        videoView.start();
//        videoView.setOnPreparedListener(mp -> progressDialog.dismiss());
//
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE
//        );
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayerController.registerEventListener(this);
        mediaPlayerController.bindToMediaPlayerView(videoView);
        mediaPlayerController.keepScreenOn(true);
        mediaPlayerController.play(Uri.parse(URL), streamType);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (supportsPiPMode()) {
            control_layout.setVisibility(View.GONE);
            mediaPlayerController.play(Uri.parse(URL), streamType);
            mediaPlayerController.keepScreenOn(false);


        } else {
            mediaPlayerController.unbindFromMediaPlayerView(videoView);
            mediaPlayerController.release();
            mediaPlayerController.unregisterEventListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayerController.keepScreenOn(false);
    }

    public boolean supportsPiPMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }



    @Override
    public void onUserLeaveHint () {
        if (supportsPiPMode()) {
            mediaPlayerController.play(Uri.parse(URL), streamType);

        } else {
            mediaPlayerController.play(Uri.parse(URL), streamType);

        }
    }


    @Override
    public void onMediaPlayerEvent(SRGMediaPlayerController mp, SRGMediaPlayerController.Event event) {
        Log.d("MediaPlayerEvent", " " + event.type + " " + event.state);
        if(event.type == SRGMediaPlayerController.Event.Type.PLAYBACK_ACTUALLY_STARTED){
            progressBar.setVisibility(View.GONE);
            control_layout.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            updateProgressBar();
        }
    }


    private void updateProgressBar() {
        mHandler.postDelayed(updateTask, 1000);
    }

    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            seekBar.setMax(Integer.parseInt(String.valueOf(mediaPlayerController.getMediaDuration())));
            seekBar.setProgress(Integer.parseInt(String.valueOf(mediaPlayerController.getMediaPosition())));
            mHandler.postDelayed(this, 1000);
        }
    };

}
