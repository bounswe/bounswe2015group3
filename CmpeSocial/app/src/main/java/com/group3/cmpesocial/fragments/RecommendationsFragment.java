package com.group3.cmpesocial.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.group3.cmpesocial.R;
import com.group3.cmpesocial.classes.Recommendation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationsFragment extends Fragment {

    private final String title = "Recommendatios";
    private ListView listView;

    public RecommendationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_recommendations, container, false);

        listView = (ListView) mView.findViewById(R.id.listView);

        ArrayList<Recommendation> recommendations = new ArrayList<>();
        Recommendation r1 = new Recommendation(0, "Watching Star Wars in Kuzey Kampus Cinema");
        Recommendation r2 = new Recommendation(1, "Orta Düzey Tenis Oyuncuları");

        recommendations.add(r1);
        recommendations.add(r2);

        listView.setAdapter(new RecommendationAdapter(getContext(), recommendations));

        return mView;    }

    public String getTitle(){
        return title;
    }

    public class RecommendationAdapter extends ArrayAdapter<Recommendation> {

        public RecommendationAdapter(Context context, List objects) {
            super(context, R.layout.item_recommendation, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            }
            Recommendation mRecommendation = getItem(position);

            TextView typeTextView = (TextView) convertView.findViewById(R.id.typeTextView);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);

            int type = mRecommendation.getType();
            if(type == Recommendation.TYPE_EVENT)
                typeTextView.setText("EVENT");
            else if (type == Recommendation.TYPE_GROUP)
                typeTextView.setText("GROUP");

            titleTextView.setText(mRecommendation.getTitle());

            return convertView;
        }
    }

}
