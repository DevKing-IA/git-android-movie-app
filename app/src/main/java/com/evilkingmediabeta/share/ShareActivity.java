package com.evilkingmediabeta.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class ShareActivity extends AppCompatActivity {

    ListView channelListView;
    EditText etHomeSearch;
    ImageView ivNext, ivPrev, ivUp, ivDown;
    TextView tvPage;

    private ProgressDialog mProgressDialog;

    int page_max_rows = 20;
    int page_count = 0;
    int page_rows = 0;
    int selected_page = 0;

    TVShareAdapter mAdapter;

    private ArrayList<ShareItemData> totalResultList = new ArrayList<>();
    private ArrayList<ShareItemData> condResultList = new ArrayList<>();
    private ArrayList<ShareItemData> subResultList = new ArrayList<>();

    private String stream_type = "";

    private int selected_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_share);

        stream_type = getIntent().getStringExtra("type");

        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);

        ivPrev.setVisibility(View.GONE);

        tvPage = findViewById(R.id.tvPage);

        etHomeSearch = findViewById(R.id.etHomeSearch);

        etHomeSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                CustomKeyboardHandler.showKeyboard(ShareActivity.this);
            } else {
                CustomKeyboardHandler.hiddenKeyboard(ShareActivity.this, etHomeSearch.getWindowToken());
            }
        });

        etHomeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etHomeSearch.setOnKeyListener((view, i, keyEvent) -> {
            if ( i == KeyEvent.KEYCODE_DPAD_DOWN || i == KeyEvent.KEYCODE_CHANNEL_DOWN) {
                etHomeSearch.clearFocus();
                return true;
            }
            return false;
        });

        etHomeSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard();
                etHomeSearch.clearFocus();
                return true;
            }
            return false;
        });

        ivNext.setOnClickListener(view -> onNext());

        ivPrev.setOnClickListener(view -> onPrev());

        ivUp.setOnClickListener(view -> channelListView.smoothScrollBy(0, -200));

        ivDown.setOnClickListener(view -> channelListView.smoothScrollBy(0, 200));

        channelListView = findViewById(R.id.channelListView);
        channelListView.setOnItemClickListener((adapterView, view, i, l) -> {
            selected_pos = i;
            refreshChannelList();
            playVideo();
        });

        mAdapter = new TVShareAdapter(this, R.layout.share_tv_item, subResultList);
        channelListView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new prepareData().execute();

    }

    private void hideSoftKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class prepareData extends AsyncTask<String, Void, List<SimpleM3UParser.M3U_Entry>> {

        protected List<SimpleM3UParser.M3U_Entry> doInBackground(String... urls) {
            try {
                totalResultList.clear();
                condResultList.clear();
                InputStream inputStream;
                if (stream_type.equals("vod")){
                    inputStream = new URL(Constant.CINEMA_SHARE_URL).openStream();
                }
                else{
                    inputStream = new URL(Constant.TV_SHARE_URL).openStream();
                }
                return new SimpleM3UParser().parse(inputStream);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(List<SimpleM3UParser.M3U_Entry> list) {
            if(mProgressDialog!=null) {
                mProgressDialog.dismiss();
            }
            if (list != null){
                for (int i=0;i < list.size(); i++){
                    SimpleM3UParser.M3U_Entry nChannel = list.get(i);
                    ShareItemData nData = new ShareItemData();
                    nData.setId(nChannel.getTvgId());
                    nData.setLogo(nChannel.getTvgLogo());
                    nData.setName(nChannel.getName());
                    nData.setGroupTitle(nChannel.getGroupTitle());
                    nData.setUrl(nChannel.getUrl());
                    totalResultList.add(nData);
                    condResultList.add(nData);
                }

                page_count = (int)(Math.round((float) totalResultList.size() / (float) page_max_rows + 0.5));
                selected_page = 0;

                if ( condResultList.size() >= page_max_rows ){
                    page_rows = page_max_rows;
                    ivNext.setVisibility(View.VISIBLE);
                }
                else{
                    page_rows = condResultList.size();
                    ivNext.setVisibility(View.INVISIBLE);
                }

                updateChannelList();
            }
        }

    }

    private void updateChannelList(){

        subResultList.clear();

        int start = selected_page * page_max_rows;
        int end;

        if (selected_page < page_count-1 ){
            end = (selected_page +1)* page_max_rows;
            page_rows = page_max_rows;
        }
        else {
            end =  condResultList.size();
            page_rows = condResultList.size() - start;
        }

        for (int i = start; i < end; i++){
            subResultList.add(condResultList.get(i));
        }

        if (selected_page > 0) {
            ivPrev.setVisibility(View.VISIBLE);
        }
        else{
            ivPrev.setVisibility(View.INVISIBLE);
        }

        if (selected_page < page_count - 1 ) {
            ivNext.setVisibility(View.VISIBLE);
        }
        else{
            ivNext.setVisibility(View.INVISIBLE);
        }

        refreshChannelList();
    }

    private void refreshChannelList(){

        tvPage.setText((selected_page + 1) +  "/" + page_count );

        channelListView.setSelection(selected_pos);
        mAdapter.selectPage(selected_page);
        mAdapter.selectPos(selected_pos);
        mAdapter.notifyDataSetChanged();
    }

    private void playVideo(){

        Intent intent = new Intent(this, ShareVideoActivity.class);
//        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("CHANNEL_NAME", subResultList.get(selected_pos).getName());
        intent.putExtra("CHANNEL_LOGO", subResultList.get(selected_pos).getLogo());
        intent.putExtra("CHANNEL_URL", subResultList.get(selected_pos).getUrl());
        intent.putExtra("STREAM_TYPE", stream_type);

        startActivity(intent);
    }

    private void searchData( CharSequence c ){
        condResultList.clear();
        for (int i = 0; i < totalResultList.size(); i++){
            ShareItemData itemData = totalResultList.get(i);
            if (itemData.getName().toLowerCase().contains(String.valueOf(c).toLowerCase())){
                condResultList.add(itemData);
            }
        }

        page_count = (int)(Math.round((float) condResultList.size() / (float) page_max_rows + 0.5));
        selected_page = 0;

        ivPrev.setVisibility(View.INVISIBLE);

        if ( condResultList.size() >= page_max_rows ){
            page_rows = page_max_rows;
            ivNext.setVisibility(View.VISIBLE);
        }
        else{
            page_rows = condResultList.size();
            ivNext.setVisibility(View.INVISIBLE);
        }

        updateChannelList();
    }

    private void onUp(){

        if ( selected_pos % page_max_rows == 0 ){

            etHomeSearch.requestFocus();
        }
        else{

            if ( selected_pos <= 0 ){
                if ( selected_page > 0 ){
                    selected_pos = page_max_rows - 1;
                    selected_page--;
                    updateChannelList();
                }
            }
            else{
                selected_pos--;
                refreshChannelList();
            }

        }

    }

    private void onDown(){
        if ( selected_pos >= page_rows-1){
            if ( selected_page < page_count-1 ){
                selected_pos = 0;
                selected_page++;
                updateChannelList();
            }
        }
        else{
            selected_pos++;
            refreshChannelList();
        }
    }

    private void onPrev(){
        if ( selected_page > 0 ){
            selected_pos = 0;
            selected_page--;
            updateChannelList();
        }
    }

    private void onNext(){
        if ( selected_page < page_count-1 ){
            selected_pos = 0;
            selected_page++;
            updateChannelList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {
            onUp();
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            onDown();
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD || keyCode == KeyEvent.KEYCODE_BUTTON_L1) {
            onPrev();
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD || keyCode == KeyEvent.KEYCODE_BUTTON_R1 ) {
            onNext();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_MENU ){
            return true;
        }

        if ( keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BUTTON_A ){
            playVideo();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }


}
