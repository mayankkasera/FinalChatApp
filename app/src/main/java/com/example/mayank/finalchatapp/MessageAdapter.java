package com.example.mayank.finalchatapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by mayank on 10/1/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder>{

    private FirebaseAuth firebaseAuth;
    private List<SingleMessage> MessageList;

    public MessageAdapter(List<SingleMessage> messageList) {
        MessageList = messageList;
    }


    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_message,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        SingleMessage singleMessage = MessageList.get(position);
        String curentUser =  firebaseAuth.getUid();
        String id = singleMessage.getFrom();
        if (id.equals(curentUser)){
            holder.Message.setBackgroundColor(R.drawable.cus_register);
            holder.Message.setTextColor(Color.WHITE);
        }
        holder.Message.setText(singleMessage.getMessage());
    }




    @Override
    public int getItemCount() {
        return MessageList.size();
    }



    public class MessageHolder extends RecyclerView.ViewHolder{

        TextView Message;

        public MessageHolder(View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.Sin_Massege);
        }
    }



}
