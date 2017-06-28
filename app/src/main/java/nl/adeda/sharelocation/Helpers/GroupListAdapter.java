package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.DateTime;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackInterfaceGroupList;
import nl.adeda.sharelocation.NameTime;
import nl.adeda.sharelocation.R;

/**
 * Created by Antonio on 8-6-2017.
 */

public class GroupListAdapter extends BaseExpandableListAdapter {

    public static CallbackInterfaceGroupList delegate;
    private Context context;
    private NameTime nameTime;
    private HashMap<String, List<String>> groupMembers;

    public GroupListAdapter(Context context, NameTime nameTime, HashMap<String, List<String>> groupMembers) {
        this.context = context;
        this.nameTime = nameTime;
        this.groupMembers = groupMembers;
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
        Object[] objects = new Object[2];
        objects[0] = nameTime.getNames().get(groupPosition);
        if (nameTime.getTimes() != null) {
            objects[1] = nameTime.getTimes().get(groupPosition);
        }
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.groups_group_name);
        TextView endTimeTextView = (TextView) convertView.findViewById(R.id.groups_group_time_left);

        String endTimeString = "";
        if (endTime != null) {
            endTimeString = "Tot " + endTime.getDay() + "-" + endTime.getMonth() + "-" + endTime.getYear() + " om " + endTime.getHour() + ":" +
                    endTime.getMinute();
        }
        groupNameTextView.setText(groupNameText);
        endTimeTextView.setText(endTimeString);

        Button goToMapViewBtn = (Button) convertView.findViewById(R.id.go_to_map_btn);
        goToMapViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onGroupListClick(groupPosition);
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

        CircleImageView groupMemberPhotoView = (CircleImageView) convertView.findViewById(R.id.groups_contact_photo);
        TextView groupMemberTextView = (TextView) convertView.findViewById(R.id.groups_contact_name);

        groupMemberTextView.setText(groupMemberText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
