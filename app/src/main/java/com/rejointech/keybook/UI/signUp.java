package com.rejointech.keybook.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.rejointech.keybook.Data.userCredentials;
import com.rejointech.keybook.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class signUp extends AppCompatActivity {
    private static final int SIGN = 1;
    TextInputLayout name, email, password, phone;
    CircleImageView profileImage;
    Button signUp, alredyUser;
    FloatingActionButton faba, fabb, fabc;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    GoogleSignInClient mGoogleSignInClient;
    int code;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        profileImage = findViewById(R.id.profileImage);
        faba = findViewById(R.id.Faba);
        fabb = findViewById(R.id.Fabb);
        fabc = findViewById(R.id.Fabc);
        name = findViewById(R.id.name1);
        email = findViewById(R.id.email1);
        password = findViewById(R.id.pass);
        phone = findViewById(R.id.phone);
        signUp = findViewById(R.id.signUp);
        alredyUser = findViewById(R.id.alredyUser);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        createRequest();


        faba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignUp(SIGN);

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getEditText().getText().toString().isEmpty()) {
                    name.setError("Required");
                    name.requestFocus();
                } else if (email.getEditText().getText().toString().isEmpty()) {
                    email.setError("Email Required");
                    email.requestFocus();
                } else if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError("No password found");
                    password.requestFocus();
                } else {
                    SignUp(email.getEditText().getText().toString(), password.getEditText().getText().toString());
                }
            }
        });

        alredyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Entry.class));

            }
        });
        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSignUp();

            }
        });
        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                githubSignUp();

            }
        });
    }


    private void SignUp(@NonNull String username, @NonNull String password) {
        auth.createUserWithEmailAndPassword(username, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userCredentials userCredentials = new userCredentials(email.getEditText().getText().toString(),
                            phone.getEditText().getText().toString(),
                            name.getEditText().getText().toString(),
                            auth.getUid()
                    );
                    databaseReference.child("users").child(auth.getUid().toString()).setValue(userCredentials).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void githubSignUp() {
    }

    private void facebookSignUp() {
    }


    private void createRequest() {
        //request for pop up

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignUp(int code) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));

                } else {
                    Toast.makeText(signUp.this, "Authentication Failed!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}



