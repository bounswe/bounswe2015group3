package com.group3.cmpesocial.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.activities.event.EventDetailActivity;
import com.group3.cmpesocial.classes.Event;

import java.util.ArrayList;

/**
 * Created by Tuba on 11/12/15.
 */
public class RVEventAdapter extends RecyclerView.Adapter<RVEventAdapter.ViewHolder>{

    private ArrayList<Event> events;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView;
        public ImageView pictureButton;
        public int id;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            pictureButton = (ImageView) itemView.findViewById(R.id.pictureButton);
            pictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventDetailActivity.class);
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
    public RVEventAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_detail_event, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event mEvent = events.get(position);
        String name = mEvent.getName();
        holder.setId(mEvent.getId());
        holder.nameTextView.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }

    public void add(Event item) {
        events.add(item);
        notifyItemInserted(getItemCount());
    }

    public void remove(Event item) {
        int position = events.indexOf(item);
        events.remove(position);
        notifyItemRemoved(position);
    }

}
