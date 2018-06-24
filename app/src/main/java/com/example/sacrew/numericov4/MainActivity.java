package com.example.sacrew.numericov4;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static java.io.File.createTempFile;

public class MainActivity extends AppCompatActivity {
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
    private final String homeInformation = "If you want to report some bug, you can do it in <a href=\"https://github.com/sebospc/numericoV4\">github official project</a> ," +
            " in the play store" +
            " or send us a mail!";
    private final String graphInformation = "In this section you will be able to graph any function, " +
            "but take care with no continuous functions. " +
            "<br><br>The library of this graph is   "+
            "<a href=\"http://www.android-graphview.org/\">GraphView</a>\n" +
            "        by Jonas Grehring"+
            "<br><br>You will see your (correct) functions in \"MY FUNCTIONS\" keyboard options."+
            "<br><br>"+homeInformation;
    private final String oneVariableInfo = "stiven ponga un overview aca :v"+
            "<br><br>"+homeInformation;
    private final String systemEquationInfo = "stiven ponga un overview aca :v"+
            "<br><br>"+homeInformation;
    private final String interpolationInfo = "stiven ponga un overview aca :v"+
            "<br><br>"+homeInformation;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        extraInformation = homeInformation;
        //define poop

        // draweLayout
        android.support.v7.app.ActionBar aBar  = getSupportActionBar();
        aBar.setHomeButtonEnabled(true);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = findViewById(R.id.root);
        final String[] opciones ={"Home","Graph","One Variable","System Equations","Interpolation"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,opciones);
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
            if(temp.exists() && temp.length() != 0) {
                FileInputStream bridgeToRead = new FileInputStream(temp);
                ObjectInputStream objectToRead = new ObjectInputStream(bridgeToRead);
                FunctionStorage func = ((FunctionStorage) objectToRead.readObject());
                if(func.functions != null)
                functionStorage.functions = func.functions ;
            }
            functionStorage.functions.isEmpty();
            graphFragment.temp=temp;
            graphFragment.functionStorage = functionStorage;
            graphFragment.functionStorage.functions.isEmpty();
            oneVariableFragment.temp=temp;
            oneVariableFragment.functionStorage = functionStorage;

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

    AdapterView.OnItemClickListener adap = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 0: openHome();
                    break;
                case 1: openGraph();
                    break;
                case 2: openOneVariable();
                    break;
                case 3: openSystemEquations();
                    break;
                case 4: openInterpolation();
                    break;
                default:
                    break;
            }

        }
    };
    public void openHome(){
        if(idFragment != 0) {
            extraInformation = homeInformation;
            /*
             * remove animations of system equations
             */
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
               if(fragment != null )getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, homeFragment);
            transaction.commit();
            idFragment = 0;
        }
    }
    public void openGraph(){
        if(idFragment != 1) {

            extraInformation = graphInformation;
            /**
             * remove animations of system equations
             */
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                if(fragment != null )getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, graphFragment);
            transaction.commit();
            idFragment = 1;
        }
    }
    public void openOneVariable(){

        if(idFragment != 2) {
            extraInformation = oneVariableInfo;
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            /**
             * remove animations of system equations
             */
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                if(fragment != null )getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, oneVariableFragment);
            transaction.commit();
            idFragment = 2;
        }

    }

    public void openSystemEquations(){
        if(idFragment != 3){
            extraInformation = systemEquationInfo;
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
               if(fragment != null )getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, systemEquationsFragment);
            transaction.commit();
            idFragment = 3;
        }
    }
    public void openInterpolation(){
        if(idFragment != 4){
            extraInformation = interpolationInfo;
            //getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
               if(fragment != null )getSupportFragmentManager().beginTransaction().remove(fragment).commit();
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
        System.out.println("entro item selected");
        switch (item.getItemId()){
            case 1: AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                Spanned myMessage = Html.fromHtml(extraInformation);
                TextView textView = new TextView(this);
                textView.setText(myMessage);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                alertDialog.setView(textView);
                alertDialog.show();
                break;
            default:
                    if(drawerLayout.isDrawerOpen(menuLateral)){
                        drawerLayout.closeDrawer(menuLateral);
                    }else{
                        drawerLayout.openDrawer(menuLateral);
                    }break;

        }

        return true;
    }

    @Override public void onBackPressed() {
     if(graphFragment.keyboardUtils != null){
         if(graphFragment.keyboardUtils.isUp){
             graphFragment.keyboardUtils.closeInternalKeyboard();
             return;
         }
     }
     if(oneVariable.keyboardUtils != null){
         if(oneVariable.keyboardUtils.isUp){
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
    public void onDelete(View view){
        graphFragment.onDelete(view);
    }
}
