package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopPayment extends AppCompatActivity {
    EditText account_number,ifsccode,branch,acc_holder_name,upi_id;
    Button submit_btn;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_payment);
        account_number = findViewById(R.id.accno);
        ifsccode = findViewById(R.id.ifsc);
        branch = findViewById(R.id.branchname);
        acc_holder_name = findViewById(R.id.holdername);
        upi_id = findViewById(R.id.upiid);
        submit_btn = findViewById(R.id.submit_button);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_number.getText().toString().isEmpty()||ifsccode.getText().toString().isEmpty()||branch.getText().toString().isEmpty()||acc_holder_name.getText().toString().isEmpty()||upi_id.getText().toString().isEmpty()){
                    Toast.makeText(ShopPayment.this, "Enter Payment Details", Toast.LENGTH_SHORT).show();
                }else{
                    String accno = account_number.getText().toString();
                    String ifsc = ifsccode.getText().toString();
                    String branch_name = branch.getText().toString();
                    String holder_name = acc_holder_name.getText().toString();
                    String upiid = upi_id.getText().toString();
                    ShopPaymentList paymentList = new ShopPaymentList(accno,ifsc,branch_name,holder_name,upiid);
                    databaseReference.child("SHOPS").child("SHOP PAYMENTS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(paymentList);
                    //Intent intent = new Intent(ShopPayment.this,ShopLogin.class);
                    //startActivity(intent);
                }
            }
        });
    }
}