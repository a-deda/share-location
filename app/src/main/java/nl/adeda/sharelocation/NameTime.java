package nl.adeda.sharelocation;

import java.util.ArrayList;
import java.util.BitSet;

import nl.adeda.sharelocation.DateTime;

/**
 * Created by Antonio on 22-6-2017.
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
