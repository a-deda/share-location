package nl.adeda.sharelocation.Helpers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        rootRef = database.getReference("data");
    }
    public void pushToFirebase(@NonNull FirebaseUser loggedInUser, User userInfo) {
        userRef = rootRef.child(loggedInUser.getUid());

        userRef.setValue(userInfo);

    }

    public void pullFromFirebase(@NonNull FirebaseUser user) {
        userRef = rootRef.child(user.getUid());
    }
}
