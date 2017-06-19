package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 13-6-2017.
 */

public class FirebaseHelper {

    private static DatabaseReference groupDataRef;
    private static DatabaseReference userDataRef;
    private static DatabaseReference userRef;

    private static User userData;
    private static ArrayList<String> userIds = new ArrayList<>();
    private static ArrayList<String> groupNames = new ArrayList<>();

    public static CallbackInterface delegate;


    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        groupDataRef = database.getReference("groupData");
        userDataRef = database.getReference("userData");
    }

    public static void pushToFirebaseOnRegistration(@NonNull FirebaseUser loggedInUser, String[] data) {
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
    public static void pushToFirebaseOnAddingGroup(String groupName, DateTime dateTime, final ArrayList<String> usersToAdd) {
        DatabaseReference pushedGroupRef = groupDataRef.push();
        final String pushedGroupKey = pushedGroupRef.getKey();

        pushedGroupRef.child("groupName").setValue(groupName);
        pushedGroupRef.child("members").setValue(usersToAdd);
        pushedGroupRef.child("endTime").setValue(dateTime);

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

    // Pulls personal data for user from Firebase
    public static void pullFromFirebase(@NonNull final FirebaseUser user, final int dataType) {
        userRef = userDataRef.child(user.getUid());

        userData = new User();

        final ArrayList<String> groupKeys = new ArrayList<>(); // Array for keys of all the users' groups

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("onDataChange", "onDataChange was called.");
                switch (dataType) {
                    case 0: // Get location data
                        userData.setLatitude((double) dataSnapshot.child("location").child("latitude").getValue());
                        userData.setLongitude((double) dataSnapshot.child("location").child("longitude").getValue());
                        //returnDataForActivityChange(userData, callingActivity, destination);
                        break;
                    case 1: // Get user information
                        userData.setVoornaam((String) dataSnapshot.child("userInfo").child("firstName").getValue());
                        userData.setAchternaam((String) dataSnapshot.child("userInfo").child("lastName").getValue());
                        delegate.onCompleteCallback(userData);
                        Log.e("FBH", "userData is set");
                        break;
                    case 2: // Get group information for current user
                        for (DataSnapshot childNode : dataSnapshot.child("groups").getChildren()) {
                            groupKeys.add(childNode.getValue().toString()); // Add key of group to ArrayList
                        }
                        getGroupInformation(groupKeys);
                    case 3: // TODO: Get all data
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("VEL", "ValueEventListener was cancelled.");
            }
        });
}
    // Called by pullFromFirebase(), gets group names and group member uIDs
    // for all the groups the user is in.
    private static void getGroupInformation(final ArrayList<String> groupKeys) {

        groupDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childNode : dataSnapshot.getChildren()) {
                    for (String groupKey : groupKeys) {
                        if (groupKey.equals(childNode.getKey())) {
                            groupNames.add((String) childNode.child("groupName").getValue()); // Add group name to list
                            ArrayList<String> groupMemberUIDs = getGroupMemberUIDs(childNode); // Get all UIDs of current group
                            getGroupMemberNames(groupMemberUIDs); // Gets first and last names of users in UID list
                            // TODO: Put ExtendedListAdapter on groupNames
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getGroupMemberNames(final ArrayList<String> groupMemberUIDs) {
        final ArrayList<String> groupMemberNames = new ArrayList<>();

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childNode : dataSnapshot.getChildren()) {
                    for (String groupMemberUID : groupMemberUIDs) {
                        if (groupMemberUID.equals(childNode.getKey())) {
                            String firstName = (String) childNode.child("userInfo").child("firstName").getValue();
                            String lastName = (String) childNode.child("userInfo").child("lastName").getValue();
                            groupMemberNames.add(firstName + " " + lastName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Returns an ArrayList<String> of group member UIDs in specified group node
    private static ArrayList<String> getGroupMemberUIDs(DataSnapshot childNode) {
        ArrayList<String> groupMemberUIDs = new ArrayList<>();
        for (DataSnapshot groupMember : childNode.child("members").getChildren()) {
            groupMemberUIDs.add((String) groupMember.getValue());
        }
        return groupMemberUIDs;
    }

    private static void returnDataForActivityChange(User userData, Activity callingActivity, Class destination) {
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
            // TODO (16/6): Prevent user from adding same user multiple times.
            adapter.add(user);
        }
    }

    // Returns a list of uID's, which is filled when group is made.
    // CAN ONLY BE CALLED AFTER AddUserIfExists() HAS FINISHED!
    public static ArrayList<String> returnAddedKeys() {
        ArrayList<String> userIdsToReturn = new ArrayList<>(userIds);
        userIds.clear();
        return userIdsToReturn;
    }

    // Returns a list of group names of the groups the user is in.
    // CAN ONLY BE CALLED AFTER getGroupInformation() HAS FINISHED!
    public static ArrayList<String> returnGroupNames() {
        ArrayList<String> groupNamesToReturn = new ArrayList<>(groupNames);
        groupNames.clear();
        return groupNamesToReturn;
    }

    // Returns userData that has been queried by pullFromFirebase()
    // CAN ONLY BE CALLED AFTER pullFromFirebase() has finished!
    public static User returnUserData() {
        Log.e("RET", "returnUserData() is called");
        return userData;
    }
}
