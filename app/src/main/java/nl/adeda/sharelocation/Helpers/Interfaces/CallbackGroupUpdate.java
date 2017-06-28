package nl.adeda.sharelocation.Helpers.Interfaces;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 21-6-2017.
 */

public interface CallbackGroupUpdate {
    void returnGroupUpdate(ArrayList<User> membersInGroupUpdate, User currentUserData, ArrayList<Marker> otherUserMarkers, Marker currentMarker);
}
