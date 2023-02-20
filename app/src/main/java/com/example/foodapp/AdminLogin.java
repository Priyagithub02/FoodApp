package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class AdminLogin extends AppCompatActivity {
    EditText email,password;
    TextView register, loginerror;
    Button loginbuton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Integer i = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email = findViewById(R.id.loginmail);
        password = findViewById(R.id.loginpassword);
        register = findViewById(R.id.registerbtn);
        loginbuton = findViewById(R.id.loginbutton);
        loginerror = findViewById(R.id.errorlogin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOP ADMIN");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminLogin.this,AdminRegister.class);
                startActivity(i);
            }
        });
        loginbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminID = "admin" + new Date().getTime();
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                databaseReference.orderByChild(adminID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            AdminList adminList = dataSnapshot.getValue(AdminList.class);

                            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                                loginerror.setVisibility(View.VISIBLE);
                            }
                            if(mail.equals(adminList.getEmail()) && pass.equals(adminList.getPassword())){
                                i = i+1;
                            }
                        }
                        if (i == 1) {
                            Toast.makeText(AdminLogin.this, "User already logged in", Toast.LENGTH_SHORT).show();
                            Intent a = new Intent(AdminLogin.this, HomePage.class);
                            startActivity(a);
                        }else {
                            Toast.makeText(AdminLogin.this, "E-Mail or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}