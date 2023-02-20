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
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    private static final String TAG = "GOOGLEAUTH";
    EditText username, email, phone, password, cnfrmpass;
    Button registerbtn;
    TextView loginbtn;
    SignInButton googlesigninbtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final Pattern PASSWORD_PASSWORD = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + ".{6,}" + "$");
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Integer i=0;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phoneno);
        password = findViewById(R.id.password);
        cnfrmpass = findViewById(R.id.confirmpass);
        registerbtn = findViewById(R.id.regis);
        loginbtn = findViewById(R.id.login);
        googlesigninbtn = findViewById(R.id.googlesignin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        googlesigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  googleSignIn();
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(MainActivity.this,LoginPage.class);
                startActivity(c);
            }
        });

        // Custom Registration
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuthentication();
            }
        });
    }

    private void performAuthentication() {
        String user = username.getText().toString();
        String mail = email.getText().toString();
        String phoneno = phone.getText().toString();
        String pass = password.getText().toString();
        String confirmpass = cnfrmpass.getText().toString();
        TextView mailiderr = findViewById(R.id.mailerror);
        TextView passwrderr = findViewById(R.id.passerror);
        TextView cnfrmpasserr = findViewById(R.id.repasserror);
        TextView phoneerr = findViewById(R.id.phnoerror);

        if (user.isEmpty() || phoneno.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter user details", Toast.LENGTH_SHORT).show();
        } else if (!mail.matches(emailPattern)) {
            mailiderr.setVisibility(View.VISIBLE);
        } else if (!PASSWORD_PASSWORD.matcher(pass).matches()) {
            passwrderr.setVisibility(View.VISIBLE);
        } else if (!pass.matches(confirmpass)) {
            cnfrmpasserr.setVisibility(View.VISIBLE);
        } else if (!(phoneno.length() == 10)) {
            phoneerr.setVisibility(View.VISIBLE);
        } else {
            progressDialog.setMessage("Registering");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            ViewDialogverify viewDialogverify = new ViewDialogverify();
            viewDialogverify.showDialog(MainActivity.this);

            // Registering user in Firebase Database
            mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                        String userId = "user" + new Date().getTime();
                        String user = username.getText().toString();
                        String phonenum = phone.getText().toString();
                        String mail = email.getText().toString();
                        String pass = password.getText().toString();
                        UserList userList = new UserList(userId, user, phonenum, mail, pass);
                        databaseReference.child("USERS").child("USER REGISTER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userList);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void googleSignIn() {
        Intent signIn = gsc.getSignInIntent();
        startActivityForResult(signIn, GOOGLE_SIGN_IN_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d(TAG, "Something went wrong", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = "user" + new Date().getTime();
                            String user = mUser.getDisplayName();
                            String phonenum = mUser.getPhoneNumber();
                            String mail = mUser.getEmail();
                            Log.d(TAG, "signInWithCredential:success");
                            mUser = mAuth.getCurrentUser();

                            // Checking if user is already logged in
                            databaseReference.orderByChild(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        UserList userList = dataSnapshot.getValue(UserList.class);
                                        if(mail.equals(userList.getMail())) {
                                            i = i + 1;
                                        }
                                    }
                                    if (i == 1) {
                                        Toast.makeText(MainActivity.this, "User already logged in", Toast.LENGTH_SHORT).show();
                                        Intent a = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(a);
                                    }else {
                                        databaseReference.child(userId).setValue(new UserList(userId, user, "", mail, ""));
                                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });}
                    }});
    }

    private class ViewDialogverify {
        public void showDialog(MainActivity mainActivity) {
            final Dialog dialog = new Dialog(MainActivity.this);
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
                    String mail = email.getText().toString().trim();
                    String pass = password.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if ((FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())) {
                                Intent a = new Intent(MainActivity.this, Address.class);
                                startActivity(a);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(mainActivity, "Verify E-Mail to Continue", Toast.LENGTH_SHORT).show();
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



