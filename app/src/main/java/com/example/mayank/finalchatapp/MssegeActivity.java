package com.example.mayank.finalchatapp;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MssegeActivity extends AppCompatActivity {

    private Toolbar Massegemenubar;
    String otherUserId;
    String curentUserId;
    String otherUserName;

    DatabaseReference Root;
    TextView CusName,CusLastSeen;
    ImageView CusImage;


    RecyclerView recyclerView;

    ImageView SendImage,AddImage;
    EditText Massege;
    MessageAdapter messageAdapter;

    private  final List<SingleMessage> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    FirebaseAuth mAuth;
    private DatabaseReference OdatabaseReference;
    private FirebaseUser OcurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mssege);

        Massegemenubar =  findViewById(R.id.Massegemenubar);
        setSupportActionBar(Massegemenubar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar. setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custoolbar,null);
        actionBar.setCustomView(view);

        CusName = (TextView) view.findViewById(R.id.CusName);
        CusLastSeen = view.findViewById(R.id.CusLastSeen);
        CusImage = view.findViewById(R.id.CusImage);

        otherUserId = getIntent().getStringExtra("USER_CLICKED_ID");

        mAuth = FirebaseAuth.getInstance();
        curentUserId = mAuth.getUid();
        Root = FirebaseDatabase.getInstance().getReference();

        OcurrentUser = mAuth.getCurrentUser();
        OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());


        messageAdapter = new MessageAdapter(messageList);

        recyclerView = findViewById(R.id.MesRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);





        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);



        loadMessage();





        Root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Users").child(otherUserId).child("name").getValue().toString();
                CusName.setText(name);
                String lastSeen = dataSnapshot.child("Users").child(otherUserId).child("lastSeen").getValue().toString();
                String online = dataSnapshot.child("Users").child(otherUserId).child("online").getValue().toString();

                if(online.equals("true")){
                    CusLastSeen.setText("Online");
                }
                else{
                     long l = Long.parseLong(online);
                      String time = TimeSeen.getTimeAgo(l,getApplicationContext());
                    CusLastSeen.setText("Last Seen "+time+"");
                }


                String image = dataSnapshot.child("Users").child(otherUserId).child("thumImage").getValue().toString();
                Picasso.with(MssegeActivity.this).load(image).into(CusImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        AddImage = findViewById(R.id.AddImage);
        SendImage = findViewById(R.id.SendImage);
        Massege = findViewById(R.id.Massege);

        SendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMassege();
            }
        });



    }
    private void loadMessage() {
        Root.child("Message").child(curentUserId).child(otherUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SingleMessage singleMessage = dataSnapshot.getValue(SingleMessage.class);
                messageList.add(singleMessage);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMassege() {
        final String Message_s = Massege.getText().toString();
        if(!TextUtils.isEmpty(Message_s)){


            Massege.setText("");

            Root.child("Chat").child(curentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(otherUserId)){

                        Map chatMap = new HashMap();
                        chatMap.put("Seen","false");
                        chatMap.put("LastMessage", Message_s);

                        Map chatUserMap = new HashMap();
                        chatUserMap.put("Chat/"+curentUserId+"/"+otherUserId,chatMap);
                        chatUserMap.put("Chat/"+otherUserId+"/"+curentUserId,chatMap);

                        Root.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null)
                                    Toast.makeText(MssegeActivity.this,"error",Toast.LENGTH_LONG);
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





            String curentUserRef = "Message/"+curentUserId+"/"+otherUserId;
            String otherUserRef = "Message/"+otherUserId+"/"+curentUserId;

            DatabaseReference pushQurey = Root.child("Message").child(curentUserId)
                    .child(otherUserId).push();

            String pushId = pushQurey.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",Message_s);
            messageMap.put("seen","false");
            messageMap.put("type","text");
            messageMap.put("from",curentUserId);
            messageMap.put("time",ServerValue.TIMESTAMP);

            Map messageUserMap = new HashMap();
            messageUserMap.put(curentUserRef+"/"+pushId,messageMap);
            messageUserMap.put(otherUserRef+"/"+pushId,messageMap);

            Root.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null)
                        Toast.makeText(MssegeActivity.this,"error",Toast.LENGTH_LONG);
                }
            });
        }


    }

}
