package com.sands.aplication.numeric.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sands.aplication.numeric.R;
import com.sands.aplication.numeric.fragments.interpolationFragments.cubicSpline;
import com.sands.aplication.numeric.fragments.interpolationFragments.lagrange;
import com.sands.aplication.numeric.fragments.interpolationFragments.linearSpline;
import com.sands.aplication.numeric.fragments.interpolationFragments.newtonInterpolator;
import com.sands.aplication.numeric.fragments.interpolationFragments.quadraticSpline;
import com.sands.aplication.numeric.pagerAdapter;
import com.sands.aplication.numeric.utils.FunctionStorage;
import com.sands.aplication.numeric.utils.KeyboardUtils;
import com.sands.aplication.numeric.utils.graphUtils;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.matheclipse.core.eval.ExprEvaluator;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.sands.aplication.numeric.fragments.homeFragment.poolColors;
import static com.sands.aplication.numeric.fragments.oneVariable.keyboardUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class interpolation extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static TableLayout vectors;
    public static GraphView interpolationGraph;
    private final graphUtils graphUtils = new graphUtils();
    private final HashMap<EditText, Pair<PointsGraphSeries<DataPoint>, Integer>> viewToPoint = new HashMap<>();
    public FunctionStorage functionStorage;
    public File temp;
    private int count = 3;

    public interpolation() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //clear toasts
        SuperActivityToast.cancelAllSuperToasts();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interpolation, container, false);

        vectors = view.findViewById(R.id.vectors);
        ViewPager slideView = view.findViewById(R.id.pager);
        Button btnNext = view.findViewById(R.id.btnNext);
        Button btnPrev = view.findViewById(R.id.btnPrev);
        interpolationGraph = view.findViewById(R.id.interpolationGraph);
        keyboardUtils = new KeyboardUtils(view, R.id.keyboardView, getContext());
        keyboardUtils.graph = interpolationGraph;
        view.post(new Runnable() {
            @Override
            public void run() {
                keyboardUtils.heighAuxGraph = interpolationGraph.getMeasuredHeight();
            }
        });
        ImageButton add = view.findViewById(R.id.addRow);
        ImageButton homeGraph = view.findViewById(R.id.homeGraphButton);
        final List<LineGraphSeries<DataPoint>> listSeries = graphUtils.graphPharallel(50, "x", 0);
        homeGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    interpolationGraph.removeSeries(inSerie);
                interpolationGraph.getViewport().setYAxisBoundsManual(true);
                interpolationGraph.getViewport().setMinY(-3);
                interpolationGraph.getViewport().setMaxY(3);

                interpolationGraph.getViewport().setXAxisBoundsManual(true);
                interpolationGraph.getViewport().setMinX(-3);
                interpolationGraph.getViewport().setMaxX(3);
                interpolationGraph.getViewport().setScrollable(true);
                interpolationGraph.getViewport().setScrollableY(true);
                interpolationGraph.getViewport().setScalable(true);
                interpolationGraph.getViewport().setScalableY(true);
                for (LineGraphSeries<DataPoint> inSerie : listSeries)
                    interpolationGraph.addSeries(inSerie);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        ImageButton remove = view.findViewById(R.id.deleteRow);

        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                removeRow();
            }
        });
        List<Fragment> fragments = new LinkedList<>();
        fragments.add(new newtonInterpolator());
        fragments.add(new lagrange());
        fragments.add(new linearSpline());
        fragments.add(new quadraticSpline());
        fragments.add(new cubicSpline());
        pagerAdapter pager = new pagerAdapter(getChildFragmentManager(), fragments);
        slideView.setAdapter(pager);
        if(slideView.getCurrentItem() == 0){
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.INVISIBLE);
        }else if (slideView.getCurrentItem() == 4){
            btnNext.setVisibility(View.INVISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }else{
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.VISIBLE);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideView.setCurrentItem(slideView.getCurrentItem()+1);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideView.setCurrentItem(slideView.getCurrentItem()-1);
            }
        });
        slideView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                SuperActivityToast.cancelAllSuperToasts();
            }

            @Override
            public void onPageSelected(int position) {
                if(slideView.getCurrentItem() == 0){
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.INVISIBLE);
                }else if (slideView.getCurrentItem() == 4){
                    btnNext.setVisibility(View.INVISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }else{
                    btnNext.setVisibility(View.VISIBLE);
                    btnPrev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initialize(count);
        for (LineGraphSeries<DataPoint> inSerie : graphUtils.graphPharallel(50, "x", 0))
            interpolationGraph.addSeries(inSerie);
        interpolationGraph.getViewport().setYAxisBoundsManual(true);
        interpolationGraph.getViewport().setMinY(-3);
        interpolationGraph.getViewport().setMaxY(3);
        interpolationGraph.getViewport().setXAxisBoundsManual(true);
        interpolationGraph.getViewport().setMinX(-3);
        interpolationGraph.getViewport().setMaxX(3);
        interpolationGraph.getViewport().setScrollable(true); // enables horizontal scrolling
        interpolationGraph.getViewport().setScrollableY(true); // enables vertical scrolling
        interpolationGraph.getViewport().setScalable(true);// esto genera errores se podria solucionar pero
        interpolationGraph.getViewport().setScalableY(true);// es complejo, es para el zoom
        new ExprEvaluator();

        for (String function : functionStorage.functions)
            keyboardUtils.addFunction(function, getContext(), functionStorage, temp);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addRow() {
        int aux = ((TableRow) vectors.getChildAt(0)).getChildCount() - 1;
        EditText text = (EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(aux);
        try {
            int auxColor = poolColors.remove(0);
            poolColors.add(auxColor);
            Integer checkInteger = Integer.parseInt(text.getText().toString());
            EditText key = defaultEditText(String.valueOf(checkInteger + 1), auxColor);
            ((TableRow) vectors.getChildAt(0)).addView(key);
            ((TableRow) vectors.getChildAt(1)).addView(defaultEditText("0", auxColor));

            viewToPoint.put(key, new Pair<>(updatePointGraph(checkInteger + 1, 0, auxColor), auxColor));
            count = count + 1;
        } catch (Exception isDouble) {
            try {
                int auxColor = poolColors.remove(0);
                poolColors.add(auxColor);
                double checkDouble = Double.parseDouble(text.getText().toString());
                EditText key = defaultEditText(String.valueOf(checkDouble + 1), auxColor);
                ((TableRow) vectors.getChildAt(0)).addView(key);
                ((TableRow) vectors.getChildAt(1)).addView(defaultEditText("0", auxColor));

                viewToPoint.put(key, new Pair<>(updatePointGraph(checkDouble + 1, 0, auxColor), auxColor));
                count = count + 1;
            } catch (Exception d) {
                text.setError("invalid number");
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void removeRow() {
        if (count > 2) {
            int last = ((TableRow) vectors.getChildAt(0)).getChildCount() - 1;
            EditText key = (EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(last);
            ((TableRow) vectors.getChildAt(0)).removeViewAt(last);
            ((TableRow) vectors.getChildAt(1)).removeViewAt(last);
            interpolationGraph.removeSeries(viewToPoint.get(key).first);
            viewToPoint.remove(key);
            count = count - 1;
        }

    }


    private PointsGraphSeries<DataPoint> updatePointGraph(double x, double y, int color) {

        PointsGraphSeries<DataPoint> point = graphUtils.graphPoint(x, y, PointsGraphSeries.Shape.POINT, color, false);
        interpolationGraph.addSeries(point);
        return point;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initialize(int count) {
        TableRow aux = new TableRow(getContext());
        aux.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TableRow aux2 = new TableRow(getContext());
        aux2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < count; i++) {
            int auxColor = poolColors.remove(0);
            poolColors.add(auxColor);
            EditText key = defaultEditText(String.valueOf(i), auxColor);
            aux.addView(key);
            aux2.addView(defaultEditText("0", auxColor));

            viewToPoint.put(key, new Pair<>(updatePointGraph(i, 0, auxColor), auxColor));
        }
        vectors.addView(aux, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        vectors.addView(aux2, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private EditText defaultEditText(String value, int color) {
        EditText text = new EditText(getContext());
        text.setLayoutParams(new TableRow.LayoutParams(100, 110));
        text.setEms(2);
        text.setMaxLines(1);
        text.setBackground(null);
        text.setTextColor(Color.WHITE);
        text.setTypeface(null, Typeface.BOLD);
        text.setBackgroundColor(color);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        keyboardUtils.registerEdittext(text, getContext(), getActivity());
        text.addTextChangedListener(new TextChangedListener<EditText>(text) {
            @Override
            public void onTextChanged(EditText target) {
                try {
                    double x;
                    double y;
                    EditText key = target;
                    int aux = ((TableRow) vectors.getChildAt(0)).indexOfChild(key);
                    if (aux != -1) {
                        interpolationGraph.removeSeries(viewToPoint.get(key).first);
                        x = Double.parseDouble(target.getText().toString());
                        y = Double.parseDouble(((EditText) ((TableRow) vectors.getChildAt(1)).getChildAt(aux)).getText().toString());
                    } else {
                        key = ((EditText) ((TableRow) vectors.getChildAt(0)).getChildAt(((TableRow) vectors.getChildAt(1)).indexOfChild(target)));
                        interpolationGraph.removeSeries(viewToPoint.get(key).first);
                        x = Double.parseDouble(key.getText().toString());
                        y = Double.parseDouble(target.getText().toString());
                    }


                    if (viewToPoint.containsKey(key)) {
                        int auxColor = viewToPoint.get(key).second;
                        viewToPoint.put(key, new Pair<>(updatePointGraph(x, y, auxColor), auxColor));
                    }
                } catch (Exception ignored) {

                }
            }
        });

        text.setText(value);
        return text;
    }

    private abstract class TextChangedListener<T> implements TextWatcher {
        private final T target;

        TextChangedListener(T target) {
            this.target = target;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target);
        }

        protected abstract void onTextChanged(T target);
    }

}
