package com.example.barebonesocketioandroid;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatActivity extends AppCompatActivity {
    public RecyclerView myRecylerView;
    public List<Message> MessageList;
    public ChatListAdapter chatAdapter;
    public EditText messagetxt;
    public Button send;
    //declare socket object
    private Socket socket;

    public String Nickname = "Android";
    private String SERVER_URL = "http://192.168.11.100:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagetxt = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        //connect you socket client to the server
        try {
            socket = IO.socket(SERVER_URL);
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if (!messagetxt.getText().toString().isEmpty()) {
                    socket.emit("message_from_client", Nickname, messagetxt.getText().toString());

                    messagetxt.setText(" ");
                }


            }
        });

        //implementing socket listeners
        socket.on("message_from_server", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                            Message m = new Message(nickname, message);


                            //add the message to the messageList

                            MessageList.add(m);

                            // add the new updated list to the dapter
                            chatAdapter = new ChatListAdapter(MessageList);

                            // notify the adapter to update the recycler view

                            chatAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            myRecylerView.setAdapter(chatAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}