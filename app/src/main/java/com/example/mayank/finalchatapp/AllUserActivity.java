package com.example.mayank.finalchatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar AllUserMenuBar;
    private DatabaseReference reference;
    String TAG = "dbsdj";
    DatabaseReference databaseReference;
    FirebaseAuth mmAuth;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    private DatabaseReference OdatabaseReference;
    private FirebaseUser OcurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        mmAuth = FirebaseAuth.getInstance();
        currentUser = mmAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        OcurrentUser = mAuth.getCurrentUser();
        OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());



        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        AllUserMenuBar =  findViewById(R.id.AllUserMenubar);
        setSupportActionBar(AllUserMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.AURecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllUserActivity.this));
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue("true");
            Toast.makeText(AllUserActivity.this,"online update",Toast.LENGTH_LONG).show();

        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<AllUsers> options =
                new FirebaseRecyclerOptions.Builder<AllUsers>()
                        .setQuery(query, AllUsers.class)
                        .build();


        FirebaseRecyclerAdapter<AllUsers, UserViewHolder> adapter = new FirebaseRecyclerAdapter<AllUsers, UserViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull AllUsers user) {
                final int pos = position;

                holder.setName(user.getName(),getRef(pos).getKey());
                holder.setUserStatus(user.getStatus(),getRef(pos).getKey());
                holder.setImage(user.getThumImage(),getRef(pos).getKey());
                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AllUserActivity.this, ProfileActivity.class);
                        String userId = getRef(pos).getKey();
                        intent.putExtra("USER_CLICKED_ID", userId);
                        startActivity(intent);

                    }
                });


            }

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.au_single_row_layout, parent, false);

                return new UserViewHolder(mView);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public  class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;
        Context c;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name,String s){
            TextView textView = (TextView)mView.findViewById(R.id.name);
            textView.setText(name);
            if(s.equals(currentUser.getUid())){
                LinearLayout layout = (LinearLayout) mView.findViewById(R.id.AU_single_row);
                layout.setVisibility(View.GONE);
                mView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }

        }


        public void setUserStatus(String status,String s){
            TextView txtUserStatus = (TextView)mView.findViewById(R.id.status);
            txtUserStatus.setText(status);
//            if(s.equals(currentUser.getUid())){
//                txtUserStatus.setVisibility(View.GONE);
//            }
        }


        public void setImage(String image,String s) {

            CircleImageView img = (CircleImageView) mView.findViewById(R.id.image);
            Picasso.with(AllUserActivity.this).load(image).into(img);
            // if(s.equals(currentUser.getUid())){
            //   img.setVisibility(View.GONE);
            //}

        }


        public View getView(){
            return this.mView;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue("true");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }




}
