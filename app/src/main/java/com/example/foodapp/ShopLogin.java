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

public class ShopLogin extends AppCompatActivity {
    EditText shop_email,shop_password;
    TextView register, login_error;
    Button loginbuton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Integer i = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);
        shop_email = findViewById(R.id.shoploginmail);
        shop_password = findViewById(R.id.shoploginpassword);
        register = findViewById(R.id.registerbuttn);
        loginbuton = findViewById(R.id.shoploginbutton);
        login_error = findViewById(R.id.error_login);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPS").child("SHOP REGISTERS");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShopLogin.this,ShopRegister.class);
                startActivity(i);
            }
        });

        loginbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopid = mAuth.getCurrentUser().getUid();
                String shopmail = shop_email.getText().toString().trim();
                String shoppass = shop_password.getText().toString().trim();

                databaseReference.orderByChild(shopid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ShopList shopList = dataSnapshot.getValue(ShopList.class);

                            if(shop_email.getText().toString().isEmpty() || shop_password.getText().toString().isEmpty()){
                                login_error.setVisibility(View.VISIBLE);
                            }
                            if(shopmail.equals(shopList.getShopmail()) && shoppass.equals(shopList.getPassword())){
                                i = i+1;
                            }
                        }
                        if (i == 1) {
                            Toast.makeText(ShopLogin.this, "User already logged in", Toast.LENGTH_SHORT).show();
                            Intent c = new Intent(ShopLogin.this, HomePage.class);
                            startActivity(c);
                        }else {
                            Toast.makeText(ShopLogin.this, "E-Mail or password incorrect", Toast.LENGTH_SHORT).show();
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
