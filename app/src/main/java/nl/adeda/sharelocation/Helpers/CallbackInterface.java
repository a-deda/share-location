package nl.adeda.sharelocation.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 19-6-2017.
 */

public interface CallbackInterface {
    void onLoginUserDataCallback(User userData);
    void onGroupDataCallback(ArrayList<String> groupNames, HashMap<String, List<String>> groupMemberNames);
}
