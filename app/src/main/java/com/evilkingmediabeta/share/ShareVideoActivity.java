package com.evilkingmediabeta.share;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.app.MediaRouteButton;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.R;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import hybridmediaplayer.ExoMediaPlayer;
import hybridmediaplayer.MediaSourceInfo;
import io.fabric.sdk.android.Fabric;

public class ShareVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private ExoMediaPlayer mediaPlayer;
    float speed = 1;
    private SurfaceView playerView;

    private LinearLayout controlLayout;

    //Chromecast
    private CastContext castContext;
    private MediaRouteButton mediaRouteButton;
    private List<MediaSourceInfo> sources;

    private String channel_name = "";
    private String channel_logo = "";
    private String channel_url = "";

    private String stream_type = "";

    private Button btnBackMenu;

    private ImageView btPlay;
    private ImageView btPause;

    private ProgressBar prog;

    int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_share_video);

        channel_logo = getIntent().getStringExtra("CHANNEL_LOGO");
        channel_name = getIntent().getStringExtra("CHANNEL_NAME");
        channel_url = getIntent().getStringExtra("CHANNEL_URL");

        stream_type = getIntent().getStringExtra("STREAM_TYPE");

//        channel_logo = "";
//        channel_name = "Test";
//        channel_url = "https://github.com/mediaelement/mediaelement-files/blob/master/big_buck_bunny.mp4?raw=true";

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else{
            // do something for phones running an SDK before lollipop
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        controlLayout = findViewById(R.id.controlLayout);

        prog = findViewById(R.id.progress_bar);
        prog.setVisibility(View.VISIBLE);

        btPlay = findViewById(R.id.btPlay);
        btPlay.setOnClickListener(view -> {
            if (mediaPlayer != null){
                mediaPlayer.play();
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
            }
        });

        btPause = findViewById(R.id.btPause);
        btPause.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                KLog.d(mediaPlayer.getCurrentPosition());
                KLog.i(mediaPlayer.getDuration());
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
            }
        });

        btnBackMenu = findViewById(R.id.btnBackMenu);
        btnBackMenu.setBackgroundResource(R.drawable.section_selection_background_whitetext);
        btnBackMenu.setOnClickListener(view -> {
            finish();
        });


        ImageView btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        ImageView btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                prev_time= System.currentTimeMillis();
                if (mediaPlayer.getCurrentPosition() < 30000 ) {
                    currentPosition = 0;
                    createPlayer();
                }
                else {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 30000);
                }
            }
        });

        ImageView btnForward = findViewById(R.id.btnForward);
        btnForward.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                prev_time= System.currentTimeMillis();
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+30000);
            }
        });

        ImageView btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

//        btStop = findViewById(R.id.btStop);
//        btStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
//        });

        playerView = findViewById(R.id.playerView);
        ImageView btnToggle = findViewById(R.id.btnToggleController);
        btnToggle.setOnClickListener(view -> {
//                prev_time= System.currentTimeMillis();
//                flag_control = true;
//                controlLayout.setVisibility(View.VISIBLE);
            Log.d("Video playing","player view is clicked");
            if(flag_control) {
                hideController();
            }
            else {
                btnBackMenu.requestFocus();
                showController();
            }
        });

        //Chromecast:
        mediaRouteButton = findViewById(R.id.media_route_button);
