package nl.adeda.sharelocation.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nl.adeda.sharelocation.R;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

        // Hide action bar
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        // Get views
        email = (EditText) findViewById(R.id.emailFieldLogin);
        password = (EditText) findViewById(R.id.passwordFieldLogin);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpLink = (Button) findViewById(R.id.signUpLink);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Er is iets misgegaan. Probeer het nog eens.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
