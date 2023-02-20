package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ShopDetails extends AppCompatActivity {
    EditText firstcontact_name,firstcontact_mail,firstcontact_phone,firstcontact_idproof;
    ImageView upload_IDproof;
    Button submit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        firstcontact_name = findViewById(R.id.firstcontactname);
        firstcontact_mail = findViewById(R.id.firstcontactmail);
        firstcontact_phone = findViewById(R.id.firstcontactphone);
        firstcontact_idproof = findViewById(R.id.firstcontactid);
        upload_IDproof = findViewById(R.id.upload_ID);
        submit = findViewById(R.id.submitbutton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        upload_IDproof.setOnClickListener(new View.OnClickListener() {
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
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            firstcontact_idproof.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firstcontact_name.getText().toString().isEmpty()||firstcontact_mail.getText().toString().isEmpty()||firstcontact_phone.getText().toString().isEmpty()||firstcontact_idproof.getText().toString().isEmpty()){
                        Toast.makeText(ShopDetails.this, "Enter required details", Toast.LENGTH_SHORT).show();
                    }else if(!firstcontact_mail.getText().toString().matches(emailPattern)){
                        Toast.makeText(ShopDetails.this, "Enter valid Email id", Toast.LENGTH_SHORT).show();
                    }else if(!(firstcontact_phone.length() == 10)){
                        Toast.makeText(ShopDetails.this, "Enter valid phone number", Toast.LENGTH_SHORT).show();
                    }else {
                        fileUpload(data.getData());
                    }
                }
            });
        }
    }
    private void fileUpload(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File loading");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" +System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                String uri1 = data.getPath();
                String name = firstcontact_name.getText().toString();
                String mail = firstcontact_mail.getText().toString();
                String phone = firstcontact_phone.getText().toString();
                ShopDetailsList detailsList = new ShopDetailsList(name,mail,phone,uri1);
                databaseReference.child("SHOPS").child("SHOP DETAILS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(detailsList);
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded" +(int) progress + "%");
                Intent intent = new Intent(ShopDetails.this,ShopPayment.class);
                startActivity(intent);
            }
        });
    }
}