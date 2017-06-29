package nl.adeda.sharelocation.Helpers.Interfaces;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Callback interface that is called when the updated locations for all users are fetched from
 * Firebase. The method is executed in the MapFragment, where these locations are used to update
 * the map markers.
 */

public interface CallbackGroupUpdate {
    void returnGroupUpdate(ArrayList<User> membersInGroupUpdate, User currentUserData, ArrayList<Marker> otherUserMarkers, Marker currentMarker);
}
