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
import android.widget.TextView;
import android.widget.Toast;

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
public class RequestFragment extends Fragment {

    View mView;
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String id;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_request, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();

        recyclerView = mView.findViewById(R.id.RequestRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mView;

    }
    public void onStart() {
        super.onStart();


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("frends_request")
                .child(id)
                .limitToLast(50);

        FirebaseRecyclerOptions<DataRequest> options =
                new FirebaseRecyclerOptions.Builder<DataRequest>()
                        .setQuery(query, DataRequest.class)
                        .build();


        FirebaseRecyclerAdapter<DataRequest,RequestViewHolder> adapter = new FirebaseRecyclerAdapter<DataRequest,RequestFragment.RequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final RequestFragment.RequestViewHolder holder, int position, @NonNull DataRequest user) {
                final int pos = position;
                holder.setData(user.getRequestType());
                final String userId = getRef(pos).getKey();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String thumImage = dataSnapshot.child("thumImage").getValue().toString();

                        holder.setName(name);
                        holder.setImage(thumImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(),"error 1",Toast.LENGTH_LONG).show();
                    }
                });

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        Intent intent = new Intent(getContext(), ProfileActivity.class);

                        intent.putExtra("USER_CLICKED_ID", userId);
                        startActivity(intent);

                        /**/
                    }
                });

            }

            @Override
            public RequestFragment.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.au_single_row_layout, parent, false);

                return new RequestFragment.RequestViewHolder(mView);
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public class RequestViewHolder extends RecyclerView.ViewHolder{
        View mView;


        public RequestViewHolder(View itemView) {
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


        public View getView(){
            return this.mView;
        }

    }


}
