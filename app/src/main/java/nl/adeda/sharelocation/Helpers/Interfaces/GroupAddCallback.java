package nl.adeda.sharelocation.Helpers.Interfaces;

import nl.adeda.sharelocation.User;

/**
 * Callback interface that is called to add users to the ListView in the AddGroupActivity.
 */

public interface GroupAddCallback {
    void addUserToList(User user, boolean isInList);
}
