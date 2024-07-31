package com.ddombuj.to_do_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {

    TextView namesMain, emailMain;
    ProgressBar progressBarDataMain;
    DatabaseReference users;

    Button bt_signOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agenda Online");

        namesMain = findViewById(R.id.namesMain);
        emailMain = findViewById(R.id.emailMain);
        progressBarDataMain = findViewById(R.id.progressBarDataMain);

        users = FirebaseDatabase.getInstance().getReference("Users");

        bt_signOut = findViewById(R.id.bt_signOut);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        bt_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }

    @Override
    protected void onStart() {
        checkSignIn();
        super.onStart();
    }

    private void checkSignIn(){
        if(user != null){
            loadData();
        } else {
            startActivity(new Intent(MainMenu.this, MainActivity.class));
            finish();
        }
    }

    private void loadData(){
        users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //If the user exists, lets show the data
                if(snapshot.exists()){
                    //Hide the progress bar
                    progressBarDataMain.setVisibility(View.GONE);
                    //Both textview's are shown
                    namesMain.setVisibility(View.VISIBLE);
                    emailMain.setVisibility(View.VISIBLE);

                    //Get the data
                    String names = "" + snapshot.child("name").getValue();
                    String email = "" + snapshot.child("email").getValue();

                    //Set the data
                    namesMain.setText(names);
                    emailMain.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void exitApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenu.this, MainActivity.class));
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
    }
}