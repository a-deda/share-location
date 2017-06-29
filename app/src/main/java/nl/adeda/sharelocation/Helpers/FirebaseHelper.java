package nl.adeda.sharelocation.Helpers;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.springframework.scheduling.annotation.AsyncResult;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackGroupUpdate;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterface;
import nl.adeda.sharelocation.Helpers.Interfaces.GroupAddCallback;
import nl.adeda.sharelocation.Helpers.Interfaces.PhotoInterface;
import nl.adeda.sharelocation.User;

/**
 * A helper class that is used for all Firebase interactions. Contains multiple callbacks to
 * return data to the calling classes.
 */

public class FirebaseHelper {

    // Initialize variables
    private static DatabaseReference groupDataRef;
    private static DatabaseReference userDataRef;
    public static DatabaseReference userRef;
    private static ArrayList<String> groupNames;
    private static ArrayList<String> groupMemberUIDs;
    private static ArrayList<DateTime> endTimes;

    private static StorageReference storageRef;
    private static StorageReference userStorageRef;
    private static StorageReference smallUserStorageRef;

    private static User userData;
    private static User currentUserData;
    private static User completeUserData;
    public static ArrayList<String> userIds;

    public static CallbackInterface delegate;
    public static CallbackGroupUpdate groupDelegate;
    public static PhotoInterface photoDelegate;

    private static final LinkedHashMap<String, List<String>> groupsNamesHashMap;
    private static final LinkedHashMap<String, List<String>> groupsUIDHashMap;
    private static int i;

    private static final ArrayList<User> initializedUsers;
    public static GroupAddCallback groupAddDelegate;


    static {
        // Give variables a value
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        groupDataRef = database.getReference("groupData");
        userDataRef = database.getReference("userData");
        storageRef = storage.getReference("userData");
        groupsNamesHashMap = new LinkedHashMap<>();
        groupsUIDHashMap = new LinkedHashMap<>();
        userIds = new ArrayList<>();
        initializedUsers = new ArrayList<>();
    }

    // Pushes user data to Firebase when the user registers
    public static void pushToFirebaseOnRegistration(@NonNull FirebaseUser loggedInUser, String[] data) {
        userRef = userDataRef.child(loggedInUser.getUid());

        userRef.child("email").setValue(data[0]);
        userRef.child("userInfo").child("firstName").setValue(data[1]);
        userRef.child("userInfo").child("lastName").setValue(data[2]);
    }

