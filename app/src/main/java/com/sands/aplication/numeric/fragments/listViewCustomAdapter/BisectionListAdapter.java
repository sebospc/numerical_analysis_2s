package com.sands.aplication.numeric.fragments.listViewCustomAdapter;


import android.annotation.SuppressLint;
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

public class BisectionListAdapter extends ArrayAdapter<Bisection> {

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
    public BisectionListAdapter(Context context, int resource, ArrayList<Bisection> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        //get the persons information
        String n = Objects.requireNonNull(getItem(position)).getN();
        String xi = Objects.requireNonNull(getItem(position)).getXi();
        String xs = Objects.requireNonNull(getItem(position)).getXs();
        String xm = Objects.requireNonNull(getItem(position)).getXm();
        String fXm = Objects.requireNonNull(getItem(position)).getFXm();
        String error = Objects.requireNonNull(getItem(position)).getError();

        //Create the person object with the information
        Bisection bisection = new Bisection(n, xi, xs, xm, fXm, error);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.textViewN = convertView.findViewById(R.id.textViewN);
            holder.textViewXi = convertView.findViewById(R.id.textViewXi);
            holder.textViewXs = convertView.findViewById(R.id.textViewXs);
            holder.textViewXm = convertView.findViewById(R.id.textViewXm);
            holder.textViewFXm = convertView.findViewById(R.id.textViewFXm);
            holder.textViewError = convertView.findViewById(R.id.textViewError);

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

        holder.textViewN.setText(bisection.getN());
        holder.textViewXi.setText(bisection.getXi());
        holder.textViewXs.setText(bisection.getXs());
        holder.textViewXm.setText(bisection.getXm());
        holder.textViewFXm.setText(bisection.getFXm());
        holder.textViewError.setText(bisection.getError());
        if (holder.textViewN.getText() == "n") {
            holder.textViewN.setTextColor(Color.WHITE);
            holder.textViewXi.setTextColor(Color.WHITE);
            holder.textViewXs.setTextColor(Color.WHITE);
            holder.textViewXm.setTextColor(Color.WHITE);
            holder.textViewFXm.setTextColor(Color.WHITE);
            holder.textViewError.setTextColor(Color.WHITE);
            holder.textViewN.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewXi.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewXs.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewXm.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewFXm.setBackgroundColor(Color.rgb(63, 81, 181));
            holder.textViewError.setBackgroundColor(Color.rgb(63, 81, 181));
        } else {
            holder.textViewN.setTextColor(Color.BLACK);
            holder.textViewXi.setTextColor(Color.BLACK);
            holder.textViewXs.setTextColor(Color.BLACK);
            holder.textViewXm.setTextColor(Color.BLACK);
            holder.textViewFXm.setTextColor(Color.BLACK);
            holder.textViewError.setTextColor(Color.BLACK);
            holder.textViewN.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewXi.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewXs.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewXm.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewFXm.setBackgroundColor(Color.TRANSPARENT);
            holder.textViewError.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    /**
     * Holds variables in a View
     */
    static class ViewHolder {
        TextView textViewN;
        TextView textViewXi;
        TextView textViewXs;
        TextView textViewXm;
        TextView textViewFXm;
        TextView textViewError;
    }
}
