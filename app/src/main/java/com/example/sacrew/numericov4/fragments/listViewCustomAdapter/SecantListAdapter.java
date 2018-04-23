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

public class SecantListAdapter extends ArrayAdapter<Secant> {

    private static final String TAG = "SecantListAdapter";

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
    public SecantListAdapter(Context context, int resource, ArrayList<Secant> objects) {
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
        Secant secant = new Secant(n,xn,fXn, fdXn, fd2Xn,error);

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

        holder.textViewN.setText(secant.getN());
        holder.textViewXn.setText(secant.getXn());
        holder.textViewFXn.setText(secant.getFXn());
        holder.textViewFDXn.setText(secant.getFDXn());
        holder.textViewFD2Xn.setText(secant.getFD2Xn());
        holder.textViewError.setText(secant.getError());


        return convertView;
    }
}