    // Pushes location data (lat, long) to Firebase when users location is changed. Called by
    // GPSHelper.locationPusher
    public static void pushToFirebaseOnLocationUpdate(@NonNull String loggedInUserId, Double[] data) {
        userRef = userDataRef.child(loggedInUserId);

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
                            userDataRef.child(childNode.getKey()).child("groups").child(pushedGroupKey).setValue(pushedGroupKey);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Pushes profile photo of user (full version (large) and map version (small)) to Firebase
    // Storage
    public static void pushProfilePhoto(@NonNull final String loggedInUserId, File profilePhoto) {
        userStorageRef = storageRef.child(loggedInUserId);

        Uri fileURI = Uri.fromFile(profilePhoto);
        userStorageRef.putFile(fileURI);

        smallUserStorageRef = storageRef.child(loggedInUserId).child("map-size");
        File fixedPhoto = PhotoFixer.fixPhotoMapMarker(profilePhoto,
                loggedInUserId);
        Uri fixedPhotoURI = Uri.fromFile(fixedPhoto);
        smallUserStorageRef.putFile(fixedPhotoURI);
    }

    // Pulls personal data for user from Firebase
    public static void pullFromFirebase(final int dataType) {
        userRef = userDataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userData = new User(); // New user object that can be filled with user data

        final ArrayList<String> groupKeys = new ArrayList<>(); // Array for keys of all the users' groups

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                switch (dataType) {
                    case 0: // Get location data
                        userData.setLatitude((double) dataSnapshot.child("location").child("latitude").getValue());
                        userData.setLongitude((double) dataSnapshot.child("location").child("longitude").getValue());
                        break;
                    case 1: // Get user information
                        userData.setFirstName((String) dataSnapshot.child("userInfo").child("firstName").getValue());
                        userData.setLastName((String) dataSnapshot.child("userInfo").child("lastName").getValue());
                        delegate.onLoginUserDataCallback(userData); // Log user in
                        break;
                    case 2: // Get group information for current user
                        for (DataSnapshot childNode : dataSnapshot.child("groups").getChildren()) {
                            // Add key of group the user is in to ArrayList
                            groupKeys.add(childNode.getValue().toString());
                        }

                        // Get group information data for all groups the user is in
                        getGroupInformation(groupKeys);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    // Called by pullFromFirebase(), gets group names and group member uIDs
    // for all the groups the user is in.
    private static void getGroupInformation(final ArrayList<String> groupKeys) {
        groupNames = new ArrayList<>();
        endTimes = new ArrayList<>();

        i = 0;

        groupDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childNode : dataSnapshot.getChildren()) {
                    for (String groupKey : groupKeys) {
                        if (groupKey.equals(childNode.getKey())) {
                            String currentGroupName = (String) childNode.child("groupName").getValue();
                            DateTime currentDateTime = childNode.child("endTime").getValue(DateTime.class);

                            if (currentDateTime != null) {
                                groupHasExpired(currentDateTime, groupKey);
                            }

                            groupNames.add(currentGroupName); // Add group name to list
                            endTimes.add(currentDateTime); // Add DateTime objects to list

                            groupMemberUIDs = getGroupMemberUIDs(childNode); // Get all UIDs of current group
                            getGroupMemberNames(groupMemberUIDs, i); // Gets first and last names of users in UID list
                            i++;
                        }
                    }
                }

                delegate.onGroupDataCallback(groupNames, groupsNamesHashMap, groupsUIDHashMap,
                        endTimes, groupKeys); // Callback method in GroupFragment
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Gets first and last names of users in group index, using their user ID. Puts together a
    // hashmap that can then be used in the GroupsFragment to display all groups and its users in
    // an ExpandableListView.
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

                            // Add first and last name to list
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

    // Called when the user clicks the go-to-map button in the ExpandableListView
    // (GroupsFragment). When finished, the group locations are pushed to the GroupsFragment,
    // that launches the MapFragment.
    public static void pullGroupMemberLocations(final List<String> memberUIDs, final String
            groupName) {

        final ArrayList<User> membersInGroup = new ArrayList<>();
        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (String memberUID : memberUIDs) {
                        if (memberUID.equals(childSnapshot.getKey())) {
                            // Separate the current user from the other users
                            if (memberUID.equals(currentUID)) {
                                currentUserData = new User();
                                currentUserData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                                currentUserData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                                currentUserData.setFirstName((String) childSnapshot.child("userInfo").child("firstName").getValue());
                                currentUserData.setUserId(currentUID);
                            } else { // Fill list of other users
                                userData = new User();
                                userData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                                userData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                                userData.setFirstName((String) childSnapshot.child("userInfo").child("firstName").getValue());
                                userData.setLastName((String) childSnapshot.child("userInfo").child("lastName").getValue());
                                userData.setUserId(memberUID);
                                membersInGroup.add(userData);
                            }
                        }
                    }
                }
                // Callback function that passes the loaded information in order to initialize
                // the map
                delegate.onLoadGroupMap(membersInGroup, currentUserData, groupName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Loops over the existing users in Firebase, checking if the email address that was given
    // matches an entry in the database.
    public static void addUserIfExists(final String email) {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String emailAddress = (String) child.child("email").getValue();

                    // Check current email address matches with given email address
                    if (email.equals(emailAddress)) {
                        // Check if user is already in the to add list
                        for (String userId : userIds) {
                            if (userId.equals(child.getKey())) {
                                // User is already added to list
                                groupAddDelegate.addUserToList(null, true);
                                return;
                            }
                        }

                        // User is not added to list, so add it
                        User user = getUserData(child);
                        user.setEmail(emailAddress);
                        groupAddDelegate.addUserToList(user, false);
                        userIds.add(child.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing.
            }
        });
    }

    // Sets the users' first and last name in the user object
    private static User getUserData(DataSnapshot child) {
        User user = new User();

        user.setFirstName(child.child("userInfo").child("firstName").getValue().toString());
        user.setLastName(child.child("userInfo").child("lastName").getValue().toString());

        return user;
    }

    // Returns a list of uID's, which is filled when group is made.
    // CAN ONLY BE CALLED AFTER AddUserIfExists() HAS FINISHED!
    public static ArrayList<String> returnAddedKeys() {
        ArrayList<String> userIdsToReturn = new ArrayList<>(userIds);
        userIds.clear();
        return userIdsToReturn;
    }

    // Updates the user coordinates for all users that are displayed on the active map in the
    // MapFragment with a given interval. Calls a function in MapFragment, that subsequently
    // updates the markers on the displayed GoogleMap.
    public static void updateLocations(final List<String> userIds, final ArrayList<Marker> otherUserMarkers, final Marker currentMarker) {

        final ArrayList<User> membersInGroupUpdate = new ArrayList<>();
        final String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("ODC", "Locationupdate ondatachange called.");
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    for (String userId : userIds) {
                        if (userId.equals(childSnapshot.getKey())) {
                            if (userId.equals(currentUID)) { // Separate the current user from the other users
                                currentUserData = new User();
                                currentUserData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                                currentUserData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                                currentUserData.setFirstName((String) childSnapshot.child("userInfo").child("firstName").getValue());
                                currentUserData.setLastName((String) childSnapshot.child("userInfo").child("lastName").getValue());
                                currentUserData.setUserId(currentUID);
                            } else { // Load other users
                                userData = new User();
                                userData.setLatitude((Double) childSnapshot.child("location").child("latitude").getValue());
                                userData.setLongitude((Double) childSnapshot.child("location").child("longitude").getValue());
                                userData.setFirstName((String) childSnapshot.child("userInfo").child("firstName").getValue());
                                userData.setLastName((String) childSnapshot.child("userInfo").child("lastName").getValue());
                                userData.setUserId(userId);
                                membersInGroupUpdate.add(userData); // Add user to loaded user list
                            }
                        }
                    }
                }
                // Call to update the markers on the displayed GoogleMap in MapFragment
                groupDelegate.returnGroupUpdate(membersInGroupUpdate, currentUserData,
                        otherUserMarkers, currentMarker);
                membersInGroupUpdate.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Gets profile photo for logged in user in order for it to be displayed in the
    // NavigationDrawer view.
    public static void pullProfilePhoto(String loggedInUserId) {
        userStorageRef = storageRef.child(loggedInUserId);

        File profilePhoto = null;
        String filePath = null;

        try {
            profilePhoto = File.createTempFile("profile", ".png");
            filePath = profilePhoto.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (profilePhoto == null) {
            return;
        }

        final File finalProfilePhoto = profilePhoto;

        userStorageRef.getFile(profilePhoto).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Callback to MainActivity
                photoDelegate.returnPhoto(finalProfilePhoto);
            }
        });
    }

    // Check if one of the users' group has passed it expiration date and time
    private static void groupHasExpired(DateTime currentDateTime, final String groupKey) {
        String dateString = String.format("%s-%d-%d %d:%d", currentDateTime.getDay(), currentDateTime.getMonth(),
                currentDateTime.getYear(), currentDateTime.getHour(), currentDateTime.getMinute());

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();

        if (date.after(currentDate)) {
            // Do nothing; date hasn't passed yet.
        } else { // Date and time have passed
            groupDataRef.child(groupKey).child("members").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot member : dataSnapshot.getChildren()) {
                        // Remove group from user node in Firebase
                        userDataRef.child(member.getValue().toString()).child("groups").child(groupKey).removeValue();
                    }
                    // Remove group node from Firebase
                    groupDataRef.child(groupKey).removeValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // Gets other users' photos and returns them into a list in getOtherUserMapMarkers
    private static Future<User> getUserImage(User user) {
        initializeMapMarkers(user, true);
        return new AsyncResult<>(user);
    }

    // Starts Future that waits for user photos to be loaded. These are then put into a list of
    // Futures, that is then unpacked and returned as a normal list of users to MapFragment.
    public static ArrayList<User> getOtherUserMapMarkers(final ArrayList<User> initUsers) {
        ArrayList<Future<?>> futureList = new ArrayList<>();
        ArrayList<User> initializedUsers = new ArrayList<>();

        for (User user : initUsers) {
            futureList.add(getUserImage(user)); // Add futures
        }

        for (Future<?> futureItem : futureList) {
            try {
                // Unpack futureList items
                User initializedUser = (User) futureItem.get();
                initializedUsers.add(initializedUser);
            } catch (InterruptedException | ExecutionException e) {
                Log.e("ERROR", e.toString());
            }
        }

        return initializedUsers;
    }

    // Tries to get user photos.
    public static void initializeMapMarkers(final User userData, final boolean isOtherUser) {
        // Check if user has added a profile picture
        storageRef.child(userData.getUserId()).child("map-size").getDownloadUrl
                ().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                getUserPhoto(userData, isOtherUser); // Get photo of specified user
            }
        }).addOnFailureListener(new OnFailureListener() { // Return user object without photo
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!isOtherUser) {
                    // Returns the user object without photo to initialize the current user
                    // marker in the MapFragment
                    photoDelegate.initializeCurrentUserMarker(userData);
                }
            }
        });
    }

    // Called when initializing map markers (above), sets photo to user objects. If the method is
    // called to load the photo of the logged in user. A callback is made to the MapFragment.
    private static void getUserPhoto(final User userData, final boolean isOtherUser) {
        userStorageRef = storageRef.child(userData.getUserId()).child("map-size");
        File profilePhoto = null;

        try {
            profilePhoto = File.createTempFile("profile", ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (profilePhoto == null) {
            return;
        }

        final File finalProfilePhoto = profilePhoto;

        userStorageRef.getFile(profilePhoto).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                userData.setMapPhoto(BitmapFactory.decodeFile(finalProfilePhoto.getPath()));
                if (!isOtherUser) {
                    // Initializes the marker for the logged in user
                    photoDelegate.initializeCurrentUserMarker(userData);
                }
            }
        });
    }

    // Called when user deletes itself from a group. The user key in the group node in Firebase
    // will be deleted.
    public static void deleteUserFromGroup(final String groupId) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference group = groupDataRef.child(groupId);

        group.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("members").getChildren()) {
                    if (ds.getValue().equals(userId)) {
                        group.child("members").child(ds.getKey()).removeValue(); // Delete value
                        deleteGroupFromUser(groupId, userId); // Delete group reference from user
                    }
                }

                // If user was only group member, the group can be deleted.
                if (dataSnapshot.child("members").getChildrenCount() == 1) {
                    group.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Deletes the group reference from the user node in Firebase, and updates the
    // ExpandableListView in the GroupsFragment.
    private static void deleteGroupFromUser(final String groupId, String userId) {
        final DatabaseReference currentUserGroupsRef = userDataRef.child(userId).child("groups");
        currentUserGroupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().equals(groupId)) {
                        currentUserGroupsRef.child(ds.getKey()).removeValue();
                        pullFromFirebase(2); // Fetch group data from Firebase from new list
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
