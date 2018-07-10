package com.example.sacrew.numericov4;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.homeFragment;
import com.example.sacrew.numericov4.fragments.interpolation;
import com.example.sacrew.numericov4.fragments.oneVariable;
import com.example.sacrew.numericov4.fragments.systemEquations;
import com.example.sacrew.numericov4.utils.FunctionStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final String homeInformation = "If you want to report some bug, you can do it in <a href=\"https://github.com/sebospc/numericoV4\">Github Official Project</a> ," +
            " in the Play Store" +
            " or send us an email.";
    private ListView menuLateral;
    private DrawerLayout drawerLayout;
    private graphFragment graphFragment;
    private oneVariable oneVariableFragment;
    private homeFragment homeFragment;
    private systemEquations systemEquationsFragment;
    private interpolation interpolationFragment;
    private int idFragment; //0 is graphFragment // 1 is one variable
    private FragmentManager fragmentManager;
    private String extraInformation;
    private android.support.v7.app.ActionBar aBar;
    private final AdapterView.OnItemClickListener adap = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            switch (i) {
                case 0:
                    openHome();
                    break;
                case 1:
                    openGraph();
                    break;
                case 2:
                    openOneVariable();
                    break;
                case 3:
                    openSystemEquations();
                    break;
                case 4:
                    openInterpolation();
                    break;
                default:
                    break;
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        extraInformation = "If you liked the application you can make a <a href=\"https://www.paypal.me/sandscompany\">donation</a>! <br><br>" + homeInformation;
        //define poop

        // draweLayout
        aBar = getSupportActionBar();
        Objects.requireNonNull(aBar).setHomeButtonEnabled(true);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = findViewById(R.id.root);
        final String[] opciones = {"Home", "Graph", "One Variable Equations", "Systems of Equations", "Interpolation"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, opciones);
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setAdapter(adp);
        menuLateral.setOnItemClickListener(adap);


        //graphFragment default
        homeFragment = new homeFragment();
        graphFragment = new graphFragment();
        oneVariableFragment = new oneVariable();
        systemEquationsFragment = new systemEquations();
        interpolationFragment = new interpolation();
        FunctionStorage functionStorage = new FunctionStorage();

        //functionStorage.functions.add("x*x");
        try {
            //File temp = createTempFile("num","eric");

            File temp = new File(this.getCacheDir(), "num");
            //functionStorage.updateStorage(temp,functionStorage);
            if (temp.exists() && temp.length() != 0) {
                FileInputStream bridgeToRead = new FileInputStream(temp);
                ObjectInputStream objectToRead = new ObjectInputStream(bridgeToRead);
                FunctionStorage func = ((FunctionStorage) objectToRead.readObject());
                if (func.functions != null)
                    functionStorage.functions = func.functions;
            }

            graphFragment.temp = temp;
            graphFragment.functionStorage = functionStorage;

            oneVariableFragment.temp = temp;
            oneVariableFragment.functionStorage = functionStorage;

            systemEquationsFragment.temp = temp;
            systemEquationsFragment.functionStorage = functionStorage;

            interpolationFragment.temp = temp;
            interpolationFragment.functionStorage = functionStorage;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainLayout, homeFragment);
        transaction.commit();
        idFragment = 0;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "History").setIcon(R.drawable.ic_info_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;//super.onCreateOptionsMenu(menu);
    }

    private void openHome() {
        if (idFragment != 0) {
            aBar.setTitle("Numerical Analysis 2S");
            extraInformation = "If you liked the application you can make a <a href=\"https://www.paypal.me/sandscompany\">donation</a>! <br><br>" + homeInformation;

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, homeFragment);
            transaction.commit();
            idFragment = 0;
        }
    }

    private void openGraph() {
        if (idFragment != 1) {
            aBar.setTitle("Graph");
            extraInformation = "In this section you will be able to graph any function, " +
                    "but take care with no continuous functions. " +
                    "<br><br>You will see your (correct) functions in \"MY FUNCTIONS\" keyboard options." +
                    "<br><br>The used library for this graph was   " +
                    "<a href=\"http://www.android-graphview.org/\">GraphView</a>\n" +
                    "        by Jonas Grehring." +
                    "<br><br>" + homeInformation;
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, graphFragment);
            transaction.commit();
            idFragment = 1;
        }
    }

    private void openOneVariable() {

        if (idFragment != 2) {
            aBar.setTitle("One Variable Equations");
            extraInformation = "In this section you will find different root-finding methods" +
                    "<br><br>The used libraries in this section were: " +
                    "<a href=\"https://github.com/uklimaschewski/EvalEx\">EvalEx</a>\n" +
                    "        by Udo Klimaschewski, " + "<a href=\"https://github.com/evrencoskun/TableView\">TableView</a>\n" +
                    " by Evren Coşkun and " + "<a href=\"https://github.com/JohnPersano/SuperToasts\">SuperToasts</a>\n" +
                    " by John Persano." +
                    "<br><br>" + homeInformation;
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, oneVariableFragment);
            transaction.commit();
            idFragment = 2;
        }

    }

    private void openSystemEquations() {
        if (idFragment != 3) {
            aBar.setTitle("Systems of Equations");
            extraInformation = "In this section you will be able to solve linear systems equations of n equations with n variables through matrixes with " +
                    "different numerical methods " + "<br><br>" + homeInformation;
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, systemEquationsFragment);
            transaction.commit();
            idFragment = 3;
        }
    }

    private void openInterpolation() {
        if (idFragment != 4) {
            aBar.setTitle("Interpolation");
            extraInformation = "In this section you will be able to find interpolating polynomials through points joining these " +
                    "polynomials and have a similar behavior of the original functions." +
                    "<br><br>We use " +
                    "<a href=\"https://github.com/kexanie/MathView/\">MathView</a>\n" +
                    " by Hour Glass to show mathematical expression and " +
                    "<a href=\"https://github.com/axkr/symja_android_library\">Symja</a>\n" +
                    "        by Axel Kramer."
                    + "<br><br>" + homeInformation;
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, interpolationFragment);
            transaction.commit();
            idFragment = 4;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                Spanned myMessage = Html.fromHtml(extraInformation);
                TextView textView = new TextView(this);
                textView.setText(myMessage);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                alertDialog.setView(textView);
                alertDialog.show();
                break;
            default:
                if (drawerLayout.isDrawerOpen(menuLateral)) {
                    drawerLayout.closeDrawer(menuLateral);
                } else {
                    drawerLayout.openDrawer(menuLateral);
                }
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (oneVariable.keyboardUtils != null) {
            if (oneVariable.keyboardUtils.isUp) {
                oneVariable.keyboardUtils.closeInternalKeyboard();
                return;
            }
        }
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onGraphIt(View view) {
        graphFragment.graphIt(view);
    }

    public void onDelete(View view) {
        graphFragment.onDelete(view);
    }
}
