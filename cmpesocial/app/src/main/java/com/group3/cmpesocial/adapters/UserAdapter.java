package com.group3.cmpesocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.UserDetailActivity;
import com.group3.cmpesocial.classes.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tuba on 05/01/16.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List objects;

    public UserAdapter(Context context, List objects) {
        super(context, R.layout.item_event, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);

        final User mUser = getItem(position);

        ImageView profileImageView = (ImageView) convertView.findViewById(R.id.profileImageView);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        nameTextView.setText(mUser.getName() + " " + mUser.getSurname());
        String url = mUser.getProfilePictureURL();
        if (url != null && !url.equals("")){
            Picasso.with(context).load(url).into(profileImageView);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("id", mUser.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getName().hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
