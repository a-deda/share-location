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

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterface;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * LoginActivity, verifies the users' email address and password. If correct, the user is logged
 * into his account.
 */

public class LoginActivity extends AppCompatActivity implements CallbackInterface {

    // Initialize variables
    private FirebaseAuth firebaseAuth;

    private EditText email;
    private EditText password;

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
        getViews();

        // Set onClickListeners
        setOnClickListeners();
    }

    private void getViews() {
        email = (EditText) findViewById(R.id.emailFieldLogin);
        password = (EditText) findViewById(R.id.passwordFieldLogin);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpLink = (Button) findViewById(R.id.signUpLink);
    }

    private void setOnClickListeners() {
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

    // Checks if the user is already logged in onStart. If so, a splash screen is showed until
    // all data for the user is loaded.
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            setContentView(R.layout.splash_screen); // Display splash screen

            // Fetch user data from Firebase
            FirebaseHelper.delegate = this;
            FirebaseHelper.pullFromFirebase(1);
        }

    }

    // Checks correctness of filled in login form
    private void formCheck() {
        // Get text from views
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        // Set color to default
        email.setBackgroundColor(Color.parseColor("#212121"));
        password.setBackgroundColor(Color.parseColor("#212121"));

        int errors = checkEmptyFields(emailText, passwordText);

        // If there are no errors, the user can be logged in
        if (errors == 0){
            progressDialog = ProgressDialog.show(this, "", "Inloggen...", true);
            login(emailText, passwordText);
        }
    }

    private int checkEmptyFields(String emailText, String passwordText) {
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

        return errors;

    }

    // Logs the user in using the given credentials
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

    // Callback function from FirebaseHelper.pullFromFirebase(1). Called when user data is fetched
    // successfully.
    @Override
    public void onLoginUserDataCallback(User userData) {
        // Launch MainActivity with the users' data
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGroupDataCallback(ArrayList<String> groupNames, LinkedHashMap<String, List<String>> groupMemberNames, LinkedHashMap<String, List<String>> groupMemberUIDs, ArrayList<DateTime> endTimes, ArrayList<String> groupKeys) {
        // Has no function here.
    }

    @Override
    public void onLoadGroupMap(ArrayList<User> users, User currentUserData, String groupName) {
        // Has no function here.
    }

}
