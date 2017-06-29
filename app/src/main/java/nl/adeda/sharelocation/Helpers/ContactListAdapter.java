package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * The adapter that is used to fill the list in the AddGroupActivity, containing users that have
 * to be added to a new group.
 */

public class ContactListAdapter extends ArrayAdapter {

    public ContactListAdapter(Context context, ArrayList<User> contacten) {
        super(context, 0, contacten);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_contact_list, parent, false);
        }

        TextView naam = (TextView) convertView.findViewById(R.id.contact_list_name);
        TextView email = (TextView) convertView.findViewById(R.id.contact_list_email);

        String completeNaam = user.voornaam + " " + user.achternaam;

        naam.setText(completeNaam);
        email.setText(user.email);

        return convertView;

    }

}
