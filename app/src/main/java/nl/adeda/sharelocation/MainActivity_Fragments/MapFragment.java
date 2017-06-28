package nl.adeda.sharelocation.MainActivity_Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import nl.adeda.sharelocation.Helpers.Interfaces.CallbackGroupUpdate;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.GPSHelper;
import nl.adeda.sharelocation.Helpers.OverviewListAdapter;
import nl.adeda.sharelocation.Helpers.PermissionChecker;
import nl.adeda.sharelocation.Helpers.Interfaces.PhotoInterface;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

public class MapFragment extends Fragment implements OnMapReadyCallback, CallbackGroupUpdate,
        PhotoInterface {

    // TEST LOCATIONS
    private static final LatLng AMS = new LatLng(52.355207, 4.95411);
    private static final LatLng RTD = new LatLng(51.9267368, 4.4866416);
    private static final LatLng ALK = new LatLng(52.6335989, 4.6841371);
    private static final LatLng PAR = new LatLng(48.8588377, 2.2775169);
    private static final LatLng TKY = new LatLng(35.6732619, 139.5703014);

    private LatLngBounds.Builder builder;

    private static int INTERVAL = 2500;

    private Context context;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ArrayList<User> listUsers;
    private ArrayList<User> userList = new ArrayList<>();
    private List<String> userIDs;
    private User currentUser;

    private GoogleMap googleMap;
    private Marker currentMarker;

    private ListView contactList;
    private String groupName;

    private OverviewListAdapter overviewListAdapter;

    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Create map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userList = getArguments().getParcelableArrayList("userList"); // Get information of users in group
        currentUser = getArguments().getParcelable("currentUser"); // Get information of current user
        groupName = getArguments().getString("groupName"); // Get current group name

        // TODO: Check if contact list contains contacts
        contactList = (ListView) view.findViewById(R.id.overview_contact_list);

        context = getActivity();
        /*
        if (contactList.getAdapter().getCount() == 0) {
            Display text
        }
         */

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(groupName);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // When map is loaded, put markers in

        PermissionChecker.locationPermissions(getActivity(), googleMap);

        // Get and push location coordinates to Firebase
        GPSHelper gpsHelper = new GPSHelper(getContext());
        Location location = gpsHelper.getLocation();
        gpsHelper.locationPusher(location);

        // Get map marker photo for current user
        FirebaseHelper.photoDelegate = this;
        FirebaseHelper.initializeMapMarkers(currentUser, false);
        //FirebaseHelper.initializeMapMarkers(currentUser, false, false, null, userList);

    }

    private void updateListView(ArrayList<User> users) {

    }

    @Override
    public void initializeCurrentUserMarker(User userData) {
        LatLng currentUserLocation = new LatLng(userData.getLatitude(), userData.getLongitude());

        builder = new LatLngBounds.Builder();
        builder.include(currentUserLocation);


        if (userData.getMapPhoto() != null) { // Set photo as icon if it's present
            Bitmap bitmap = userData.getMapPhoto();
            currentMarker = googleMap.addMarker(new MarkerOptions()
                    .position(currentUserLocation)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        } else { // Set no photo if it's not present
            currentMarker = googleMap.addMarker(new MarkerOptions().position(currentUserLocation)
                    .anchor(0.5f, 0.5f));
        }
        currentMarker.setTitle(userData.getVoornaam()); // TODO: TESTLINE - REMOVE AFTER USE

        // Initialize markers for other users
        ArrayList<User> initializedUsers = FirebaseHelper.getOtherUserMapMarkers(userList);
        initializeOtherUserMarkers(initializedUsers);
    }

    @Override
    public void initializeOtherUserMarkers(ArrayList<User> initializedUsers) {
        ArrayList<Marker> otherUserMarkers = new ArrayList<>();
        ArrayList<Bitmap> userPhotos = new ArrayList<>();
        
        for (User user : initializedUsers) {
            LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
            Marker otherUserMarker;

            if (user.getMapPhoto() != null) {
                otherUserMarker = googleMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromBitmap(user.getMapPhoto())));
            } else {
                otherUserMarker = googleMap.addMarker(new MarkerOptions().position(userLocation)
                        .anchor(0.5f, 0.5f));
            }

            otherUserMarkers.add(otherUserMarker);

            builder.include(userLocation);

            userPhotos.add(user.getMapPhoto());

            returnDistance(currentUser, user);
        }

        listUsers = initializedUsers;
        overviewListAdapter = new OverviewListAdapter(context, listUsers);
        contactList.setAdapter(overviewListAdapter);

        // Zoom to fit all markers
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));



        updateUserLocations(otherUserMarkers, initializedUsers);
    }

    private void updateUserLocations(final ArrayList<Marker> otherUserMarkers, final ArrayList<User> initializedUsers) {
        userIDs = new ArrayList<>();
        userIDs.add(currentUser.getUserId());

        for (User user : initializedUsers) {
            userIDs.add(user.getUserId());
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e("RUN", "MapFragment is running the timer!");
                FirebaseHelper.groupDelegate = MapFragment.this;
                FirebaseHelper.photoDelegate = MapFragment.this;
                if (otherUserMarkers.size() == initializedUsers.size()) {
                    FirebaseHelper.updateLocations(userIDs, otherUserMarkers, currentMarker);
                }
            }
        }, 0, INTERVAL);
    }

    @Override
    public void returnGroupUpdate(ArrayList<User> userData, User currentUserData, ArrayList<Marker> otherUserMarkers, Marker currentMarker) {
        Log.e("RUN", "MapFragment is returning group updates");
        if (userData.size() != otherUserMarkers.size() || currentUserData == null) {
            // Do nothing.
        } else {
            updateCurrentMapMarker(currentUserData, currentMarker);
            updateOtherMapMarkers(currentUserData, userData, otherUserMarkers);
        }
    }

    private void updateOtherMapMarkers(User currentUser, ArrayList<User> userData,
                                       ArrayList<Marker>
            otherUserMarkers) {
        int i = 0;
        for (User user : userData) {
            LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
            otherUserMarkers.get(i).setPosition(userLocation);
            returnDistance(currentUser, user);

            listUsers.get(i).setDistance(user.getDistance());

            i++;
        }

        overviewListAdapter.notifyDataSetChanged();
    }

    private void updateCurrentMapMarker(User currentUserData, Marker currentMarker) {
        LatLng userLocation = new LatLng(currentUserData.getLatitude(), currentUserData
                .getLongitude());

        currentMarker.setPosition(userLocation);
    }

    @Override
    public void returnPhoto(File photoFile) {
        // Not used here.
    }

    private void returnDistance(User currentUser, User user) {
        // Calculate distance between users
        float[] results = new float[1];
        Location.distanceBetween(currentUser.getLatitude(), currentUser.getLongitude(), user.getLatitude(), user.getLongitude(), results);

        String result = "";
        if (results[0] > 1000) {
            result = String.format(Locale.ENGLISH, "%.0f", results[0]/1000) + " km";
        } else {
            result = String.format(Locale.ENGLISH, "%.0f", results[0]) + " m";
        }
        user.setDistance(result);
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }
}
