package nl.adeda.sharelocation;

import java.util.ArrayList;

/**
 * Class that combines the group names with their expiration times to use in the GroupListAdapter.
 */
public class NameTime {

    private ArrayList<String> groupNames;
    private ArrayList<DateTime> endTimes;

    public NameTime(ArrayList<String> groupNames, ArrayList<DateTime> endTimes) {
        this.groupNames = groupNames;
        this.endTimes = endTimes;
    }

    public ArrayList<String> getNames() {
        return this.groupNames;
    }

    public ArrayList<DateTime> getTimes() {
        return this.endTimes;
    }
}
