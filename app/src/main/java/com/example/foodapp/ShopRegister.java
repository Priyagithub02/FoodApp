package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.regex.Pattern;

public class ShopRegister extends AppCompatActivity {
    EditText shopname,shopemail,shoppassword, confirmpassword,shopmobile,shoptelephone;
    TextView loginbutton, error, mobileerr, password_error,confirmpassword_error;
    Spinner categories;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final Pattern PASSWORD_PASSWORD = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + ".{6,}" + "$");
    Button registerbutton;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);

        shopname = findViewById(R.id.shopname);
        shopemail = findViewById(R.id.shopmail);
        shoppassword = findViewById(R.id.shoppass);
        confirmpassword = findViewById(R.id.shopconfirmpass);
        shopmobile = findViewById(R.id.shopmobile);
        shoptelephone = findViewById(R.id.shoptelephone);
        registerbutton = findViewById(R.id.register);
        error = findViewById(R.id.shoperror);
        password_error = findViewById(R.id.passworderror);
        confirmpassword_error = findViewById(R.id.confirmpassworderror);
        mobileerr = findViewById(R.id.mobileerror);
        loginbutton = findViewById(R.id.login);
        categories = findViewById(R.id.category);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories,R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        categories.setAdapter(adapter);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        registerbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               performAuthentication();
           }
       });
    }
    private void performAuthentication() {
        String shop_name = shopname.getText().toString();
        String shop_mail = shopemail.getText().toString();
        String shop_password = shoppassword.getText().toString();
        String confirm_password = confirmpassword.getText().toString();
        String shop_mobile = shopmobile.getText().toString();
        String shop_category = categories.getSelectedItem().toString();
        if(shop_name.isEmpty()||shop_mail.isEmpty()||shop_mobile.isEmpty()||shop_category.isEmpty()||shop_password.isEmpty()||confirm_password.isEmpty()){
            error.setVisibility(View.VISIBLE);
        }else if (!shop_mail.matches(emailPattern)){
            Toast.makeText(ShopRegister.this, "Enter valid E-Mail", Toast.LENGTH_SHORT).show();
        }else if(!PASSWORD_PASSWORD.matcher(shop_password).matches()){
            password_error.setVisibility(View.VISIBLE);
        }else if(!shop_password.matches(confirm_password)){
            confirmpassword_error.setVisibility(View.VISIBLE);
        }else if(!(shop_mobile.length() == 10)){
            mobileerr.setVisibility(View.VISIBLE);
        }else {
            ViewDialogshopverify shopverify = new  ViewDialogshopverify();
            shopverify.showDialog(ShopRegister.this);

            mAuth.createUserWithEmailAndPassword(shop_mail,shop_password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShopRegister.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                        String shopId = "shop" + new Date().getTime();
                        String shop_name = shopname.getText().toString();
                        String shopmail = shopemail.getText().toString();
                        String password = shoppassword.getText().toString();
                        String mobile = shopmobile.getText().toString();
                        String telephone = shoptelephone.getText().toString();
                        String shopcategory = categories.getSelectedItem().toString();
                        ShopList shopList = new ShopList(shopId,shop_name,shopmail,password,"",mobile,shopcategory);
                        databaseReference.child("SHOPS").child("SHOP REGISTERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(shopList);
                    } else {
                        Toast.makeText(ShopRegister.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private class ViewDialogshopverify {
        public void showDialog(ShopRegister shopRegister) {
            final Dialog dialog = new Dialog(ShopRegister.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.activity_optverify);

            ImageView closebtn = dialog.findViewById(R.id.close);
            Button verifybtn = dialog.findViewById(R.id.verify);

            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        dialog.dismiss();
                    }
            });
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mail = shopemail.getText().toString().trim();
                    String password = shoppassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if ((FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())) {
                                Intent a = new Intent(ShopRegister.this, ShopAddress.class);
                                startActivity(a);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(shopRegister, "Verify E-Mail to Continue", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}