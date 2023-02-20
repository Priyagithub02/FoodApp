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

public class DeliveryPersonLogin extends AppCompatActivity {
    EditText deliverypersonemail, deliverypersonpassword;
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
        setContentView(R.layout.activity_delivery_person_login);

        deliverypersonemail = findViewById(R.id.loginmail);
        deliverypersonpassword = findViewById(R.id.loginpassword);
        register = findViewById(R.id.registerbtn);
        loginbuton = findViewById(R.id.loginbutton);
        loginerror = findViewById(R.id.errorlogin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DELIVERY PERSON");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeliveryPersonLogin.this, DeliveryPersonRegister.class);
                startActivity(i);
            }
        });
        loginbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deliverypersonId = "deliveryperson" +new Date().getTime();
                String deliveryperson_mail = deliverypersonemail.getText().toString().trim();
                String deliveryperson_pass = deliverypersonpassword.getText().toString().trim();

                databaseReference.orderByChild(deliverypersonId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DeliveryPersonList deliveryPersonList = dataSnapshot.getValue(DeliveryPersonList.class);

                            if (deliverypersonemail.getText().toString().isEmpty() || deliverypersonpassword.getText().toString().isEmpty()) {
                                loginerror.setVisibility(View.VISIBLE);
                            }
                            if (deliveryperson_mail.equals(deliveryPersonList.getDeliveryemail()) && deliveryperson_pass.equals(deliveryPersonList.getDeliverypassword())) {
                                i = i + 1;
                            }
                        }
                        if (i == 1) {
                            Toast.makeText(DeliveryPersonLogin.this, "User already logged in", Toast.LENGTH_SHORT).show();
                            Intent a = new Intent(DeliveryPersonLogin.this, HomePage.class);
                            startActivity(a);
                        } else {
                            Toast.makeText(DeliveryPersonLogin.this, "E-Mail or password incorrect", Toast.LENGTH_SHORT).show();
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