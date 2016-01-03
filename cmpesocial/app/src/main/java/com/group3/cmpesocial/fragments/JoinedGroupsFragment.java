package com.group3.cmpesocial.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.GroupAdapter;
import com.group3.cmpesocial.classes.Group;

import java.util.ArrayList;

/**
 * Created by Tuba on 20/12/15.
 */
public class JoinedGroupsFragment extends Fragment {

    private static final String TAG = JoinedGroupsFragment.class.getSimpleName();

    private final String title = "Joined Groups";

    private View mView;
    private ListView listView;
    private ArrayList<Group> groupsArray;
    private GroupAdapter adapter;
    private JsonObject json;
    private int user_id;

    public JoinedGroupsFragment() {
        // Required empty public constructor
    }

    public void setUserID(int id){
        this.user_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_groups, container, false);

        listView = (ListView) mView.findViewById(R.id.listView);

        groupsArray = new ArrayList<>();

        adapter = new GroupAdapter(getContext(), groupsArray);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        json = new JsonObject();
        json.addProperty("id", user_id);

        if (groupsArray.size() == 0) {
            groupsArray = GroupAPI.getJoinedGroups(json, getContext());
            adapter.addAll(groupsArray);
        }

        return mView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View getmView() {
        return mView;
    }

}
