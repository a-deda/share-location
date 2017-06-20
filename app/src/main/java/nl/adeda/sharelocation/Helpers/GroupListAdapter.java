package nl.adeda.sharelocation.Helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 8-6-2017.
 */

public class GroupListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groupNames;
    private HashMap<String, List<String>> groupMembers;

    public GroupListAdapter(Context context, List<String> groupNames, HashMap<String, List<String>> groupMembers) {
        this.context = context;
        this.groupNames = groupNames;
        this.groupMembers = groupMembers;
    }

    @Override
    public int getGroupCount() {
        return groupNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupMembers.get(groupNames.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupMembers.get(this.groupNames.get(groupPosition)).get(childPosition);
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
        String groupNameText = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
        }

        TextView groupNameTextView = (TextView) convertView.findViewById(R.id.groups_group_name);
        TextView groupTimeTextView = (TextView) convertView.findViewById(R.id.groups_group_time_left);

        groupNameTextView.setText(groupNameText);

        Button goToMapViewBtn = (Button) convertView.findViewById(R.id.go_to_map_btn);
        goToMapViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
