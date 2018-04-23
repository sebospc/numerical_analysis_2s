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

import java.util.ArrayList;

/**
 * Created by User on 3/14/2017.
 */

public class MultipleRootsListAdapter extends ArrayAdapter<MultipleRoots> {

    private static final String TAG = "MultipleRootsListAdapter";

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
        TextView textViewFDXn;
        TextView textViewFD2Xn;
        TextView textViewError;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public MultipleRootsListAdapter(Context context, int resource, ArrayList<MultipleRoots> objects) {
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
        String fdXn = getItem(position).getFDXn();
        String fd2Xn = getItem(position).getFD2Xn();
        String error = getItem(position).getError();

        //Create the person object with the information
        MultipleRoots multiple = new MultipleRoots(n,xn,fXn, fdXn, fd2Xn,error);

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
            holder.textViewFDXn = convertView.findViewById(R.id.textViewFDXn);
            holder.textViewFD2Xn = convertView.findViewById(R.id.textViewFD2Xn);
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

        holder.textViewN.setText(multiple.getN());
        holder.textViewXn.setText(multiple.getXn());
        holder.textViewFXn.setText(multiple.getFXn());
        holder.textViewFDXn.setText(multiple.getFDXn());
        holder.textViewFD2Xn.setText(multiple.getFD2Xn());
        holder.textViewError.setText(multiple.getError());


        return convertView;
    }
}
