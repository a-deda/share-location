package nl.adeda.sharelocation.MainActivity_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 9-6-2017.
 */
public class GroepenFragment extends Fragment {

    ListView groupList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groepen, container, false);

        setHasOptionsMenu(true);

        groupList = (ListView) view.findViewById(R.id.group_list);

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
            FirebaseHelper.pullFromFirebase(user, 2, null, null);
        }

        // TODO (16/6): Build new adapter for groupnames

        // TODO (16/6): onItemClick - open KaartFragment with map for group






    }
}
