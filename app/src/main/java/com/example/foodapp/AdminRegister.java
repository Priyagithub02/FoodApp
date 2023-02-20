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
import java.util.Objects;
import java.util.regex.Pattern;

public class AdminRegister extends AppCompatActivity {
    EditText admin_name, admin_mail, admin_phone, password, confirm_password, id_proof;
    Button register_button;
    ImageView upload_idproof;
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
        setContentView(R.layout.activity_admin_register);
        admin_name = findViewById(R.id.adminname);
        admin_mail = findViewById(R.id.adminmail);
        admin_phone = findViewById(R.id.adminphone);
        password = findViewById(R.id.adminpass);
        confirm_password = findViewById(R.id.adminconfirmpass);
        id_proof = findViewById(R.id.idproof);
        register_button = findViewById(R.id.adminregbutton);
        upload_idproof = findViewById(R.id.uploadIDproof);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        upload_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            id_proof.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));

            register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performAuth();
                    uploadPDF(data.getData());
                }
            });
        }
    }
    private void performAuth() {
        String adminname = admin_name.getText().toString();
        String adminmail = admin_mail.getText().toString();
        String adminphoneno = admin_phone.getText().toString();
        String pass = password.getText().toString();
        String confirmpass = confirm_password.getText().toString();
        TextView mailiderr = findViewById(R.id.mailiderror);

        if (adminname.isEmpty() || adminmail.isEmpty() || adminphoneno.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()) {
            Toast.makeText(this, "Enter User Details", Toast.LENGTH_SHORT).show();
        } else if (!adminmail.matches(emailPattern)) {
            mailiderr.setVisibility(View.VISIBLE);
        } else if (!PASSWORD_PASSWORD.matcher(pass).matches()) {
            Toast.makeText(this, "Password must contain more than 5 characters with a mixture of alphabets,numbers and special characters", Toast.LENGTH_SHORT).show();
        } else if (!pass.matches(confirmpass)) {
            Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show();
        } else if (!(adminphoneno.length() == 10)) {
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AdminRegister.ViewDialogotpverify viewDialogotpverify = new AdminRegister.ViewDialogotpverify();
            viewDialogotpverify.showDialog(AdminRegister.this);

            mAuth.createUserWithEmailAndPassword(adminmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminRegister.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    private class ViewDialogotpverify {
        public void showDialog(AdminRegister adminRegister) {
            final Dialog dialog = new Dialog(AdminRegister.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.admin_otp_verify);

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
                    String mail = admin_mail.getText().toString().trim();
                    String pass = password.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if ((Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified())) {
                                Intent a = new Intent(AdminRegister.this, AdminLogin.class);
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
    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File loading");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String adminId = "admin" + new Date().getTime();
                String shopId = "shop" + new Date().getTime();
                String name = admin_name.getText().toString();
                String mail = admin_mail.getText().toString();
                String phonenum = admin_phone.getText().toString();
                String pass = password.getText().toString();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri uri = uriTask.getResult();
                AdminList adminList = new AdminList(adminId,name,mail,phonenum,pass,shopId,"Admin",uri.getPath());
                databaseReference.child("SHOP ADMIN").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(adminList);
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
}