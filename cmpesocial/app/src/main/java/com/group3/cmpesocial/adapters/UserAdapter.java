package com.group3.cmpesocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.UserDetailActivity;
import com.group3.cmpesocial.classes.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> users;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView;
        public Button profileButton;
        public int id;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            profileButton = (Button) itemView.findViewById(R.id.profileButton);
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDetailActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
        }
        public void setId(int id){
            this.id = id;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_detail_user, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User mUser = users.get(position);
        String name = mUser.getName();
        String surname = mUser.getSurname();
        holder.setId(mUser.getId());
        holder.nameTextView.setText(name + "\n" + surname);
        holder.profileButton.setText(name.charAt(0) + "" + surname.charAt(0));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }

    public void add(User item) {
        users.add(item);
        notifyItemInserted(getItemCount());
    }

    public void remove(User item) {
        int position = users.indexOf(item);
        users.remove(position);
        notifyItemRemoved(position);
    }

    private boolean containsUser(int id_user){

    }

}
