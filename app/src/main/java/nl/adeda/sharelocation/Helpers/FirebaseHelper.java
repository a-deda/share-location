package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 13-6-2017.
 */

public class FirebaseHelper {

    private static DatabaseReference groupDataRef;
    private static DatabaseReference userDataRef;
    private DatabaseReference userRef;

    private static ArrayList<String> userIds = new ArrayList<>();

    public FirebaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        groupDataRef = database.getReference("groupData");
        userDataRef = database.getReference("userData");
    }

    public void pushToFirebaseOnRegistration(@NonNull FirebaseUser loggedInUser, String[] data) {
        userRef = userDataRef.child(loggedInUser.getUid());

        userRef.child("email").setValue(data[0]);
        userRef.child("userInfo").child("firstName").setValue(data[1]);
        userRef.child("userInfo").child("lastName").setValue(data[2]);
    }

    public void pushToFirebaseOnLocationUpdate(@NonNull FirebaseUser loggedInUser, Double[] data) {
        userRef = userDataRef.child(loggedInUser.getUid());

        userRef.child("location").child("latitude").setValue(data[0]);
        userRef.child("location").child("longitude").setValue(data[1]);
    }

    // Pushes group data to group node
    public static void pushToFirebaseOnAddingGroup(String groupName, final ArrayList<String> usersToAdd) {
        DatabaseReference pushedGroupRef = groupDataRef.push();
        final String pushedGroupKey = pushedGroupRef.getKey();

        pushedGroupRef.child("groupName").setValue(groupName);
        pushedGroupRef.child("members").setValue(usersToAdd);

        // Loop through Firebase users to find group members. If found,
        // put the unique group key in the user node.
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childNode : dataSnapshot.getChildren()) {
                    for (String user : usersToAdd) {
                        if (user.equals(childNode.getKey())) {
                            userDataRef.child(childNode.getKey()).child("groups").push().setValue(pushedGroupKey);
                        }
                }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void pullFromFirebase(@NonNull final FirebaseUser user, final int dataType, final Activity callingActivity, final Class destination) {
        userRef = userDataRef.child(user.getUid());

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
                // TODO: Make return function
                returnDataForActivityChange(userData, callingActivity, destination);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("VEL", "ValueEventListener was cancelled.");
            }
        });
    }

    private void returnDataForActivityChange(User userData, Activity callingActivity, Class destination) {
        Intent intent = new Intent(callingActivity, destination);
        intent.putExtra("userData", userData);
        callingActivity.startActivity(intent);
        callingActivity.finish();
    }

    public static void addUserIfExists(final String email, final ListView listView, final Context context) {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String emailAddress = (String) child.child("email").getValue();

                    Log.e("EMAIL", emailAddress);

                    if (email.equals(emailAddress)) {
                        User user = getUserData(child);
                        user.setEmail(emailAddress);
                        returnDataInList(listView, user, context);

                        userIds.add(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static User getUserData(DataSnapshot child) {
        User user = new User();
        user.setVoornaam(child.child("userInfo").child("firstName").getValue().toString());
        user.setAchternaam(child.child("userInfo").child("lastName").getValue().toString());

        return user;
    }

    private static void returnDataInList(ListView listView, User user, Context context) {

        if (listView.getAdapter() == null) {
            ArrayList<User> userList = new ArrayList<>();
            ContactListAdapter adapter = new ContactListAdapter(context, userList);
            listView.setAdapter(adapter);
            adapter.add(user);
        } else {
            ContactListAdapter adapter = (ContactListAdapter) listView.getAdapter();
            // TODO: Prevent user from adding same user multiple times.
            adapter.add(user);
        }
    }

    // Returns a list of uID's, which is filled when group is made.
    // CAN ONLY BE CALLED AFTER AddUserIfExists() HAS FINISHED
    public static ArrayList<String> returnAddedKeys() {
        ArrayList<String> userIdsToReturn = new ArrayList<>(userIds);
        userIds.clear();
        return userIdsToReturn;
    }

}
