package com.rejointech.keybook.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.rejointech.keybook.R;

public class Entry extends AppCompatActivity {
    public static int SIGN = 1;
    ImageView arrow, arrow1;
    TextView swipe;
    Button loginBot;
    TextInputLayout user, password;
    FloatingActionButton fabMain, Fab1, Fab2, Fab3;
    Boolean BUTTON_OPEN = false;
    Animation botOpen, botClose, blink1;

    //extra for testing purpose
    FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_entry);
        arrow = findViewById(R.id.arrow);
        arrow1 = findViewById(R.id.arrow1);
        swipe = findViewById(R.id.swipe);
        blink1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink2);
        arrow1.startAnimation(blink1);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        arrow.startAnimation(animation);
        botOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_open);
        botClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_close);
        user = findViewById(R.id.user);
        auth = FirebaseAuth.getInstance();
        password = findViewById(R.id.password);
        fabMain = findViewById(R.id.fabMain);
        loginBot = findViewById(R.id.loginbot);
        Fab1 = findViewById(R.id.Fab1);
        Fab2 = findViewById(R.id.Fab2);
        Fab3 = findViewById(R.id.Fab3);
        Fab1.setVisibility(View.INVISIBLE);
        Fab2.setVisibility(View.INVISIBLE);
        Fab3.setVisibility(View.INVISIBLE);
        loginBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getEditText().getText().toString().isEmpty()) {
                    user.setError("Email id required!!");
                    user.requestFocus();
                } else if (password.getEditText().getText().toString().isEmpty()) {
                    password.setError("Required");
                    password.requestFocus();

                } else {

                    login(user.getEditText().getText().toString(), password.getEditText().getText().toString());
                }

            }
        });


        googleReq();
        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signUp.class));
            }
        });


        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BUTTON_OPEN) {
                    fabMain.setRotation(0);
                    Fab1.startAnimation(botClose);
                    Fab2.startAnimation(botClose);
                    Fab3.startAnimation(botClose);
                    BUTTON_OPEN = false;
                } else {
                    fabMain.setRotation(45);
                    Fab1.startAnimation(botOpen);
                    Fab2.startAnimation(botOpen);
                    Fab3.startAnimation(botOpen);
                    BUTTON_OPEN = true;
                }
            }
        });
        Fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignUp(SIGN);
            }
        });
        Fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSignUp();
            }
        });
        Fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                githubSignUp();
            }
        });


    }

    private void login(@NonNull String userId1, @NonNull String password1) {
        auth.signInWithEmailAndPassword(userId1, password1).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));

                }
            }
        });
    }

    private void githubSignUp() {
    }

    private void facebookSignUp() {
    }

    //GOOGLE

    private void googleReq() {
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
                firebaseGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = auth.getCurrentUser();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });

    }
}