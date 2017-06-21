package nl.adeda.sharelocation.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import nl.adeda.sharelocation.Helpers.CallbackInterface;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

public class LoginActivity extends AppCompatActivity implements CallbackInterface {

    private FirebaseAuth firebaseAuth;

    private EditText email;
    private EditText password;
    private EditText passwordConf;
    private Button signInBtn;
    private Button signUpLink;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide action bar
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        // Get views
        email = (EditText) findViewById(R.id.emailFieldLogin);
        password = (EditText) findViewById(R.id.passwordFieldLogin);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpLink = (Button) findViewById(R.id.signUpLink);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Check correctness of filled-in form
                formCheck();
            }
        });

        // Go to sign in activity
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            setContentView(R.layout.splash_screen);
            // Fetch user data from Firebase
            FirebaseHelper.delegate = this;
            FirebaseHelper.pullFromFirebase(user, 1);
        }

    }

    private void formCheck() {
        // Get text from views
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        email.setBackgroundColor(Color.parseColor("#212121"));
        password.setBackgroundColor(Color.parseColor("#212121"));

        int errors = 0;

        // Check if fields are not empty
        if (emailText.equals("")) {
            email.setBackgroundColor(Color.parseColor("#661414"));
            errors += 1;
        }

        if (passwordText.equals("")) {
            password.setBackgroundColor(Color.parseColor("#661414"));
            errors += 1;
        }


        if (errors == 0){
            progressDialog = ProgressDialog.show(this, "", "Inloggen...", true);
            login(emailText, passwordText);
        }
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Go to MainActivity
                            onStart();
                        } else {
                            Toast.makeText(LoginActivity.this, "Er is iets misgegaan. Probeer het nog eens.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onLoginUserDataCallback(User userData) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGroupDataCallback(ArrayList<String> groupNames, LinkedHashMap<String, List<String>> groupMemberNames, LinkedHashMap<String, List<String>> groupMemberUIDs) {
        // Has no function here.
    }

    @Override
    public void onLoadGroupMap(ArrayList<User> users, List<String> memberUIDs) {
        // Has no function here.
    }

}
