package com.jvera.chat_app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jvera.chat_app.Helper;
import com.jvera.chat_app.R;


public class UserHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public int base_fragment = R.id.base_fragment;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        createFragment(Helper.createUserListFragment(), base_fragment, "add");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_side_menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_home:
                createFragment(Helper.createUserListFragment(), base_fragment, "replace");
                Helper.toastAnnounce(this, "Home selected");
                break;
            case R.id.nav_profil:
                createFragment(Helper.createProfileFragment(), base_fragment, "replace");
                Helper.toastAnnounce(this, "Profile selected");
                break;
            case R.id.nav_settings:
                Helper.toastAnnounce(this, "Options selected");
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createFragment(Fragment myFragment,int baseFragment, String addOrReplace){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch(addOrReplace) {
            case "replace":
                fragmentTransaction.replace(baseFragment, myFragment);
                break;
            default: //"add"
                fragmentTransaction.add(baseFragment, myFragment);
                break;
        }
        fragmentTransaction.commit();
    }
}
