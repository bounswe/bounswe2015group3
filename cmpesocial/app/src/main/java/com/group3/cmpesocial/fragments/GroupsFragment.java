package com.group3.cmpesocial.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.group3.cmpesocial.API.GroupAPI;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.adapters.GroupAdapter;
import com.group3.cmpesocial.classes.Group;

import java.util.ArrayList;

//import com.group3.cmpesocial.GroupDetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private static final String TAG = GroupsFragment.class.getSimpleName();

    private final String title = "Groups";

    private View mView;
    private ListView listView;
    private ArrayList<Group> groupsArray;
    private GroupAdapter adapter;

    public GroupsFragment() {
        // Required empty public constructor
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

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList();
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

    public void refreshList() {
        Log.i(TAG, "refresh");
        groupsArray.clear();
        adapter.clear();
        int result = GroupAPI.allGroups(getContext(), adapter);
        if(result == -1){
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public View getmView() {
        return mView;
    }
}
