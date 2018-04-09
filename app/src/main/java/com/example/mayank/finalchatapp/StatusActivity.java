package com.example.mayank.finalchatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar StatusMenuBar;
    private Button Btn_cha_Status;
    private TextInputLayout textInputLayout;
    private FirebaseAuth mAuth;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mAuth = FirebaseAuth.getInstance();

        StatusMenuBar =  findViewById(R.id.Status_menubar);
        setSupportActionBar(StatusMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInputLayout = findViewById(R.id.statusfiled);
        textInputLayout.getEditText().setText(getIntent().getStringExtra("Status"));

        Btn_cha_Status = findViewById(R.id.Btn_Cha_Status);
        Btn_cha_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar = new ProgressDialog(StatusActivity.this);
                progressBar.setMessage("Wait until be update status!");
                progressBar.setTitle("updating status!");
                progressBar.setCanceledOnTouchOutside(true);
                progressBar.show();

                String id = mAuth.getCurrentUser().getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Users").child(id);


                String status = textInputLayout.getEditText().getText().toString();

                myRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.dismiss();
                        }
                        else{
                            progressBar.hide();
                            Toast.makeText(StatusActivity.this,"Registration Unsuccesfull",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }
}