//        mediaRouteButton.setRemoteIndicatorDrawable( ContextCompat.getDrawable(this, R.drawable.progress2) );

        if (stream_type.equals("vod")){

            btnBackward.setVisibility(View.VISIBLE);
            btnForward.setVisibility(View.VISIBLE);

            showController();

        }
        else{
            controlLayout.setVisibility(View.INVISIBLE);
//            mediaRouteButton.setVisibility(View.INVISIBLE);
            btnBackward.setVisibility(View.INVISIBLE);
            btnForward.setVisibility(View.INVISIBLE);
        }

        initChromecast();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        createPlayer();

        runControlThread();

    }

    void initChromecast(){
        try {

            mediaRouteButton.setVisibility(View.VISIBLE);
            castContext = CastContext.getSharedInstance(this);
            castContext.addCastStateListener(state -> {
                if (state == CastState.NO_DEVICES_AVAILABLE)
                    mediaRouteButton.setVisibility(View.GONE);
                else {
                    if (mediaRouteButton.getVisibility() == View.GONE)
                        mediaRouteButton.setVisibility(View.VISIBLE);
                }
            });

            CastButtonFactory.setUpMediaRouteButton(this, mediaRouteButton);

        } catch (Exception e) {
            // track non-fatal
            mediaRouteButton.setVisibility(View.INVISIBLE);
        }
    }
    private void showController(){
        prev_time= System.currentTimeMillis();
        flag_control = true;
        controlLayout.setVisibility(View.VISIBLE);
    }

    private void hideController(){
        controlLayout.setVisibility(View.INVISIBLE);
        flag_control = false;
    }


    long prev_time;
    boolean flag_control;

    public void runControlThread() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long diff = System.currentTimeMillis() - prev_time;
                if ( diff > 2000 && flag_control){
                    hideController();
                }
            }
            @Override
            public void onFinish() {
                runControlThread();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (supportsPiPMode()) {
            mediaPlayer.play();
        } else {
            if (mediaPlayer != null)
                mediaPlayer.pause();
        }

    }

    public boolean supportsPiPMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }



    @Override
    public void onUserLeaveHint () {
        if (supportsPiPMode()) {
            enterPictureInPictureMode();
            mediaPlayer.play();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    private void createPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new ExoMediaPlayer(this, castContext, 0);
        //mediaPlayer.setDataSource(url);

//        getParsedUrl(channel_url);
        MediaSourceInfo source1 = new MediaSourceInfo.Builder().setUrl(channel_url)
                .setTitle(channel_name)
                .isVideo(true)
                .setImageUrl(channel_logo)
                .build();

        sources = new ArrayList<>();
        sources.add(source1);

        mediaPlayer.setPlayerView(this, playerView);
        mediaPlayer.setSupportingSystemEqualizer(true);
        mediaPlayer.setOnTrackChangedListener(isFinished -> KLog.w("onTrackChanged isFinished " + isFinished + " " + mediaPlayer.getDuration() + " window = " + mediaPlayer.getCurrentWindow()));

        mediaPlayer.setOnPreparedListener(player -> {
            KLog.w(mediaPlayer.hasVideo());
            KLog.d("onPrepared " + mediaPlayer.getCurrentPlayer());
            prog.setVisibility(View.GONE);
        });

        mediaPlayer.setOnPlayerStateChanged((playWhenReady, playbackState) -> KLog.d("onPlayerStateChanged playbackState " + playbackState + " position " + mediaPlayer.getCurrentWindow()));

        mediaPlayer.setOnCompletionListener(player -> KLog.i("onCompletion"));

        mediaPlayer.setOnLoadingChanged(isLoading -> {
            KLog.d("setOnLoadingChanged " + isLoading);
            if(!isLoading) {
                currentPosition = mediaPlayer.getCurrentPosition();
                createPlayer();
            }
        });

        mediaPlayer.setDataSource(sources, sources);
        mediaPlayer.setOnAudioSessionIdSetListener(audioSessionId -> KLog.d("onAudioSessionIdset audio session id = " + audioSessionId));

        mediaPlayer.setOnPositionDiscontinuityListener((reason, currentWindowIndex) -> KLog.w("onPositionDiscontinuity reason " + reason + " position " + mediaPlayer.getCurrentWindow() + " currentWindowIndex " + currentWindowIndex));
//        mediaPlayer.setInitialWindowNum(2);

        mediaPlayer.prepare();
        mediaPlayer.play();

        if(currentPosition != 0 ) {
            mediaPlayer.seekTo(currentPosition);
            currentPosition = 0;
        }

        KLog.w(mediaPlayer.getWindowCount());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.options, menu);
//        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

//    private void setVolume(boolean flag_up){

//        if (flag_up){
//            volumeProgressbar.setProgress(volumeProgressbar.getProgress() + 1);
//        }
//        else{
//            volumeProgressbar.setProgress(volumeProgressbar.getProgress() - 1);
//        }
//        audio.setStreamVolume(AudioManager.STREAM_MUSIC,volumeProgressbar.getProgress(), 0);
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD || keyCode == KeyEvent.KEYCODE_BUTTON_L1) {

            if (stream_type.equals("vod")){
                showController();
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 30000);
            }
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD || keyCode == KeyEvent.KEYCODE_BUTTON_R1 ) {

            if (stream_type.equals("vod")){
                showController();
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 30000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_MENU ){
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BUTTON_A ){

            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

}