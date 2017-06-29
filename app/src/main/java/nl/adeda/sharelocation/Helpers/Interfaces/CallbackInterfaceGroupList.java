package nl.adeda.sharelocation.Helpers.Interfaces;

/**
 * Callbacks that come from the GroupListAdapter, used for the ExpandableListView in the
 * GroupsFragment. 
 */
public interface CallbackInterfaceGroupList {
    void onGroupListClick(int groupPosition);
    void onGroupDelete(int groupPosition, String groupId);
}
