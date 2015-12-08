package com.group3.cmpesocial.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group3.cmpesocial.API;
//import com.group3.cmpesocial.GroupDetailActivity;
import com.group3.cmpesocial.NewGroupActivity;
import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private static final String TAG = GroupsFragment.class.getSimpleName();

    private final String title = "Groups";
    private FloatingActionButton createGroupButton;
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
        View mView = inflater.inflate(R.layout.fragment_groups, container, false);

        createGroupButton = (FloatingActionButton) mView.findViewById(R.id.createGroupButton);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewGroupActivity.class);
                startActivity(intent);
            }
        });

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
        int result = API.allGroups(getContext(), adapter);
        if(result == -1){
            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public class GroupAdapter extends ArrayAdapter<Group> {

        public GroupAdapter(Context context, List objects) {
            super(context, R.layout.item_group, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);

            final Group mGroup = getItem(position);


            TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);




            Log.d(TAG, mGroup.getName());
            titleTextView.setText(mGroup.getName());


           /* convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                    intent.putExtra("id", mGroup.getId());
                    startActivity(intent);
                }
            });*/

            return convertView;
        }
    }

}
