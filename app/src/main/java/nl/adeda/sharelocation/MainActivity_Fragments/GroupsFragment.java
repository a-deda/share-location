package nl.adeda.sharelocation.MainActivity_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.List;

import nl.adeda.sharelocation.Helpers.CallbackInterface;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.GroupListAdapter;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 9-6-2017.
 */
public class GroupsFragment extends Fragment implements CallbackInterface {

    ExpandableListView groupList;
    HashMap<String, List<String>> groupMemberUIDs;

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
            FirebaseHelper.pullFromFirebase(user, 2); // Get users' first and last names
        }

        // TODO (16/6): onItemClick - open KaartFragment with map for group

    }

    @Override
    public void onLoginUserDataCallback(User userData) {
        // Has no function here.
    }

    // Sets an adapter on the ExpandableListView containing the names of the groups the
    // user is in, and other users that are in these groups.
    @Override
    public void onGroupDataCallback(ArrayList<String> groupNames, HashMap<String, List<String>> groupMemberNames, HashMap<String, List<String>> groupMemberUIDs) {
        GroupListAdapter groupListAdapter = new GroupListAdapter(getContext(), groupNames, groupMemberNames);
        groupList.setAdapter(groupListAdapter);

        this.groupMemberUIDs = groupMemberUIDs; // Get list of groupMemberUIDs
    }
}
