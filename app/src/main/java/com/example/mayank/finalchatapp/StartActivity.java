package com.example.mayank.finalchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button Start_Reg_btn,Start_login_btn;
    Toolbar StartMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StartMenuBar =  findViewById(R.id.startmenubar);
        setSupportActionBar(StartMenuBar);
        getSupportActionBar().setTitle("Crazy Chat App");


        Start_Reg_btn = findViewById(R.id.StartReg);
        Start_Reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });


        Start_login_btn = findViewById(R.id.Start_Login_btn);
        Start_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });
    }
}
