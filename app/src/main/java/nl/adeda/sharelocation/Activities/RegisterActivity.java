package nl.adeda.sharelocation.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import nl.adeda.sharelocation.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText email;
    private EditText password;
    private EditText passwordConf;
    private Button signUpBtn;
    private Button signInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide action bar
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();


        // Get views
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        passwordConf = (EditText) findViewById(R.id.passwordConfirmField);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signInLink = (Button) findViewById(R.id.signInLink);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check correctness of filled-in form
                formCheck();
            }
        });

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
            }
        });
    }

    private void formCheck() {
        // Get text from views
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String passwordConfText = passwordConf.getText().toString().trim();

        email.setBackgroundColor(Color.parseColor("#212121"));
        password.setBackgroundColor(Color.parseColor("#212121"));
        passwordConf.setBackgroundColor(Color.parseColor("#212121"));

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

        if (passwordConfText.equals("")) {
            passwordConf.setBackgroundColor(Color.parseColor("#661414"));
            errors += 1;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setBackgroundColor(Color.parseColor("#661414"));
            errors += 1;
        }

        // Check if password and confirmation are the same
        if (!passwordText.equals(passwordConfText)) {
            password.setBackgroundColor(Color.parseColor("#661414"));
            passwordConf.setBackgroundColor(Color.parseColor("#661414"));
            errors += 1;
        }

        if (errors == 0){
            register(emailText, passwordText);
        }
    }

    private void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Account aangemaakt!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Er is iets misgegaan. Controleer je gegevens en probeer het opnieuw.", Toast.LENGTH_SHORT).show();
                    Log.w("Registration", task.getException());
                }
            }
        });
    }
}
