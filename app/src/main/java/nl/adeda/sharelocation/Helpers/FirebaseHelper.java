package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import nl.adeda.sharelocation.Activities.GroepToevoegenActivity;
import nl.adeda.sharelocation.Activities.MainActivity;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 13-6-2017.
 */

public class FirebaseHelper {

    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference("userData");
    }

    public void pushToFirebaseOnRegistration(@NonNull FirebaseUser loggedInUser, String[] data) {
        userRef = rootRef.child(loggedInUser.getUid());

        userRef.child("email").setValue(data[0]);
        userRef.child("userInfo").child("firstName").setValue(data[1]);
        userRef.child("userInfo").child("lastName").setValue(data[2]);
    }

    public void pushToFirebaseOnLocationUpdate(@NonNull FirebaseUser loggedInUser, Double[] data) {
        userRef = rootRef.child(loggedInUser.getUid());

        userRef.child("location").child("latitude").setValue(data[0]);
        userRef.child("location").child("longitude").setValue(data[1]);
    }

    public void pullFromFirebase(@NonNull final FirebaseUser user, final int dataType, final Activity callingActivity, final Class destination) {
        userRef = rootRef.child(user.getUid());

        final User userData = new User();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("onDataChange", "onDataChange was called.");
                switch (dataType) {
                    case 0: // Get location data
                        userData.setLatitude((double) dataSnapshot.child("location").child("latitude").getValue());
                        userData.setLongitude((double) dataSnapshot.child("location").child("longitude").getValue());
                        break;
                    case 1: // Get user information
                        userData.setVoornaam((String) dataSnapshot.child("userInfo").child("firstName").getValue());
                        userData.setAchternaam((String) dataSnapshot.child("userInfo").child("lastName").getValue());
                        break;
                    case 2: // TODO: Get all data
                        break;
                }
                returnData(userData, callingActivity, destination);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("VEL", "ValueEventListener was cancelled.");
            }
        });
    }

    private void returnData(User userData, Activity callingActivity, Class destination) {
        Intent intent = new Intent(callingActivity, destination);
        intent.putExtra("userData", userData);
        callingActivity.startActivity(intent);
        callingActivity.finish();
    }

    public void checkIfUserExists(final String email) {
        DatabaseReference userDataRef = rootRef.child("userData");

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String emailAddress = (String) snapshot.child("email").getValue();
                    Log.e("EMAIL", emailAddress);
                    if (email.equals(emailAddress)) {
                        Log.e("Ex", "It exists.");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
