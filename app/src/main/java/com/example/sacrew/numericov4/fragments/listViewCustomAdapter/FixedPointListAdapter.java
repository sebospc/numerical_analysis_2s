package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.oneVariableFragments.incrementalSearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class FixedPointListAdapter extends ArrayAdapter<FixedPoint> {

    private static final String TAG = "FixedPointListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView textViewN;
        TextView textViewXn;
        TextView textViewFXn;
        TextView textViewGXn;
        TextView textViewError;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public FixedPointListAdapter(Context context, int resource, ArrayList<FixedPoint> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String n = getItem(position).getN();
        String xn = getItem(position).getXn();
        String fXn = getItem(position).getFXn();
        String gXn = getItem(position).getGXn();
        String error = getItem(position).getError();

        //Create the person object with the information
        FixedPoint fixedPoint = new FixedPoint(n,xn,fXn, gXn, error);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.textViewN = convertView.findViewById(R.id.textViewN);
            holder.textViewXn = convertView.findViewById(R.id.textViewXn);
            holder.textViewFXn = convertView.findViewById(R.id.textViewFXn);
            holder.textViewGXn = convertView.findViewById(R.id.textViewGXn);
            holder.textViewError = convertView.findViewById(R.id.textViewError);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.textViewN.setText(fixedPoint.getN());
        holder.textViewXn.setText(fixedPoint.getXn());
        holder.textViewFXn.setText(fixedPoint.getFXn());
        holder.textViewGXn.setText(fixedPoint.getGXn());
        holder.textViewError.setText(fixedPoint.getError());


        return convertView;
    }
}
