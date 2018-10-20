package com.jvera.chat_app;

import android.os.Bundle;
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

import com.jvera.chat_app.fragment.ProfilFragment;
import com.jvera.chat_app.fragment.UserListFragment;

public class UserHomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static private String TAG = LoginActivity.class.getSimpleName();
    private boolean fragmentOpen = false;
    public int base_fragment = R.id.base_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        Fragment fragment = new UserListFragment();
        createFragment(fragment, base_fragment,"add");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment = new UserListFragment();
            createFragment(fragment, base_fragment,"replace");

            Helper.toastAnnounce(this,"Home selected");

        } else if (id == R.id.nav_profil) {
            Fragment fragment = new ProfilFragment();
            createFragment(fragment, base_fragment,"replace");

            Helper.toastAnnounce(this,"Profil selected");

        } else if (id == R.id.nav_settings) {
            Helper.toastAnnounce(this,"Options selected");

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createFragment(Fragment myFragment,int baseFragment, String addOrReplace){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch(addOrReplace) {
            case "add":
                fragmentTransaction.add(baseFragment, myFragment);
                break;
            case "replace":
                fragmentTransaction.replace(baseFragment, myFragment);
                break;
            default:
                fragmentTransaction.add(baseFragment, myFragment);
                break;
        }
        fragmentTransaction.commit();
    }

}
