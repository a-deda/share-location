package nl.adeda.sharelocation.Helpers.Interfaces;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.User;

/**
 * Callback interface containing three different methods to return user- and group-related data
 * that is fetched from Firebase.
 */

public interface CallbackInterface {
    void onLoginUserDataCallback(User userData);
    void onGroupDataCallback(ArrayList<String> groupNames, LinkedHashMap<String, List<String>> groupMemberNames, LinkedHashMap<String, List<String>> groupMemberUIDs, ArrayList<DateTime> endTimes, ArrayList<String> groupKeys);
    void onLoadGroupMap(ArrayList<User> users, User currentUserData, String groupName);
}
