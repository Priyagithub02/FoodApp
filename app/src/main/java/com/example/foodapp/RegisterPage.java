package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterPage extends AppCompatActivity {
    Button user, shop,admin,delivery_person;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        user = findViewById(R.id.userregister);
        shop = findViewById(R.id.shopregister);
        admin = findViewById(R.id.shopadmin);
        delivery_person = findViewById(R.id.deliverperson);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this,MainActivity.class);
                startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPage.this,ShopRegister.class);
                startActivity(i);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(RegisterPage.this,AdminRegister.class);
                startActivity(a);
            }
        });
        delivery_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(RegisterPage.this,DeliveryPersonRegister.class);
                startActivity(b);
            }
        });
    }
}