package nl.adeda.sharelocation.MainActivity_Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import nl.adeda.sharelocation.User;
import nl.adeda.sharelocation.Helpers.ContactListAdapter;
import nl.adeda.sharelocation.Helpers.PhotoFixer;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 7-6-2017.
 */
public class ContactFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacten, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contacten");

        ContactListAdapter adapter = setContactListAdapter(view);

        /*
        // Convert images
        PhotoFixer pf = new PhotoFixer();
        Bitmap cp1 = pf.fixPhoto(getResources(), R.mipmap.profile_1);
        Bitmap cp2 = pf.fixPhoto(getResources(), R.mipmap.profile_2);
        Bitmap cp3 = pf.fixPhoto(getResources(), R.mipmap.profile_3);
        Bitmap cp4 = pf.fixPhoto(getResources(), R.mipmap.profile_4);
        Bitmap cp5 = pf.fixPhoto(getResources(), R.mipmap.profile_5);

        // Put test data into list
        User contact1 = new User("Hans", "van der Plas", cp1);
        User contact2 = new User("Johan", "Hanenveld", cp2);
        User contact3 = new User("Geert", "Waldorf", cp3);
        User contact4 = new User("Peter", "Bakker", cp4);
        User contact5 = new User("Kim", "Chin Chang-Sing-Pang", cp5);

        adapter.add(contact1);
        adapter.add(contact2);
        adapter.add(contact3);
        adapter.add(contact4);
        adapter.add(contact5);
        */
    }

    private ContactListAdapter setContactListAdapter(View view) {
        ArrayList<User> contactenLijst = new ArrayList<>();
        ContactListAdapter adapter = new ContactListAdapter(getContext(), contactenLijst);

        ListView lvContactenLijst = (ListView) view.findViewById(R.id.contacten_lijst);
        lvContactenLijst.setAdapter(adapter);

        return adapter;
    }
}

