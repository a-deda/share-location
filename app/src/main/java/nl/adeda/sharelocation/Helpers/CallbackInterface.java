package nl.adeda.sharelocation.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 19-6-2017.
 */

public interface CallbackInterface {
    void onLoginUserDataCallback(User userData);
    void onGroupDataCallback(ArrayList<String> groupNames, LinkedHashMap<String, List<String>> groupMemberNames, LinkedHashMap<String, List<String>> groupMemberUIDs, ArrayList<DateTime> endTimes);
    void onLoadGroupMap(ArrayList<User> users, List<String> memberUIDs, User currentUserData);
}
