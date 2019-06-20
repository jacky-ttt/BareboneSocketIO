package com.example.barebonesocketioandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private List<Message> MessageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;


        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);


        }
    }

    public ChatListAdapter(List<Message> MessagesList) {

        this.MessageList = MessagesList;


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_item, parent, false);


        return new ChatListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatListAdapter.MyViewHolder holder, final int position) {
        final Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname() + " : ");

        holder.message.setText(m.getMessage());


    }


}