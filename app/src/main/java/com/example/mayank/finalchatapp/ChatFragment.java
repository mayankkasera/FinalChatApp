package com.example.mayank.finalchatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    View mView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String id;
    private RecyclerView recyclerView;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        recyclerView = mView.findViewById(R.id.ChatRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mView;
    }
    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Chat")
                .child(id)
                .limitToLast(50);

        FirebaseRecyclerOptions<DataChat> options =
                new FirebaseRecyclerOptions.Builder<DataChat>()
                        .setQuery(query, DataChat.class)
                        .build();




        FirebaseRecyclerAdapter<DataChat,ChatViewHolder> adapter = new FirebaseRecyclerAdapter<DataChat,ChatFragment.ChatViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final ChatFragment.ChatViewHolder holder, int position, @NonNull DataChat user) {
                final int pos = position;
                holder.setData(user.getLastMessage());
                final String userId = getRef(pos).getKey();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String thumImage = dataSnapshot.child("thumImage").getValue().toString();

                        holder.setName(name);
                        holder.setImage(thumImage);
                        holder.setOnline(userId);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        Intent intent = new Intent(getContext(), MssegeActivity.class);
                        intent.putExtra("USER_CLICKED_ID", userId);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public ChatFragment.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.au_single_row_layout, parent, false);

                return new ChatFragment.ChatViewHolder(mView);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }





    public class ChatViewHolder extends RecyclerView.ViewHolder{
        View mView;


        public ChatViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setData(String name){
            TextView textView = (TextView)mView.findViewById(R.id.status);
            textView.setText(name);
        }

        public void setName(String name){
            TextView textView = (TextView)mView.findViewById(R.id.name);
            textView.setText(name);
        }

        public void setImage(String image) {

            CircleImageView img = (CircleImageView) mView.findViewById(R.id.image);;
            //
            Picasso.with(getContext()).load(image).into(img);

        }
        public void setOnline(String userId){
            final ImageView imageView = (ImageView)mView.findViewById(R.id.online);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String online = dataSnapshot.child("online").getValue().toString();
                    if(online.equals("true")){
                        imageView.setVisibility(View.VISIBLE);
                    }
                    else{
                        imageView.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


        public View getView(){
            return this.mView;
        }

    }


}
