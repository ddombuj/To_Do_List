package com.ddombuj.to_do_list.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ddombuj.to_do_list.MainMenu;
import com.ddombuj.to_do_list.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText nameEt, emailEt, passwordEt, confirmEt;
    Button bt_registerUser;
    TextView alreadyHaveAccount;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    //String created for validation
    String name = "", email = "", password = "", confirmPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Register");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        nameEt = findViewById(R.id.nameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmEt = findViewById(R.id.confirmEt);
        bt_registerUser = findViewById(R.id.bt_registerUser);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Registering user...");
        progressDialog.setCanceledOnTouchOutside(false);

        bt_registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }

    private void validateData(){
        name = nameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmEt.getText().toString().trim();

        boolean isValid = true;

        //Name
        if (TextUtils.isEmpty(name)){
            nameEt.setError("Name is required");
            isValid = false;
        } else {
            nameEt.setError(null);
        }

        //Email
        if (TextUtils.isEmpty(email)){
            emailEt.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.setError("Enter a valid email address");
            isValid = false;
        } else {
            emailEt.setError(null);
        }

        //Password
        if (password.isEmpty()){
            passwordEt.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6){
            passwordEt.setError("Password should be at least 6 characters long");
            isValid = false;
        } else {
            passwordEt.setError(null);
        }

        //Password confirmation
        if (!confirmPassword.equals(password)){
            confirmEt.setError("Passwords must match");
            isValid = false;
        } else {
            confirmEt.setError(null);
        }

        if(isValid){
            createAccount();
        }
    }

    private void createAccount() {
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //Create user in Firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        SaveInformation();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void SaveInformation() {
        progressDialog.setMessage("Saving user information...");
        progressDialog.dismiss();

        //Obtaining actual user information
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Data = new HashMap<>();
        Data.put("uid", uid);
        Data.put("email", email);
        Data.put("name", name);
        Data.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(uid)
                .setValue(Data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Account created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, MainMenu.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}