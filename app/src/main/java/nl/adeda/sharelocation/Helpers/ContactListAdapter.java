package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.Contact;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 8-6-2017.
 */

public class ContactListAdapter extends ArrayAdapter {
    public ContactListAdapter(Context context, ArrayList<Contact> contacten) {
        super(context, 0, contacten);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = (Contact) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_contact_list, parent, false);
        }

        CircleImageView profielFoto = (CircleImageView) convertView.findViewById(R.id.contacten_lijst_foto);
        TextView naam = (TextView) convertView.findViewById(R.id.contacten_lijst_naam);

        profielFoto.setImageBitmap(contact.foto);
        String completeNaam = contact.voornaam + " " + contact.achternaam;

        naam.setText(completeNaam);

        return convertView;

    }
}
