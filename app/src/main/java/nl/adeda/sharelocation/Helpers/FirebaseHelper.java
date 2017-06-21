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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 13-6-2017.
 */

public class FirebaseHelper {

    private static DatabaseReference groupDataRef;
    private static DatabaseReference userDataRef;
    private static DatabaseReference userRef;
    private static ArrayList<String> groupNames;
    private static ArrayList<String> groupMemberUIDs;


    private static User userData;
    private static ArrayList<String> userIds;

    public static CallbackInterface delegate;
    public static CallbackGroupUpdate groupDelegate;

    private static final LinkedHashMap<String, List<String>> groupsNamesHashMap;
    private static final LinkedHashMap<String, List<String>> groupsUIDHashMap;
    private static int i;



    static {
        // Initialize variables
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        groupDataRef = database.getReference("groupData");
        userDataRef = database.getReference("userData");
        groupsNamesHashMap = new LinkedHashMap<>();
        groupsUIDHashMap = new LinkedHashMap<>();
        userIds = new ArrayList<>();
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
                        delegate.onLoginUserDataCallback(userData);
                        Log.e("FBH", "userData is set");
                        break;
                    case 2: // Get group information for current user
                        for (DataSnapshot childNode : dataSnapshot.child("groups").getChildren()) {
                            groupKeys.add(childNode.getValue().toString()); // Add key of group the user is in to ArrayList
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
        groupNames = new ArrayList<>();

        i = 0;

        groupDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childNode : dataSnapshot.getChildren()) {
                    for (String groupKey : groupKeys) {
                        if (groupKey.equals(childNode.getKey())) {
                            String currentGroupName = (String) childNode.child("groupName").getValue();
                            groupNames.add(currentGroupName); // Add group name to list
                            groupMemberUIDs = getGroupMemberUIDs(childNode); // Get all UIDs of current group
                            getGroupMemberNames(groupMemberUIDs, i); // Gets first and last names of users in UID list
                            i++;
                        }
                    }
                }

                delegate.onGroupDataCallback(groupNames, groupsNamesHashMap, groupsUIDHashMap); // Callback method in GroupFragment
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void getGroupMemberNames(final ArrayList<String> groupMemberUIDs, final int index) {
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
                groupsNamesHashMap.put(groupNames.get(index), groupMemberNames); // Put group members in current group (at index)
                groupsUIDHashMap.put(groupNames.get(index), groupMemberUIDs); // Put group member UIDs in current group (at index)
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

    public static void pullGroupMemberLocations(final List<String> memberUIDs) {
        final ArrayList<User> membersInGroup = new ArrayList<>();

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (String memberUID : memberUIDs) {
                        if (memberUID.equals(childSnapshot.getKey())) {
                            userData = new User();
                            userData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                            userData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                            userData.setVoornaam((String) childSnapshot.child("userInfo").child("firstName").getValue());
                            userData.setAchternaam((String) childSnapshot.child("userInfo").child("lastName").getValue());
                            // userData.setPhoto((Bitmap) childSnapshot.child("userInfo").child("photoURI").getValue());
                            membersInGroup.add(userData);
                        }
                    }
                }
                delegate.onLoadGroupMap(membersInGroup, memberUIDs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public static void updateLocations(final List<String> userIds) {
        final ArrayList<User> membersInGroupUpdate = new ArrayList<>();

        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (String userId : userIds) {
                        if (userId.equals(childSnapshot.getKey())) {
                            userData = new User();
                            userData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                            userData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                            userData.setVoornaam((String) childSnapshot.child("userInfo").child("firstName").getValue());
                            userData.setAchternaam((String) childSnapshot.child("userInfo").child("lastName").getValue());
                            // userData.setPhoto((Bitmap) childSnapshot.child("userInfo").child("photoURI").getValue());
                            membersInGroupUpdate.add(userData);
                        }
                    }
                }
                groupDelegate.returnGroupUpdate(membersInGroupUpdate);
                membersInGroupUpdate.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
