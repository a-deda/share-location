package nl.adeda.sharelocation.MainActivity_Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
 * Created by Antonio on 9-6-2017.
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
                                    LinkedHashMap<String, List<String>> groupMemberUIDs, ArrayList<DateTime> endTimes) {
        GroupListAdapter.delegate = this;

        NameTime nameTime = new NameTime(groupNames, endTimes);
        GroupListAdapter groupListAdapter = new GroupListAdapter(getContext(), nameTime, groupMemberNames);
        groupList.setAdapter(groupListAdapter);

        this.groupMemberUIDs = groupMemberUIDs; // Get list of groupMemberUIDs
        this.groupNames = groupNames; // Get list of groupnames

        // Get and push location coordinates to Firebase
        GPSHelper gpsHelper = new GPSHelper(getContext());
        Location location = gpsHelper.getLocation();
        gpsHelper.locationPusher(location);
    }

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

    @Override
    public void onGroupListClick(int groupPosition) {
        Object[] keys = groupMemberUIDs.keySet().toArray();
        List<String> group = groupMemberUIDs.get(keys[groupPosition]); // Put member keys into group
        String groupName = groupNames.get(groupPosition);

        FirebaseHelper.pullGroupMemberLocations(group, groupName);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Groepen");
    }
}
