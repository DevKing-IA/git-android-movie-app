package com.evilkingmediabeta.mywebcaster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.ihhira.android.filechooser.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.evilkingmediabeta.Constant;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.demand.Utils;
import com.evilkingmediabeta.utility.CheckXml;
import com.evilkingmediabeta.utility.CustomKeyboardHandler;

import io.fabric.sdk.android.Fabric;

public class MyWebCasterActivity extends AppCompatActivity {

    EditText etSearchLink, urlSearch;
    String linkStr;
    Button btnSearchLink, btnViewHistory;
    Button backup, restore, close;
    TextView emptyTextView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;

    List<Utils> urlList;
    List<Utils> tempList = new ArrayList<>();
    public static ListView listView;
    FileChooser fileChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_my_web_caster);

        etSearchLink = findViewById(R.id.etLinkSearch);
        btnSearchLink = findViewById(R.id.searchBtn);
        btnViewHistory = findViewById(R.id.viewHistoryBtn);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!navigationView.isShown()){
                    drawerLayout.openDrawer(Gravity.START);
                    showHistoryView();
                }
            }
        });

        etSearchLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(MyWebCasterActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(MyWebCasterActivity.this, etSearchLink.getWindowToken());
                }
            }
        });

        btnSearchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkStr = etSearchLink.getText().toString().trim();
                if(linkStr.isEmpty()){
                    Toast.makeText(MyWebCasterActivity.this, "You must enter a valid url link.", Toast.LENGTH_LONG).show();
                }else if(!URLUtil.isValidUrl(linkStr)){
                    Toast.makeText(MyWebCasterActivity.this, "Url link address is invalid!", Toast.LENGTH_LONG).show();
                }else{

                    SharedPreferences sharedPreferences1 = getSharedPreferences("VisitedLinks", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = getSharedPreferences("VisitedLinks", Context.MODE_PRIVATE).edit();
                    if(sharedPreferences1.getAll().size() == 0){
                        editor.putString("1", linkStr);
                        editor.apply();
                    }else{
                        Map<String, ?> allEntries = sharedPreferences1.getAll();
                        int count = 0;
                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                            if(linkStr.equals(entry.getValue())){
                               count++;
                            }
                        }
                        if(count == 0){
                            editor.putString(String.valueOf(sharedPreferences1.getAll().size() + 1), linkStr);
                            editor.apply();
                        }
                    }
                    Constant.openWVCapp(MyWebCasterActivity.this, linkStr);
                }
            }
        });

        CheckXml.checkXml(this);
    }

    private void showHistoryView(){
        urlSearch = navigationView.findViewById(R.id.urlSearch);
        backup = navigationView.findViewById(R.id.backup);
        restore = navigationView.findViewById(R.id.restore);
        close = navigationView.findViewById(R.id.closeBtn);
        emptyTextView = navigationView.findViewById(R.id.emptyTextView);
        listView = navigationView.findViewById( R.id.linkListView);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        urlSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(MyWebCasterActivity.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(MyWebCasterActivity.this, urlSearch.getWindowToken());
                }
            }
        });

        urlSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempList.clear();
                for (int i = 0; i < urlList.size(); i++){
                    Utils utils = urlList.get(i);
                    String url = utils.getTitle();
                    if (url.contains(s)){
                        tempList.add(urlList.get(i));
                    }
                }
                ArrayAdapter<Utils> adapter = new MyWebCasterAdapter(MyWebCasterActivity.this, tempList);
                listView.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlList.size() == 0){
                    Toast.makeText(MyWebCasterActivity.this, "Data that you are going to backup don't exist!", Toast.LENGTH_LONG).show();
                } else {
                    backupData();
                }
            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser = new FileChooser(MyWebCasterActivity.this, "File Choose", FileChooser.DialogType.SELECT_FILE, null);
                fileChooser.show(new FileChooser.FileSelectionCallback() {
                    @Override
                    public void onSelect(File file) {
                        emptyTextView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        progressDialog = new ProgressDialog(MyWebCasterActivity.this);
                        progressDialog.setMessage("Loading data from file...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        restoreData(file);
                    }
                });
            }
        });

        urlList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("VisitedLinks", Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().size() == 0){
            listView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            urlList.add(new Utils(entry.getValue().toString(), R.drawable.ic_public_black_24dp));
        }

        ArrayAdapter<Utils> adapter = new MyWebCasterAdapter(MyWebCasterActivity.this, urlList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void restoreData(File file){
        FileInputStream fin;
        try {
            fin = new FileInputStream(file);
            String ret = convertStreamToString(fin);
            fin.close();
            String[] backupUrls = ret.split("##########");
            if (urlList.size() == 0){
                for (String backupUrl : backupUrls){
                    urlList.add(new Utils(backupUrl, R.drawable.ic_public_black_24dp));
                }
            } else {
                for (String backupUrl : backupUrls) {
                    boolean isEqual = false;
                    for (int i = 0; i < urlList.size(); i++){
                        if (urlList.get(i).getTitle().equals(backupUrl)) {
                            isEqual = true;
                        }
                    }
                    if (!isEqual){
                        urlList.add(new Utils(backupUrl, R.drawable.ic_public_black_24dp));
                    }
                }
            }
            ArrayAdapter<Utils> adapter = new MyWebCasterAdapter(MyWebCasterActivity.this, urlList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = getSharedPreferences("VisitedLinks", Context.MODE_PRIVATE).edit();
            editor.clear();
            for (int i = 0; i < urlList.size(); i++){
                editor.putString(Integer.toString(i), urlList.get(i).getTitle());
            }
            editor.apply();
            progressDialog.dismiss();
            Toast.makeText(MyWebCasterActivity.this, "Backup file restored successfully!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(MyWebCasterActivity.this, "File not found!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(MyWebCasterActivity.this, "Failed reading file!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(MyWebCasterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void backupData(){

        File backupFolder = new File(Environment.getExternalStorageDirectory(),"/EKMBackup");
        if(!backupFolder.exists()){
            backupFolder.mkdir();
        }

        try{
            File file = new File(backupFolder, "/ekm_my_web_caster_visited_urls");
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < urlList.size(); i++){
                String line;
                if (i == urlList.size()-1){
                    line = urlList.get(i).getTitle();
                } else {
                    line = urlList.get(i).getTitle() + "##########";
                }
                writer.append(line);
            }
            writer.flush();
            writer.close();

            Toast.makeText(MyWebCasterActivity.this, "Backup file stored into " + file.getPath() + " successfully!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MyWebCasterActivity.this, "Storing backup file failed!", Toast.LENGTH_LONG).show();
        }
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckXml.checkXml(this);
    }
}
