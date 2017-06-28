package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.User;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 8-6-2017.
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
