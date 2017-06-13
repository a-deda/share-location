package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import nl.adeda.sharelocation.Activities.LoginActivity;
import nl.adeda.sharelocation.Activities.MainActivity;

/**
 * Created by Antonio on 13-6-2017.
 */

public class UpdateInterface {

    public void update(FirebaseUser user, Activity callingActivity) {
        Intent intent = new Intent(callingActivity, MainActivity.class);
        callingActivity.startActivity(intent);
        callingActivity.finish();
    }
}
