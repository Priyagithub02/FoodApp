package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.util.Calendar;

public class ShopDocuments extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText gst_in, shoprunning_years, regis_certificate, noccertificate, standard_certification,date_of_reg;
    Button nextbutton;
    ImageView registercertificate, date_of_registration, nocupload, safety_standards;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Integer i;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_documents);
        gst_in = findViewById(R.id.gstin);
        regis_certificate = findViewById(R.id.registerceritificate);
        standard_certification = findViewById(R.id.safetystandards);
        noccertificate = findViewById(R.id.noc);
        shoprunning_years = findViewById(R.id.shoprunning);
        nextbutton = findViewById(R.id.next);
        registercertificate = findViewById(R.id.uploadregcertificate);
        date_of_registration = findViewById(R.id.dateofregister);
        date_of_reg = findViewById(R.id.dor);
        nocupload = findViewById(R.id.uploadnoc);
        safety_standards = findViewById(R.id.uploadsafetystandard);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOP DOCUMENTS");
        mAuth = FirebaseAuth.getInstance();

        date_of_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
        registercertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                selectPDF();
            }
        });
        nocupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                selectPDF();
            }
        });
        safety_standards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 3;
                selectPDF();
            }
        });
        }
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),12);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (i == 1) {
            if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                regis_certificate.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
                uploadPDF(data.getData());
            }
        }else if(i == 2){
            if (requestCode == 12 && resultCode == RESULT_OK && data != null &&  data.getData() != null) {
                noccertificate.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
                uploadPDF2(data.getData());
            }
        }else if(i == 3){
            if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                standard_certification.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
                uploadPDF3(data.getData());
            }
        }
        else {
                Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show();
            }
    }
    private void uploadPDF3(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setTitle("File loading");
        //progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" +System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String dateof_reg = date_of_reg.getText().toString();
                String gstin = gst_in.getText().toString();
                String no_ofyears = shoprunning_years.getText().toString();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                String uri1 = data.getPath();
                String uri2 = data.getPath();
                String uri3 = data.getPath();
                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopDocumentsList documentsList = new ShopDocumentsList(uri1,dateof_reg,uri2,uri3,gstin,no_ofyears);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPS").child("SHOP DOCUMENTS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.setValue(documentsList);
                       // progressDialog.dismiss();
                        Intent intent = new Intent(ShopDocuments.this,ShopDetails.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded" +(int) progress + "%");
            }
        });
    }

    private void uploadPDF2(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setTitle("File loading");
        //progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" +System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String dateof_reg = date_of_reg.getText().toString();
                String gstin = gst_in.getText().toString();
                String no_ofyears = shoprunning_years.getText().toString();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                String uri2 = data.getPath();
                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopDocumentsList documentsList = new ShopDocumentsList("",dateof_reg,uri2,"",gstin,no_ofyears);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPS").child("SHOP DOCUMENTS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.setValue(documentsList);
                        //progressDialog.dismiss();
                        Intent intent = new Intent(ShopDocuments.this,ShopDetails.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded" +(int) progress + "%");
            }
        });
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setTitle("File loading");
        //progressDialog.show();

        StorageReference reference = storageReference.child("Uploads" +System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String dateof_reg = date_of_reg.getText().toString();
                String gstin = gst_in.getText().toString();
                String no_ofyears = shoprunning_years.getText().toString();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                String uri1 = data.getPath();
                nextbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopDocumentsList documentsList = new ShopDocumentsList(uri1,dateof_reg,"","",gstin,no_ofyears);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("SHOPS").child("SHOP DOCUMENTS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseReference.setValue(documentsList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ShopDocuments.this, "999999", Toast.LENGTH_SHORT).show();
                                //progressDialog.dismiss();
                            }
                        });

                        Intent intent = new Intent(ShopDocuments.this,ShopDetails.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded" +(int) progress + "%");
            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date_of_reg.setText(currentDate);
    }
}
