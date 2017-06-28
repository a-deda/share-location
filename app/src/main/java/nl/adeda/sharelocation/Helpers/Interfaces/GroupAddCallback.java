package nl.adeda.sharelocation.Helpers.Interfaces;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 28-6-2017.
 */

public interface GroupAddCallback {
    void addUserToList(User user, boolean isInList);
}
