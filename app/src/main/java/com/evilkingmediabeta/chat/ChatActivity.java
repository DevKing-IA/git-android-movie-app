package com.evilkingmediabeta.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scaledrone.lib.HistoryRoomListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.scaledrone.lib.SubscribeOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.evilkingmediabeta.R;

import io.fabric.sdk.android.Fabric;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity implements RoomListener {

    // replace this with a real channelID from Scaledrone dashboard
    String channelID = "UY5Jb9NMyjGN1Xtc";
    String SERVER_KEY = "NkTTeD6DP2lXL3ZWbv0f7lhVEQ2oE6e1";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;

    String my_email;
    String my_id;
    String client_id;
    String client_token;
    String clientuser_email;
    String clientuser_name;
    String profile_image_path;

    String my_room_name;
    String your_room_name;

    private Room your_room;
    int unreceived_count = 0;

    DatabaseReference drUserInfo;
    Map<String, Object> updateCount = new HashMap<>();
    Map<String, Object> updateMessage = new HashMap<>();

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_chat);

        drUserInfo = FirebaseDatabase.getInstance().getReference();

        editText = findViewById(R.id.et_message);
        messagesView = findViewById(R.id.messages_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageAdapter = new MessageAdapter(this);
        messagesView.setAdapter(messageAdapter);

        my_email = getIntent().getStringExtra("my_email");
        my_id = getIntent().getStringExtra("my_id");
        client_id = getIntent().getStringExtra("client_id");
        client_token = getIntent().getStringExtra("client_token");
        clientuser_name = getIntent().getStringExtra("client_name");
        clientuser_email = getIntent().getStringExtra("client_email");
        profile_image_path = getIntent().getStringExtra("profile_image_path");

        your_room_name = "observable-room-" + clientuser_email + "-" + my_email;
        my_room_name = "observable-room-" + my_email + "-" + clientuser_email;

        getSupportActionBar().setTitle("Chat with " + clientuser_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MemberData data = new MemberData(clientuser_name, profile_image_path, Color.WHITE);

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                Log.e("opened", "Scaledrone connection open");
                your_room = scaledrone.subscribe(your_room_name, ChatActivity.this, new SubscribeOptions(100));
                scaledrone.subscribe(my_room_name, ChatActivity.this, new SubscribeOptions(100));
                your_room.listenToHistoryEvents(new HistoryRoomListener() {
                    @Override
                    public void onHistoryMessage(Room room, com.scaledrone.lib.Message message) {
                        boolean belongsToCurrentUser;
                        String msg = message.getData().asText();
                        belongsToCurrentUser = msg.endsWith("\"" + clientuser_email + "\"");
                        String msgbody;
                        if (belongsToCurrentUser){
                            msgbody = msg.replace("\"" + clientuser_email + "\"", "");
                        } else {
                            msgbody = msg.replace("\"" + my_email + "\"", "");
                        }
                        final Message message1 = new Message(msgbody, data, !belongsToCurrentUser);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.add(message1);
                                messagesView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesView.setSelection(messagesView.getCount() - 1);
                                    }
                                });
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                updateCount.put(client_id, 0);
                updateMessage.put(client_id, "");
                drUserInfo.child(my_id).child("senders").updateChildren(updateCount);
                drUserInfo.child(my_id).child("lmessage").updateChildren(updateMessage);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                Log.e("open_failure", ex.getMessage());
            }

            @Override
            public void onFailure(Exception ex) {
                Log.e("failure", ex.getMessage());
            }

            @Override
            public void onClosed(String reason) {
                Log.e("closed", reason);
            }
        });
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            if (your_room.getMembers().size() == 1){
                unreceived_count ++;
                updateCount.put(my_id, unreceived_count);
                updateMessage.put(my_id, message);
                drUserInfo.child(client_id).child("senders").updateChildren(updateCount);
                drUserInfo.child(client_id).child("lmessage").updateChildren(updateMessage);
//                sendNotification(message, client_token);
            } else {
                unreceived_count = 0;
                updateCount.put(my_id, unreceived_count);
                updateMessage.put(my_id, "");
                drUserInfo.child(client_id).child("senders").updateChildren(updateCount);
                drUserInfo.child(client_id).child("lmessage").updateChildren(updateMessage);
            }
            scaledrone.publish(your_room_name, message + "\"" + my_email + "\"");
            scaledrone.publish(my_room_name, message + "\"" + my_email + "\"");
            editText.getText().clear();
        }
    }

    @Override
    public void onOpen(Room room) {
        Log.e("opened", "Connected room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        Log.e("open_failure", ex.getMessage());
    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        if (room.getName().equals(your_room_name)){
            final ObjectMapper mapper = new ObjectMapper();
            try {
                final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
                boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
                String msg = receivedMessage.getData().asText();
                String msgbody;
                if (belongsToCurrentUser){
                    msgbody = msg.replace("\"" + my_email + "\"", "");
                } else {
                    msgbody = msg.replace("\"" + clientuser_email + "\"", "");
                }
                final Message message = new Message(msgbody, data, belongsToCurrentUser);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.add(message);
                        messagesView.post(new Runnable() {
                            @Override
                            public void run() {
                                messagesView.setSelection(messagesView.getCount() - 1);
                            }
                        });
                        messageAdapter.notifyDataSetChanged();
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendNotification(String message, String regToken) {

        try {
            OkHttpClient client = new OkHttpClient();
            JSONObject json = new JSONObject();
            JSONObject dataJson = new JSONObject();
            dataJson.put("body", message);
            dataJson.put("title", "New message");
            json.put("notification", dataJson);
            json.put("to", regToken);
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .header("Authorization", "key=" + SERVER_KEY)
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String finalResponse = response.body().string();
            Log.d("Response", finalResponse);
        } catch (Exception e) {
            Log.d("ERROR",e+"");
        }

    }
}
