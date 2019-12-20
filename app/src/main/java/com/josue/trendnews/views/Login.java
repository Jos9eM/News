package com.josue.trendnews.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josue.trendnews.MainActivity;
import com.josue.trendnews.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private DatabaseReference mDatabase;
    private String TAG = Login.class.getSimpleName();
    private static final String PATH_USERS = "Usuarios";
    private static final String PATH_MAIL = "mail";
    private static final String PATH_USER = "user";
    private EditText mail, password;
    private TextView register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        LoginButton loginButton = findViewById(R.id.face);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());   }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel"); }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(Login.this, getResources().getText(R.string.auth_fail),
                        Toast.LENGTH_SHORT).show();  }
        });


    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference reference = mDatabase.getReference(PATH_USERS);
        mCallbackManager = CallbackManager.Factory.create();

        mail = findViewById(R.id.mailTxt);
        password = findViewById(R.id.passTxt);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
         if (currentUser != null){
             updateUI(currentUser);
         } else{   }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        mDatabase = FirebaseDatabase.getInstance().getReference(PATH_USERS);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String usuario = user.getDisplayName();
                            String correo = user.getEmail();
                            mDatabase.child(user.getUid()).child(PATH_USER).setValue(usuario);
                            mDatabase.child(user.getUid()).child(PATH_MAIL).setValue(correo);

                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, getResources().getText(R.string.auth_fail),
                                    Toast.LENGTH_SHORT).show();    }
                    }
                });
    }

    private void updateUI (FirebaseUser user){
        if(user != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(Login.this, "Bienvenido...",
                    Toast.LENGTH_SHORT).show();        }
        else {    }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                loginAthempth();
                break;
            case R.id.register:
                startActivity(new Intent(getApplicationContext(), Register.class));
                break;
        }
    }

    private void loginAthempth() {

        String correo = mail.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(correo)){
            Toast.makeText(Login.this, "Introduce un email",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(Login.this, "Introduce un email Valido",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if (pass.length() <= 5){
            Toast.makeText(Login.this, "El password debe de tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Toast.makeText(Login.this, "Ayyyy Prroooo",
                    Toast.LENGTH_SHORT).show();

            mAuth.signInWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            // ...
                        }
                    });
        }
    }
}
