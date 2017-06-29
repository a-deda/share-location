package nl.adeda.sharelocation.Helpers.Interfaces;

import java.io.File;
import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Callback interface that is used to return photos and initialize all markers in the MapFragment.
 */

public interface PhotoInterface {
    void returnPhoto(File photoFile);
    void initializeCurrentUserMarker(User userInfo);
    void initializeOtherUserMarkers(ArrayList<User> initializedUsers);
}
