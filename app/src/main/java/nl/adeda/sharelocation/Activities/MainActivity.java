package nl.adeda.sharelocation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nl.adeda.sharelocation.MainActivity_Fragments.ContactFragment;
import nl.adeda.sharelocation.MainActivity_Fragments.GroepenFragment;
import nl.adeda.sharelocation.MainActivity_Fragments.InstellingenFragment;
import nl.adeda.sharelocation.MainActivity_Fragments.KaartFragment;
import nl.adeda.sharelocation.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // TODO: Get savedInstanceState of application
        // TODO: Check if a fragment is still open

        // Set 'kaart' checked & select
        navigationView.getMenu().getItem(0).setChecked(true);

        MenuItem firstMenuItem = navigationView.getMenu().getItem(0);
        selectMenuItem(firstMenuItem);

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void selectMenuItem(MenuItem item) {
        // TODO: Check if fragment is already running

        Fragment fragment = null;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_kaart:
                fragment = new KaartFragment();
                break;
            case R.id.nav_contacten:
                fragment = new ContactFragment();
                break;
            case R.id.nav_groepen:
                fragment = new GroepenFragment();
                break;
            case R.id.nav_instellingen:
                fragment = new InstellingenFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_layout, fragment);
            ft.commit();
        }
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
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_groep_toevoegen) {
            // Start contacten toevoegen
            Intent intent = new Intent(this, GroepToevoegenActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        selectMenuItem(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
