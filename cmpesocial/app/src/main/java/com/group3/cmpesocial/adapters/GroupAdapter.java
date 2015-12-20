package com.group3.cmpesocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.group.GroupDetailActivity;
import com.group3.cmpesocial.classes.Group;

import java.util.List;

/**
 * Created by Tuba on 20/12/15.
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private List objects;

    public GroupAdapter(Context context, List objects) {
        super(context, R.layout.item_group, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, parent, false);

        final Group mGroup = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);

        titleTextView.setText(mGroup.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupDetailActivity.class);
                intent.putExtra("id", mGroup.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
