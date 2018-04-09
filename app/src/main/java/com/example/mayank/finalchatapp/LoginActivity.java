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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout log_email,log_password;
    private FirebaseAuth mAuth;
    private Toolbar LoginMenuBar;
    private ProgressDialog progressBar;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        LoginMenuBar =  findViewById(R.id.login_menubar);
        setSupportActionBar(LoginMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        log_email = findViewById(R.id.login_email);
        log_password = findViewById(R.id.login_password);

        login = findViewById(R.id.log_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_s = log_email.getEditText().getText().toString();
                String password_s = log_password.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email_s)&&!TextUtils.isEmpty(password_s))
                {
                    progressBar = new ProgressDialog(LoginActivity.this);
                    progressBar.setMessage("Wait until Regiter process not complete!");
                    progressBar.setTitle("Register processing!");
                    progressBar.setCanceledOnTouchOutside(true);
                    progressBar.show();

                    loginUser(email_s,password_s);
                }
                else{
                    Toast.makeText(LoginActivity.this,"fill All Entrys",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    private void loginUser(String email_s, String password_s) {
        mAuth.signInWithEmailAndPassword(email_s,password_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this,"Login Succesfull",Toast.LENGTH_LONG).show();
                }
                else {
                    progressBar.hide();
                    Toast.makeText(LoginActivity.this,"Login Unsuccesfull",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
