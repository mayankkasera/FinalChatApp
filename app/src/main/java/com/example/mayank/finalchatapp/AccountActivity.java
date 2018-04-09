package com.example.mayank.finalchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView status,name;
    private Button Change_Status,Change_img;
    private int GALLERY_PICK;
    private StorageReference mStorageRef;
    private ProgressDialog progressBar;
    private CircleImageView SettingImg;
    FirebaseAuth mmAuth;
    private DatabaseReference databaseReference;
    FirebaseUser currentUser;



    private DatabaseReference OdatabaseReference;
    private FirebaseUser OcurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        OcurrentUser = mAuth.getCurrentUser();
        OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());


        String id = mAuth.getCurrentUser().getUid();

        mmAuth = FirebaseAuth.getInstance();
        currentUser = mmAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(id);

        status = findViewById(R.id.setting_status);
        name = findViewById(R.id.setting_name);
        SettingImg = findViewById(R.id.setting_img);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                String name = (String) dataSnapshot.child("name").getValue();
                String status =(String) dataSnapshot.child("status").getValue();
                String image =(String) dataSnapshot.child("thumImage").getValue();

                AccountActivity.this.status.setText(status);
                AccountActivity.this.name.setText(name);

                if(!image.equals("https://firebasestorage.googleapis.com/v0/b/testchat-7adcc.appspot.com/o/profile%2FrUkeMkxVPIaHDfD5xG4kLVF3yMq2.jpg?alt=media&token=c96c13fb-99d5-4d76-b83c-dfb74a3c413a")) {
                    Toast.makeText(AccountActivity.this,"yes",Toast.LENGTH_LONG);
                    Picasso.with(AccountActivity.this).load(image).into(SettingImg);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AccountActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });


        Change_Status = findViewById(R.id.Btn_Change_Status);
        Change_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(AccountActivity.this,StatusActivity.class);
               intent.putExtra("Status",((TextView)findViewById(R.id.setting_status)).getText());
               startActivity(intent);
            }
        });

        Change_img = findViewById(R.id.Btn_Change_Img);
        Change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"),GALLERY_PICK);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK)
        {

            CropImage.activity(data.getData())
                    .setAspectRatio(1,1)
                    .start(this);
            // Toast.makeText(AccountActivity.this,uri,Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progressBar = new ProgressDialog(AccountActivity.this);
                progressBar.setMessage("Wait until Regiter process not complete!");
                progressBar.setTitle("Register processing!");
                progressBar.setCanceledOnTouchOutside(true);
                progressBar.show();



                Uri resultUri = result.getUri();

                File actualImageFile = new File(resultUri.getPath());
                Bitmap compressedImageBitmap = null;
                try {
                    compressedImageBitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(actualImageFile);
                } catch (IOException e) {
                    Toast.makeText(AccountActivity.this,"Exception ",Toast.LENGTH_LONG);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumdata = baos.toByteArray();


                mAuth = FirebaseAuth.getInstance();
                String id = mAuth.getCurrentUser().getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference().child("Users").child(id);

                mStorageRef = FirebaseStorage.getInstance().getReference().child("profile").child(id+".jpg");
                final StorageReference thumStorage = FirebaseStorage.getInstance().getReference().child("profile").child("thum_image").child(id+".jpg");

                mStorageRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            UploadTask uploadTask = thumStorage.putBytes(thumdata);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumtask) {
                                    String image_url = task.getResult().getDownloadUrl().toString();
                                    String thum_image_url = thumtask.getResult().getDownloadUrl().toString();

                                    if(thumtask.isSuccessful()){

                                        Map map = new HashMap();
                                        map.put("image",image_url);
                                        map.put("thumImage",thum_image_url);

                                        myRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressBar.dismiss();
                                                    Toast.makeText(AccountActivity.this,"Complete",Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    progressBar.hide();
                                                    Toast.makeText(AccountActivity.this,"Not upload",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(AccountActivity.this," thum upload Not Complete",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(AccountActivity.this,"Not Complete",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
    protected void onStart() {
        super.onStart();
        if(OcurrentUser!=null) {
            OdatabaseReference.child("online").setValue("true");
        }
    }
}
