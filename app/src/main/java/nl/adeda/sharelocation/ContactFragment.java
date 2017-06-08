package nl.adeda.sharelocation;

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

import nl.adeda.sharelocation.Helpers.ContactListAdapter;
import nl.adeda.sharelocation.Helpers.PhotoFixer;

/**
 * Created by Antonio on 7-6-2017.
 */
public class ContactFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacten, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contacten");

        ContactListAdapter adapter = setContactListAdapter(view);

        // TODO: Get data from Firebase
        // Convert images
        PhotoFixer pf = new PhotoFixer();
        Bitmap cp1 = pf.fixPhoto(getResources(), R.mipmap.profile_1);
        Bitmap cp2 = pf.fixPhoto(getResources(), R.mipmap.profile_2);
        Bitmap cp3 = pf.fixPhoto(getResources(), R.mipmap.profile_3);
        Bitmap cp4 = pf.fixPhoto(getResources(), R.mipmap.profile_4);
        Bitmap cp5 = pf.fixPhoto(getResources(), R.mipmap.profile_5);

        // Put test data into list
        Contact contact1 = new Contact("Hans", "van der Plas", cp1);
        Contact contact2 = new Contact("Johan", "Hanenveld", cp2);
        Contact contact3 = new Contact("Geert", "Waldorf", cp3);
        Contact contact4 = new Contact("Peter", "Bakker", cp4);
        Contact contact5 = new Contact("Kim", "Chin Chang-Sing-Pang", cp5);

        adapter.add(contact1);
        adapter.add(contact2);
        adapter.add(contact3);
        adapter.add(contact4);
        adapter.add(contact5);
    }

    private ContactListAdapter setContactListAdapter(View view) {
        ArrayList<Contact> contactenLijst = new ArrayList<>();
        ContactListAdapter adapter = new ContactListAdapter(getContext(), contactenLijst);

        ListView lvContactenLijst = (ListView) view.findViewById(R.id.contacten_lijst);
        lvContactenLijst.setAdapter(adapter);

        return adapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
}

