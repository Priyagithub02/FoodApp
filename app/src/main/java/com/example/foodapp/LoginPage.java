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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class LoginPage extends AppCompatActivity {
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
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.loginmail);
        password = findViewById(R.id.loginpassword);
        register = findViewById(R.id.registerbtn);
        loginbuton = findViewById(R.id.loginbutton);
        loginerror = findViewById(R.id.errorlogin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child("USER REGISTER");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this,MainActivity.class);
                startActivity(i);
            }
        });

        loginbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mAuth.getCurrentUser().getUid();
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                databaseReference.orderByChild(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserList userList = dataSnapshot.getValue(UserList.class);

                            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                                loginerror.setVisibility(View.VISIBLE);
                            }
                            if(mail.equals(userList.getMail()) && pass.equals(userList.getPass())){
                                i = i+1;
                            }
                        }
                        if (i == 1) {
                            Toast.makeText(LoginPage.this, "User already logged in", Toast.LENGTH_SHORT).show();
                            Intent a = new Intent(LoginPage.this, HomePage.class);
                            startActivity(a);
                        }else {
                            Toast.makeText(LoginPage.this, "E-Mail or password incorrect", Toast.LENGTH_SHORT).show();
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
