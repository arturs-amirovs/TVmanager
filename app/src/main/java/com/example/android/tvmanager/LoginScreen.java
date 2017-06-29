package com.example.android.tvmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText usernameText;
    private EditText passwordText;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usernameText = (EditText) findViewById(R.id.login);
        passwordText = (EditText) findViewById(R.id.password);
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("tag ", "onAuthStateChanged:signed_in:" + user.getUid());
                    User.getCurrentUser().setUid(user.getUid());
                    Log.e("email: ===", User.getCurrentUser().getUid());
                } else {
                    // User is signed out
                    Log.d("tag ", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("tag ", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Tag ", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginScreen.this, "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("Tag ", "signInWithEmail:success", task.getException());
                            Toast.makeText(LoginScreen.this, "Auth success",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginScreen.this, HomeActivity.class));
                            finish();
//                            newActivity();

                        }

                        // ...
                    }
                });
    }
    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference userRef = myRef.child(user.getUid());
                            userRef.setValue(user);
                            Toast.makeText(LoginScreen.this, "Registration completed. You may log in.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
    });
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();

        if(i == R.id.loginButton){
            signIn(usernameText.getText().toString(), passwordText.getText().toString());
        } else if(i == R.id.registerButton) {
            register(usernameText.getText().toString(), passwordText.getText().toString());
        }

    }

    public void newActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

}