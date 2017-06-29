package nl.adeda.sharelocation.MainActivity_Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.GPSHelper;
import nl.adeda.sharelocation.Helpers.Interfaces.CallbackGroupUpdate;
import nl.adeda.sharelocation.Helpers.Interfaces.PhotoInterface;
import nl.adeda.sharelocation.Helpers.OverviewListAdapter;
import nl.adeda.sharelocation.Helpers.PermissionChecker;
import nl.adeda.sharelocation.Helpers.PhotoFixer;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

public class MapFragment extends Fragment implements OnMapReadyCallback, CallbackGroupUpdate,
        PhotoInterface {

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

        contactList = (ListView) view.findViewById(R.id.overview_contact_list);

        context = getActivity();

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
    }

    // Callback method, called when request is made to load the current user marker photo,
    // whether successful or not. Sets the marker for the current user to the GoogleMap.
    @Override
    public void initializeCurrentUserMarker(User userData) {
        LatLng currentUserLocation = new LatLng(userData.getLatitude(), userData.getLongitude());

        builder = new LatLngBounds.Builder();
        builder.include(currentUserLocation);

        currentMarker = setMarker(userData, currentUserLocation);
        currentMarker.setTitle("Jij");

        // Initialize markers for other users
        ArrayList<User> initializedUsers = FirebaseHelper.getOtherUserMapMarkers(userList);
        initializeOtherUserMarkers(initializedUsers);
    }

    @Override
    public void initializeOtherUserMarkers(ArrayList<User> initializedUsers) {
        ArrayList<Marker> otherUserMarkers = new ArrayList<>();

        for (User user : initializedUsers) {
            LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
            Marker otherUserMarker;

            otherUserMarker = setMarker(user, userLocation);

            otherUserMarker.setTitle(user.getFirstName() + " " + user.getLastName());
            otherUserMarkers.add(otherUserMarker);

            builder.include(userLocation);
            returnDistance(currentUser, user);
        }

        setContents(initializedUsers, otherUserMarkers);
    }

    // Set contents for ListView and start location updates
    private void setContents(ArrayList<User> initializedUsers, ArrayList<Marker> otherUserMarkers) {
        // Set adapter for overview list below the map, containing the user photo (if present),
        // the users' name & the distance to the current logged in user.
        listUsers = initializedUsers;
        overviewListAdapter = new OverviewListAdapter(context, listUsers);
        contactList.setAdapter(overviewListAdapter);

        // Zoom to fit all markers
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // Initialize user location updates
        updateUserLocations(otherUserMarkers, initializedUsers);
    }

    // Set markers
    private Marker setMarker(User userData, LatLng currentUserLocation) {
        Marker marker;

        if (userData.getMapPhoto() != null) { // Set photo as icon if it's present
            Bitmap bitmap = PhotoFixer.makeCircle(userData.getMapPhoto());
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(currentUserLocation)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        } else { // Set default photo if it's not present
            marker = googleMap.addMarker(new MarkerOptions().position(currentUserLocation)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(getContext().getResources(),
                                    R.mipmap.anonymous_user), 100, 100, false))));
        }
        return marker;
    }

    // Updates all user locations in the specified group at a given interval
    private void updateUserLocations(final ArrayList<Marker> otherUserMarkers, final ArrayList<User> initializedUsers) {
        userIDs = new ArrayList<>();
        userIDs.add(currentUser.getUserId());

        // Put user IDs in list for searching in Firebase
        for (User user : initializedUsers) {
            userIDs.add(user.getUserId());
        }

        setUpdateTimer(otherUserMarkers, initializedUsers);
    }

    private void setUpdateTimer(final ArrayList<Marker> otherUserMarkers, final ArrayList<User> initializedUsers) {
        // Fetch user locations with a fixed interval, to prevent starting too many threads.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FirebaseHelper.groupDelegate = MapFragment.this;
                FirebaseHelper.photoDelegate = MapFragment.this;

                // Check if enough users are loaded, only then the locations of these users can
                // be updated.
                if (otherUserMarkers.size() == initializedUsers.size()) {
                    FirebaseHelper.updateLocations(userIDs, otherUserMarkers, currentMarker);
                }
            }
        }, 0, INTERVAL);
    }

    // Callback method, called when the updated coordinates from Firebase are received.
    @Override
    public void returnGroupUpdate(ArrayList<User> userData, User currentUserData, ArrayList<Marker> otherUserMarkers, Marker currentMarker) {
        if (userData.size() == otherUserMarkers.size() && currentUserData != null) {
            // Update markers
            updateCurrentMapMarker(currentUserData, currentMarker);
            updateOtherMapMarkers(currentUserData, userData, otherUserMarkers);
        }
    }

    // Updates marker location for the logged in user
    private void updateCurrentMapMarker(User currentUserData, Marker currentMarker) {
        LatLng userLocation = new LatLng(currentUserData.getLatitude(), currentUserData
                .getLongitude());

        currentMarker.setPosition(userLocation);
    }

    // Updates marker location for other users in group
    private void updateOtherMapMarkers(User currentUser, ArrayList<User> userData,
                                       ArrayList<Marker> otherUserMarkers) {
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

    @Override
    public void returnPhoto(File photoFile) {
        // Not used here.
    }

    // Puts distance between logged in user and other users in the other users' object class
    private void returnDistance(User currentUser, User user) {
        // Calculate distance between users
        float[] results = new float[1];
        Location.distanceBetween(currentUser.getLatitude(), currentUser.getLongitude(), user.getLatitude(), user.getLongitude(), results);

        // Format text
        String result = "";
        if (results[0] > 10000) {
            result = String.format(Locale.ENGLISH, "%.0f", results[0]/1000) + " km";
        } else if (results[0] > 1000 && results[0] < 10000)  {
            result = String.format(Locale.ENGLISH, "%.1f", results[0]/1000) + " km";
        } else {
            result = String.format(Locale.ENGLISH, "%.0f", results[0]) + " m";
        }
        user.setDistance(result);
    }


    // Stop timer to prevent useless calls to FirebaseHelper
    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }
}
