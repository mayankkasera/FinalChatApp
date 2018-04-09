package com.example.mayank.finalchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private DatabaseReference OdatabaseReference;
    private TabLayout tabLayout;
    private FirebaseUser OcurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        OcurrentUser = mAuth.getCurrentUser();


        //OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());


        toolbar = findViewById(R.id.TestChatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Crezy Chat App");

        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));

        tabLayout = findViewById(R.id.main_tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if(OcurrentUser==null){
            uiUpdate();
        }
        else{
            OdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
            OdatabaseReference.child("online").setValue("true");
        }

    }



    private void uiUpdate() {
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }


  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_main_menu,menu);
        return true;
    }

      @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.my_menu_logout)
        {
            Toast.makeText(MainActivity.this,"lout",Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            uiUpdate();
        }
        if(item.getItemId()==R.id.my_menu_account_setting) {
            startActivity(new Intent(this,AccountActivity.class));
        }
        if(item.getItemId()==R.id.my_menu_all_users) {
            startActivity(new Intent(this,AllUserActivity.class));
        }
        return true;
    }

   @Override
    protected void onDestroy() {
       super.onDestroy();
    //   OdatabaseReference.child("online").setValue(ServerValue.TIMESTAMP);

   }
}
