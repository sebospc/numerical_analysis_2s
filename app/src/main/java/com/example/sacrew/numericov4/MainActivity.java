package com.example.sacrew.numericov4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sacrew.numericov4.fragments.home;
import com.example.sacrew.numericov4.fragments.oneVariable;

public class MainActivity extends AppCompatActivity {
    private ListView menuLateral;
    private DrawerLayout drawerLayout;
    private home homeFragment;
    private oneVariable oneVariableFragment;
    private Boolean first = true;
    private int idFragment; //0 is home // 1 is one variable
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
        final String[] opciones ={"home","oneVariable"};
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,opciones);
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setAdapter(adp);
        menuLateral.setOnItemClickListener(adap);

        //home default
        homeFragment = new home();
        oneVariableFragment = new oneVariable();
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
                case 0: home();
                    break;
                case 1: openOneVariable();
                    break;
                default:
                    break;
            }

        }
    };

    public void openOneVariable(){

        if(idFragment != 1) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, oneVariableFragment);
            transaction.commit();
            idFragment = 1;
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

    public void home(){
        if(idFragment != 0) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.mainLayout)).commit();
            System.out.println("home");
            drawerLayout.closeDrawer(menuLateral);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.mainLayout, homeFragment);
            transaction.commit();
            idFragment = 0;
        }
    }


    public void onGraphIt(View view) {
        homeFragment.graphIt(view);
    }
    public void onDelete(View view){
        homeFragment.onDelete(view);
    }
}
