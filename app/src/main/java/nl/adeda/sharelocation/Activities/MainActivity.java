package nl.adeda.sharelocation.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.PhotoInterface;
import nl.adeda.sharelocation.MainActivity_Fragments.GroupsFragment;
import nl.adeda.sharelocation.MainActivity_Fragments.InstellingenFragment;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PhotoInterface {

    CircleImageView userImage;
    PhotoInterface delegate;

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
        View navigationHeader = navigationView.getHeaderView(0);

        // TODO: Get savedInstanceState for application

        // Get intent from FirebaseHelper.returnData after login
        User user = (User) getIntent().getSerializableExtra("userData");

        // Set navigation drawer for current user (data from Firebase)
        TextView nameField = (TextView) navigationHeader.findViewById(R.id.nav_header_name);
        TextView emailField = (TextView) navigationHeader.findViewById(R.id.nav_header_email);
        userImage = (CircleImageView) navigationHeader.findViewById(R.id.nav_header_photo);

        String firstName = user.getVoornaam();
        String lastName = user.getAchternaam();

        String email = loadUser();

        nameField.setText(String.format("%s %s", firstName, lastName));
        emailField.setText(email);

        // TODO: Check if a fragment is still open

        // Set 'group' checked & select
        navigationView.getMenu().getItem(0).setChecked(true);
        MenuItem firstMenuItem = navigationView.getMenu().getItem(0);
        selectMenuItem(firstMenuItem);

        navigationView.setNavigationItemSelectedListener(this);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(MainActivity.this, "Foto kiezen", 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File userPhoto = imageFiles.get(0);

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseHelper.pushProfilePhoto(userId, userPhoto);
                userImage.setImageBitmap(BitmapFactory.decodeFile(userPhoto.getPath()));
            }
        });
    }

    private String loadUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail = "";

        if (firebaseUser != null) {
            userEmail = firebaseUser.getEmail();
            String loggedInUserId = firebaseUser.getUid();

            try {
                FirebaseHelper.photoDelegate = this;
                FirebaseHelper.pullProfilePhoto(loggedInUserId);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return userEmail;
    }

    private void selectMenuItem(MenuItem item) {
        // TODO: Check if fragment is already running

        Fragment fragment = null;

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_groepen:
                fragment = new GroupsFragment();
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

    @Override
    public void returnPhoto(File photoFile) {
        if (photoFile != null) {
            Bitmap photoBitmap = BitmapFactory.decodeFile(photoFile.getPath());
            userImage.setImageBitmap(photoBitmap);
        }

    }
}
