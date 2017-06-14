package nl.adeda.sharelocation.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 8-6-2017.
 */

public class GroepToevoegenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toevoegen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toevoegen_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        // OnClickListener for back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
        final EditText emailField = (EditText) findViewById(R.id.contact_to_add);
        final Button toevBtn = (Button) findViewById(R.id.contact_add_btn);

        // OnClickListener for add button
        toevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check if user is valid (!= "")
                String email = emailField.getText().toString();

                // Check if user that has been typed in exists
                FirebaseHelper firebaseHelper = new FirebaseHelper();
                firebaseHelper.checkIfUserExists(email);

                // TODO: Send request to user
                /*
                // Go back
                Toast.makeText(GroepToevoegenActivity.this, "Volgverzoek verzonden!", Toast.LENGTH_SHORT).show();
                finish();
                onBackPressed();
                */
            }
        });
    }

}
