package nl.adeda.sharelocation.MainActivity_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import nl.adeda.sharelocation.Activities.LoginActivity;
import nl.adeda.sharelocation.Activities.MainActivity;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 7-6-2017.
 */
public class InstellingenFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instellingen, container, false);

        Spinner refreshDropdown = (Spinner) view.findViewById(R.id.refresh_keuzemenu);
        String[] refreshChoices = new String[]{"30 seconden", "1 minuut", "2 minuten", "5 minuten"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, refreshChoices);
        refreshDropdown.setAdapter(adapter);

        Button uitlogBtn = (Button) view.findViewById(R.id.uitlogBtn);
        uitlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FirebaseHelper.userRef = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Instellingen");

    }
}
