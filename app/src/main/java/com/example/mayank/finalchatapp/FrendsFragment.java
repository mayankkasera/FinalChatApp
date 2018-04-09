package com.example.mayank.finalchatapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
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
public class FrendsFragment extends Fragment {

    View mView;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String id;

    FirebaseAuth mAuth;
    private DatabaseReference OdatabaseReference;
    private FirebaseUser OcurrentUser;

    public FrendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_frends, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();
        OcurrentUser = mAuth.getCurrentUser();
        OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());



        recyclerView = mView.findViewById(R.id.FreindRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue("true");
        }

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("frends")
                .child(id)
                .limitToLast(50);

        FirebaseRecyclerOptions<DataFrends> options =
                new FirebaseRecyclerOptions.Builder<DataFrends>()
                        .setQuery(query, DataFrends.class)
                        .build();

        FirebaseRecyclerAdapter<DataFrends,FrendsViewHolder> adapter = new FirebaseRecyclerAdapter<DataFrends,FrendsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final FrendsViewHolder holder, int position, @NonNull DataFrends user) {
                final int pos = position;
                holder.setData(user.getDate());
                final String userId = getRef(pos).getKey();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String thumImage = dataSnapshot.child("thumImage").getValue().toString();
                        String online = dataSnapshot.child("online").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        holder.setName(name);
                        holder.setImage(thumImage);
                        holder.setOnline(userId);
                        holder.setData(status);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        CharSequence[] charSequence = new CharSequence[]{"Open profile","Send massege"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setTitle("Select Option");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    Intent intent = new Intent(getContext(), ProfileActivity.class);

                                    intent.putExtra("USER_CLICKED_ID", userId);
                                    startActivity(intent);
                                }
                                else{
                                   Intent intent = new Intent(getContext(), MssegeActivity.class);
                                   intent.putExtra("USER_CLICKED_ID", userId);
                                   startActivity(intent);
                                }
                            }
                        });

                        builder.show();

                        /**/
                    }
                });

            }

            @Override
            public FrendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.au_single_row_layout, parent, false);

                return new FrendsViewHolder(mView);
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public class FrendsViewHolder extends RecyclerView.ViewHolder{
        View mView;


        public FrendsViewHolder(View itemView) {
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
