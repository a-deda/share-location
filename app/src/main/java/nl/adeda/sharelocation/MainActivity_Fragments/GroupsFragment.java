package nl.adeda.sharelocation.MainActivity_Fragments;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterface;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterfaceGroupList;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.GPSHelper;
import nl.adeda.sharelocation.Helpers.GroupListAdapter;
import nl.adeda.sharelocation.NameTime;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * Fragment containing an ExpandableListView that displays all groups the user is in. From here,
 * the MapFragment for each group can be launched.
 */
public class GroupsFragment extends Fragment implements CallbackInterface, CallbackInterfaceGroupList {

    ExpandableListView groupList;
    HashMap<String, List<String>> groupMemberUIDs;
    ArrayList<String> groupNames;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groepen, container, false);

        setHasOptionsMenu(true);

        groupList = (ExpandableListView) view.findViewById(R.id.group_list);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Groepen");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseHelper.delegate = this;
            FirebaseHelper.pullFromFirebase(2); // Get users' first and last names
        }

    }

    @Override
    public void onLoginUserDataCallback(User userData) {
        // Has no function here.
    }

    // Sets an adapter on the ExpandableListView containing the names of the groups the
    // user is in, and other users that are in these groups.
    @Override
    public void onGroupDataCallback(ArrayList<String> groupNames, LinkedHashMap<String, List<String>> groupMemberNames,
                                    LinkedHashMap<String, List<String>> groupMemberUIDs, ArrayList<DateTime> endTimes, ArrayList<String> groupKeys) {
        GroupListAdapter.delegate = this;

        // Set adapter on ListView
        NameTime nameTime = new NameTime(groupNames, endTimes);
        GroupListAdapter groupListAdapter = new GroupListAdapter(getContext(), nameTime,
                groupMemberNames, groupKeys);
        groupList.setAdapter(groupListAdapter);

        this.groupMemberUIDs = groupMemberUIDs; // Get list of groupMemberUIDs
        this.groupNames = groupNames; // Get list of groupnames

        // Get and push location coordinates to Firebase
        GPSHelper gpsHelper = new GPSHelper(getContext());
        Location location = gpsHelper.getLocation();
        gpsHelper.locationPusher(location);

    }

    // Callback method, called when all user location information is fetched from Firebase. This
    // information is passed to the MapFragment through a Bundle.
    @Override
    public void onLoadGroupMap(ArrayList<User> users, User currentUserData, String groupName) {
        MapFragment mapFragment = new MapFragment();

        Bundle arguments = new Bundle();

        arguments.putParcelableArrayList("userList", users);
        arguments.putParcelable("currentUser", currentUserData);
        arguments.putString("groupName", groupName);

        if (mapFragment.getArguments() != null) {
            mapFragment.getArguments().putAll(arguments);
        } else {
            mapFragment.setArguments(arguments);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.content_layout, mapFragment);
        ft.addToBackStack("MapFragment");
        ft.commit();
    }

    // Callback method, called when the go-to-map button is pressed. Pulls group member locations
    // in order to initialize the MapFragment, containing markers on these user locations.
    @Override
    public void onGroupListClick(int groupPosition) {
        Object[] keys = groupMemberUIDs.keySet().toArray();
        List<String> group = groupMemberUIDs.get(keys[groupPosition]); // Put member keys into list
        String groupName = groupNames.get(groupPosition);

        // Get locations of all group members
        FirebaseHelper.pullGroupMemberLocations(group, groupName);
    }

    // Called by GroupListAdapter when the user presses the groups' delete button.
    @Override
    public void onGroupDelete(int groupPosition, final String groupId) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        FirebaseHelper.deleteUserFromGroup(groupId); // Delete user from Firebase
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        // Show alert with 'yes' and 'no' answer options
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setMessage("Weet je zeker dat je deze groep wilt verlaten?")
                .setPositiveButton("Ja", dialogClickListener).setNegativeButton("Nee",
                dialogClickListener).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Groepen");
    }
}
