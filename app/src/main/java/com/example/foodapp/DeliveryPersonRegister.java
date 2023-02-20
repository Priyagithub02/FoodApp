package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.regex.Pattern;

public class DeliveryPersonRegister extends AppCompatActivity {
    EditText deliverperson_name, deliveryperson_mail, deliveryperson_phone, deliveryperson_password, confirm_password, deliveryperson_id_proof;
    Button register_buttn;
    ImageView upld_idproof;
    TextView delivery_person_login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final Pattern PASSWORD_PASSWORD = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + ".{6,}" + "$");
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_person_register);

        deliverperson_name = findViewById(R.id.deliverypersonname);
        deliveryperson_mail = findViewById(R.id.deliverypersonmail);
        deliveryperson_phone = findViewById(R.id.deliverypersonphone);
        deliveryperson_password = findViewById(R.id.deliverypersonpass);
        confirm_password = findViewById(R.id.deliveryperson_confirmpass);
        deliveryperson_id_proof = findViewById(R.id.deliveryperson_idproof);
        register_buttn = findViewById(R.id.regbutton);
        delivery_person_login = findViewById(R.id.deliveryperson_login);
        upld_idproof = findViewById(R.id.upload_IDproof);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        upld_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIdFile();
            }
        });
        delivery_person_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryPersonRegister.this,DeliveryPersonLogin.class);
                startActivity(intent);
            }
        });

    }

    private void selectIdFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            deliveryperson_id_proof.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));

            register_buttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performAuth();
                    uploadPDF(data.getData());
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File loading");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String delivery_personId = "deliveryperson" + new Date().getTime();
                String name = deliverperson_name.getText().toString();
                String mail = deliveryperson_mail.getText().toString();
                String phonenum = deliveryperson_phone.getText().toString();
                String pass = deliveryperson_password.getText().toString();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();
                DeliveryPersonList deliveryPersonList = new DeliveryPersonList(delivery_personId,name,mail,phonenum,pass,"Delivery Person",uri.getPath());
                databaseReference.child("DELIVERY PERSON").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(deliveryPersonList);
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded" + (int) progress + "%");
            }
        });
    }
    private void performAuth() {
        String delivery_person_name = deliverperson_name.getText().toString();
        String delivery_person_mail = deliveryperson_mail.getText().toString();
        String delivery_person_phoneno = deliveryperson_phone.getText().toString();
        String pass = deliveryperson_password.getText().toString();
        String confirmpass = confirm_password.getText().toString();
        TextView mailiderr = findViewById(R.id.mailiderror);

        if (delivery_person_name.isEmpty() || delivery_person_mail.isEmpty() || delivery_person_phoneno.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()) {
            Toast.makeText(this, "Enter User Details", Toast.LENGTH_SHORT).show();
        } else if (!delivery_person_mail.matches(emailPattern)) {
            mailiderr.setVisibility(View.VISIBLE);
        } else if (!PASSWORD_PASSWORD.matcher(pass).matches()) {
            Toast.makeText(this, "Password must contain more than 5 characters with a mixture of alphabets,numbers and special characters", Toast.LENGTH_SHORT).show();
        } else if (!pass.matches(confirmpass)) {
            Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
        } else if (!(delivery_person_phoneno.length() == 10)) {
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            DeliveryPersonRegister.Viewotpverify viewotpverify = new DeliveryPersonRegister.Viewotpverify();
            viewotpverify.showDialog(DeliveryPersonRegister.this);

            mAuth.createUserWithEmailAndPassword(delivery_person_mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(DeliveryPersonRegister.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    private class Viewotpverify {
        public void showDialog(DeliveryPersonRegister adminRegister) {
            final Dialog dialog = new Dialog(DeliveryPersonRegister.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.deliveryperson_otp_verify);

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
                    String mail = deliveryperson_mail.getText().toString().trim();
                    String pass = deliveryperson_password.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if ((FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())) {
                                Intent a = new Intent(DeliveryPersonRegister.this, DeliveryPersonLogin.class);
                                startActivity(a);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(adminRegister, "Verify E-Mail to Continue", Toast.LENGTH_SHORT).show();
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