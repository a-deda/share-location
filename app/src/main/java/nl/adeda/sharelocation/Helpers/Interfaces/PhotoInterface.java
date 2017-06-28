package nl.adeda.sharelocation.Helpers.Interfaces;

import java.io.File;
import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 22-6-2017.
 */

public interface PhotoInterface {
    void returnPhoto(File photoFile);
    //void initializeCurrentUserMarker(User userInfo, ArrayList<User> userList);
    void initializeCurrentUserMarker(User userInfo);
    void initializeOtherUserMarkers(ArrayList<User> initializedUsers);
}
