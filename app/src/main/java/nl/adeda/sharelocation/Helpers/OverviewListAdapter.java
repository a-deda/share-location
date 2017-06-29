package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * Adapter for the overview in the MapFragment. Produces a list of displayed users (except for
 * the current user), their photo and their distance to the current user.
 */

public class OverviewListAdapter extends ArrayAdapter {

    public OverviewListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        // Initialize views
        CircleImageView profielFoto = (CircleImageView) convertView.findViewById(R.id.overview_contact_photo);
        TextView name = (TextView) convertView.findViewById(R.id.overview_contact_name);
        TextView distance = (TextView) convertView.findViewById(R.id.overview_contact_distance);

        // Set values to views
        profielFoto.setImageBitmap(user.getMapPhoto());
        name.setText(user.getFirstName() + " " + user.getLastName());
        distance.setText(user.getDistance());

        return convertView;

    }

}
