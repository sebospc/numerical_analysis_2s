package com.example.sacrew.numericov4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sacrew.numericov4.fragments.graphFragment;
import com.example.sacrew.numericov4.fragments.homeFragment;
import com.example.sacrew.numericov4.fragments.systemEquationsFragment;
import com.example.sacrew.numericov4.fragments.systemEquations.gaussSimple;
import com.example.sacrew.numericov4.fragments.oneVariable;

import static com.example.sacrew.numericov4.fragments.systemEquationsFragment.animatorSet;

public class MainActivity extends AppCompatActivity {
    private ListView menuLateral;
    private DrawerLayout drawerLayout;
    private graphFragment graphFragment;
    private oneVariable oneVariableFragment;
    private gaussSimple gaussSimpleFragment;
    private homeFragment homeFragment;
    private systemEquationsFragment linearEquationsFragment;
    private int idFragment; //0 is graphFragment // 1 is one variable
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define poop

        // draweLayout
        android.support.v7.app.ActionBar aBar  = getSupportActionBar();
        aBar.setHomeButtonEnabled(true);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout = findViewById(R.id.root);
        final String[] opciones ={"Home","Graph","One Variable","System Equations"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,opciones);
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setAdapter(adp);
        menuLateral.setOnItemClickListener(adap);

        //graphFragment default
        homeFragment = new homeFragment();
        graphFragment = new graphFragment();
        oneVariableFragment = new oneVariable();
        gaussSimpleFragment = new gaussSimple();
        linearEquationsFragment = new systemEquationsFragment();


        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainLayout, homeFragment);
        transaction.commit();
        idFragment = 0;



    }

    AdapterView.OnItemClickListener adap = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String selected = (String) menuLateral.getAdapter().getItem(i);
            switch (i) {
                case 0: openHome();
                    break;
                case 1: openGraph();
                    break;
                case 2: openOneVariable();
                    break;
                case 3: openLinearEquations();
                    break;
                default:
                    break;
            }

        }
    };
    public void openHome(){
        if(idFragment != 0) {
            /**
             * remove animations of system equations
             */
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();

            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, homeFragment);
            transaction.commit();
            idFragment = 0;
        }
    }
    public void openGraph(){
        if(idFragment != 1) {
            /**
             * remove animations of system equations
             */
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();

            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, graphFragment);
            transaction.commit();
            idFragment = 1;
        }
    }
    public void openOneVariable(){

        if(idFragment != 2) {
            /**
             * remove animations of system equations
             */
            animatorSet.removeAllListeners();
            animatorSet.end();
            animatorSet.cancel();
            
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, oneVariableFragment);
            transaction.commit();
            idFragment = 2;
        }

    }
    public void openLinearEquations(){
        if(idFragment != 3){
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, linearEquationsFragment);
            transaction.commit();
            idFragment = 3;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerLayout.isDrawerOpen(menuLateral)){
            drawerLayout.closeDrawer(menuLateral);
        }else{
            drawerLayout.openDrawer(menuLateral);
        }
        return true;
    }



    


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onGraphIt(View view) {
        graphFragment.graphIt(view);
    }
    public void onDelete(View view){
        graphFragment.onDelete(view);
    }
}
