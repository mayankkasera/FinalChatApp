package com.example.mayank.finalchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout Reg_name,Reg_email,Reg_password;
    private Button Reg_btn;
    private FirebaseAuth mAuth;
    private Toolbar RegisterMenuBar;
    private ProgressDialog progressBar;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        RegisterMenuBar =  findViewById(R.id.Registermenubar);
        setSupportActionBar(RegisterMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Reg_name = findViewById(R.id.Reg_name);
        Reg_email = findViewById(R.id.Reg_email);
        Reg_password  = findViewById(R.id.Reg_password);

        mAuth = FirebaseAuth.getInstance();

        Reg_btn = findViewById(R.id.Reg_Registration);
        Reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_s = Reg_name.getEditText().getText().toString();
                String email_s = Reg_email.getEditText().getText().toString();
                String password_s = Reg_password.getEditText().getText().toString();


                if(!TextUtils.isEmpty(email_s)&&!TextUtils.isEmpty(password_s)){
                    progressBar = new ProgressDialog(RegisterActivity.this);
                    progressBar.setMessage("Wait until Regiter process not complete!");
                    progressBar.setTitle("Register processing!");
                    progressBar.setCanceledOnTouchOutside(true);
                    progressBar.show();
                    doRegistration(name_s,email_s, password_s);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"fill All Entrys", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    void doRegistration(final String name_s,String email_s,String password_s){
        mAuth.createUserWithEmailAndPassword(email_s,password_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String id = mAuth.getCurrentUser().getUid();


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users").child(id);

                    HashMap<String,String> userInfo = new HashMap<String, String>();
                    userInfo.put("name",name_s);
                    userInfo.put("online","1");
                    userInfo.put("lastSeen","");
                    userInfo.put("status","hey there ! i am using crezy chat app");
                    userInfo.put("image","https://firebasestorage.googleapis.com/v0/b/testchat-7adcc.appspot.com/o/profile%2FrUkeMkxVPIaHDfD5xG4kLVF3yMq2.jpg?alt=media&token=c96c13fb-99d5-4d76-b83c-dfb74a3c413a");
                    userInfo.put("thumImage","https://firebasestorage.googleapis.com/v0/b/testchat-7adcc.appspot.com/o/profile%2FrUkeMkxVPIaHDfD5xG4kLVF3yMq2.jpg?alt=media&token=c96c13fb-99d5-4d76-b83c-dfb74a3c413a");
                    myRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(RegisterActivity.this,"Registration Succesfull",Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else {
                    progressBar.hide();
                    Toast.makeText(RegisterActivity.this,"Registration Unsuccesfull",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
