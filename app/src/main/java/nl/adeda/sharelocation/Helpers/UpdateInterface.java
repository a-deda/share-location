package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

import nl.adeda.sharelocation.Activities.LoginActivity;
import nl.adeda.sharelocation.Activities.MainActivity;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 13-6-2017.
 */

public class UpdateInterface {

    public void update(FirebaseUser userData, Activity callingActivity) {
        // Fetch user data from Firebase
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        User fireBaseData = firebaseHelper.pullFromFirebase(userData, 1);

        // TODO: Put data from Firebase into intent
        Intent intent = new Intent(callingActivity, MainActivity.class);
        callingActivity.startActivity(intent);
        callingActivity.finish();
    }
}
