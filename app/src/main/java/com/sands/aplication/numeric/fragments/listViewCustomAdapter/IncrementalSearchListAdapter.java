package com.sands.aplication.numeric.fragments.listViewCustomAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sands.aplication.numeric.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by User on 3/14/2017.
 */

public class IncrementalSearchListAdapter extends ArrayAdapter<IncrementalSearch> {

    private final Context mContext;
    private final int mResource;
    private int lastPosition = -1;

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public IncrementalSearchListAdapter(Context context, int resource, ArrayList<IncrementalSearch> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        //get the persons information
        String n = Objects.requireNonNull(getItem(position)).getN();
        String xn = Objects.requireNonNull(getItem(position)).getXn();
        String fXn = Objects.requireNonNull(getItem(position)).getFXn();

        //Create the person object with the information
        IncrementalSearch incremental = new IncrementalSearch(n, xn, fXn);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.textViewN = convertView.findViewById(R.id.textViewN);
            holder.textViewXn = convertView.findViewById(R.id.textViewXn);
            holder.textViewFXn = convertView.findViewById(R.id.textViewFXn);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.textViewN.setText(incremental.getN());
        holder.textViewXn.setText(incremental.getXn());
        holder.textViewFXn.setText(incremental.getFXn());

        if (holder.textViewN.getText() == "n") {
            holder.textViewN.setTextColor(Color.WHITE);
            holder.textViewXn.setTextColor(Color.WHITE);
            holder.textViewFXn.setTextColor(Color.WHITE);
            holder.textViewN.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewXn.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewFXn.setBackgroundColor(Color.rgb(63, 81, 181));
        } else {
            holder.textViewN.setTextColor(Color.BLACK);
            holder.textViewXn.setTextColor(Color.BLACK);
            holder.textViewFXn.setTextColor(Color.BLACK);
            holder.textViewN.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewXn.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewFXn.setBackgroundColor(Color.TRANSPARENT);
        }


        return convertView;
    }

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView textViewN;
        TextView textViewXn;
        TextView textViewFXn;
    }
}
