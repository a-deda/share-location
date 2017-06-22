package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
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
 * Created by Antonio on 8-6-2017.
 */

public class OverviewListAdapter extends ArrayAdapter {

    public OverviewListAdapter(Context context, ArrayList<User> users/*, ArrayList<Bitmap> userPhotos*/) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = (User) getItem(position);
        //Bitmap userPhoto = (Bitmap) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        CircleImageView profielFoto = (CircleImageView) convertView.findViewById(R.id.overview_contact_photo);
        TextView name = (TextView) convertView.findViewById(R.id.overview_contact_name);
        TextView distance = (TextView) convertView.findViewById(R.id.overview_contact_distance);

        //profielFoto.setImageBitmap(userPhoto);
        name.setText(user.getVoornaam() + " " + user.getAchternaam());
        distance.setText(user.getDistance());

        return convertView;

    }

}
