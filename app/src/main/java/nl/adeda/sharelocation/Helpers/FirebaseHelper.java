package nl.adeda.sharelocation.Helpers;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

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

    public void pushToFirebase(@NonNull FirebaseUser loggedInUser, User user, int dataType) {
        userRef = rootRef.child(loggedInUser.getUid());

        switch (dataType) {
            case 0:
                userRef.child("location").setValue(user);
                break;
            case 1:
                userRef.child("userInfo").setValue(user);
                break;
        }
    }

    public User pullFromFirebase(@NonNull FirebaseUser user, final int dataType) {
        userRef = rootRef.child(user.getUid());

        final User[] userData = new User[1];

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                switch (dataType) {
                    case 0:
                        userData[0] = dataSnapshot.child("location").getValue(User.class);
                        break;
                    case 1:
                        userData[0] = dataSnapshot.child("userInfo").getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return userData[0];
    }


}
