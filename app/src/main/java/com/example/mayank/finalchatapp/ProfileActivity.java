package com.example.mayank.finalchatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar ProfileMenuBar;
    private TextView status,name;
    private Button FrendRequst,DclineRquest;
    private ImageView imageView;
    private ProgressDialog progressBar;
    private String curentState = "notfriend";
    private DatabaseReference databaseReference;
    private DatabaseReference freinddatabase;
    private FirebaseUser curentUser;
    String otherUser;

    FirebaseAuth mAuth;
    private DatabaseReference OdatabaseReference;
    private FirebaseUser OcurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);





        ProfileMenuBar =  findViewById(R.id.Profilemenubar);
        setSupportActionBar(ProfileMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name_pro);
        status = findViewById(R.id.status_pro);
        imageView = findViewById(R.id.image_pro);
        FrendRequst = findViewById(R.id.Fre_Req);
        DclineRquest = findViewById(R.id.Dec_Fre);

        DclineRquest.setVisibility(View.INVISIBLE);


        curentUser = FirebaseAuth.getInstance().getCurrentUser();
        otherUser = getIntent().getStringExtra("USER_CLICKED_ID");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("frends_request");
        freinddatabase = FirebaseDatabase.getInstance().getReference().child("frends");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(otherUser);




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                String name = (String) dataSnapshot.child("name").getValue();
                String status =(String) dataSnapshot.child("status").getValue();
                String image =(String) dataSnapshot.child("thumImage").getValue();

                ProfileActivity.this.status.setText(status);
                ProfileActivity.this.name.setText(name);


                    Toast.makeText(ProfileActivity.this,"yes", Toast.LENGTH_LONG);
                    Picasso.with(ProfileActivity.this).load(image).into(imageView);
                    Toast.makeText(ProfileActivity.this,"Coplete",Toast.LENGTH_LONG).show();


                    databaseReference.child(curentUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(ProfileActivity.this, "c bnn", Toast.LENGTH_SHORT).show();

                            if(dataSnapshot.hasChild(otherUser)){
                                String req_type = dataSnapshot.child(otherUser).child("requestType").getValue().toString();

                                Toast.makeText(ProfileActivity.this, req_type, Toast.LENGTH_SHORT).show();
                                if(req_type.equals("recive"))
                                {
                                    Toast.makeText(ProfileActivity.this, req_type+"recive", Toast.LENGTH_SHORT).show();

                                    curentState = "req_recived";
                                    FrendRequst.setText("Acs Request");
                                    DclineRquest.setVisibility(View.VISIBLE);
                                    DclineRquest.setEnabled(true);

                                }
                                else if(req_type.equals("send")){
                                    curentState = "requstSend";
                                    FrendRequst.setText("Cencle Req");
                                    DclineRquest.setVisibility(View.INVISIBLE);
                                    DclineRquest.setEnabled(false);
                                }
                            }
                            else{
                                freinddatabase.child(curentUser.getUid()).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(otherUser)){
                                            curentState = "friend";
                                            FrendRequst.setText("UnFreid Per");
                                            DclineRquest.setVisibility(View.INVISIBLE);
                                            DclineRquest.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(ProfileActivity.this,"error 1",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ProfileActivity.this,"error 2",Toast.LENGTH_LONG).show();
                        }
                    });





                ////Dcline freind requst

                DclineRquest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(curentState.equals("req_recived")){
                            databaseReference.child(curentUser.getUid()).child(otherUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        databaseReference.child(otherUser).child(curentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FrendRequst.setEnabled(true);
                                                curentState = "notfriend";
                                                FrendRequst.setText("Freid Requast");
                                                DclineRquest.setVisibility(View.INVISIBLE);
                                                DclineRquest.setEnabled(false);
                                                Toast.makeText(ProfileActivity.this,"send succesfull",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(ProfileActivity.this,"not succesfull ",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }

                    }
                });



                /// frend request


                FrendRequst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FrendRequst.setEnabled(false);

                        if(curentState.equals("notfriend")){
                            databaseReference.child(curentUser.getUid()).child(otherUser)
                                    .child("requestType").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        databaseReference.child(otherUser).child(curentUser.getUid())
                                                .child("requestType").setValue("recive").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FrendRequst.setEnabled(true);
                                                curentState = "requstSend";
                                                FrendRequst.setText("Cencle Req");
                                                DclineRquest.setVisibility(View.INVISIBLE);
                                                DclineRquest.setEnabled(false);
                                                Toast.makeText(ProfileActivity.this,"send succesfull",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(ProfileActivity.this,"not send ",Toast.LENGTH_LONG).show();
                        }



                        //------------ cencel Friend Request------------


                        if(curentState.equals("requstSend")){
                            databaseReference.child(curentUser.getUid()).child(otherUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        databaseReference.child(otherUser).child(curentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FrendRequst.setEnabled(true);
                                                curentState = "notfriend";
                                                FrendRequst.setText("Freid Requast");
                                                DclineRquest.setVisibility(View.INVISIBLE);
                                                DclineRquest.setEnabled(false);
                                                Toast.makeText(ProfileActivity.this,"send succesfull",Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(ProfileActivity.this,"not succesfull ",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }



                        //------------ Request Recived State------------

                        if(curentState.equals("req_recived")){
                            final String date = DateFormat.getDateTimeInstance().format(new Date());
                            freinddatabase.child(curentUser.getUid()).child(otherUser).child("date").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        freinddatabase.child(otherUser).child(curentUser.getUid()).child("date").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                databaseReference.child(curentUser.getUid()).child(otherUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            databaseReference.child(otherUser).child(curentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    FrendRequst.setEnabled(true);
                                                                    curentState = "friend";
                                                                    FrendRequst.setText("UnFreid Per");
                                                                    DclineRquest.setVisibility(View.INVISIBLE);
                                                                    DclineRquest.setEnabled(false);
                                                                    Toast.makeText(ProfileActivity.this,"send succesfull",Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                        else{
                                                            Toast.makeText(ProfileActivity.this,"not succesfull ",Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }

                        if(curentState.equals("friend")){
                            freinddatabase.child(curentUser.getUid()).child(otherUser).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    freinddatabase.child(otherUser).child(curentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            curentState = "notfriend";
                                            FrendRequst.setText("Freind Request");
                                        }
                                    });
                                }
                            });
                        }

                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
        if(OcurrentUser!=null) {
            Toast.makeText(ProfileActivity.this,"online update",Toast.LENGTH_LONG).show();

            OdatabaseReference.child("online").setValue("true");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue("true");
        }
    }
}
