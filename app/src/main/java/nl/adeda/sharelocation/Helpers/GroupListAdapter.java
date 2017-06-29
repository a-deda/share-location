package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterfaceGroupList;
import nl.adeda.sharelocation.NameTime;
import nl.adeda.sharelocation.R;

/**
 * Adapter for the ExpandableListView in the GroupsFragment.
 */

public class GroupListAdapter extends BaseExpandableListAdapter {

    public static CallbackInterfaceGroupList delegate;
    private Context context;
    private NameTime nameTime;
    private HashMap<String, List<String>> groupMembers;
    private ArrayList<String> groupIds;

    public GroupListAdapter(Context context, NameTime nameTime, HashMap<String, List<String>>
            groupMembers, ArrayList<String> groupIds) {
        this.context = context;
        this.nameTime = nameTime;
        this.groupMembers = groupMembers;
        this.groupIds = groupIds;
    }

    @Override
    public int getGroupCount() {
        return nameTime.getNames().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupMembers.get(nameTime.getNames().get(groupPosition)).size();
    }

    @Override
    public Object[] getGroup(int groupPosition) {
        Object[] objects = new Object[3];
        objects[0] = nameTime.getNames().get(groupPosition); // Groupname
        if (nameTime.getTimes() != null) {
            objects[1] = nameTime.getTimes().get(groupPosition); // Expiration date & time
        }
        objects[2] = groupIds.get(groupPosition); // The keys of the group
        return objects;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupMembers.get(this.nameTime.getNames().get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupNameText = (String) getGroup(groupPosition)[0];
        DateTime endTime = (DateTime) getGroup(groupPosition)[1];
        final String groupId = (String) getGroup(groupPosition)[2];

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.groups_group_name);
        TextView endTimeTextView = (TextView) convertView.findViewById(R.id.groups_group_time_left);

        // Build expiration date & time string
        String endTimeString = "";
        if (endTime != null) {
            endTimeString = "Tot " + endTime.getDay() + "-" + endTime.getMonth() + "-" + endTime.getYear() + " om " + endTime.getHour() + ":" +
                    endTime.getMinute();
        }
        groupNameTextView.setText(groupNameText);
        endTimeTextView.setText(endTimeString);

        Button goToMapViewBtn = (Button) convertView.findViewById(R.id.go_to_map_btn);
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_group_btn);

        // OnClickListeners for buttons on Group level (go to map & delete button)
        goToMapViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onGroupListClick(groupPosition);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onGroupDelete(groupPosition, groupId);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String groupMemberText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_contact_item, null);
        }

        TextView groupMemberTextView = (TextView) convertView.findViewById(R.id.groups_contact_name);

        groupMemberTextView.setText(groupMemberText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
