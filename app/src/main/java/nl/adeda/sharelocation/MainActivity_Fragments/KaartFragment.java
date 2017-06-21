package nl.adeda.sharelocation.MainActivity_Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import nl.adeda.sharelocation.Helpers.CallbackGroupUpdate;
import nl.adeda.sharelocation.Helpers.FirebaseHelper;
import nl.adeda.sharelocation.Helpers.GPSHelper;
import nl.adeda.sharelocation.R;
import nl.adeda.sharelocation.User;

public class KaartFragment extends Fragment implements OnMapReadyCallback, CallbackGroupUpdate {

    // TEST LOCATIONS
    private static final LatLng AMS = new LatLng(52.355207, 4.95411);
    private static final LatLng RTD = new LatLng(51.9267368, 4.4866416);
    private static final LatLng ALK = new LatLng(52.6335989, 4.6841371);
    private static final LatLng PAR = new LatLng(48.8588377, 2.2775169);
    private static final LatLng TKY = new LatLng(35.6732619, 139.5703014);
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ArrayList<User> userList;
    private List<String> userIDs;

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kaart, container, false);

        // Create map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.kaart);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userList = (ArrayList<User>) getArguments().getSerializable("userList"); // Get information of users in group
        userIDs = (List<String>) getArguments().getSerializable("userIDs");

        // TODO: Check if user has any open requests

        // TODO: Check if contact list contains contacts
        ListView contactList = (ListView) view.findViewById(R.id.locaties_contacten_lijst);
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
        getActivity().setTitle("Kaart");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // When map is loaded, put markers in
        /*
        // TEST MARKERS
        Marker mAMS = googleMap.addMarker(new MarkerOptions().position(AMS).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_1)));
        Marker mRTD = googleMap.addMarker(new MarkerOptions().position(RTD).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_2)));
        Marker mALK = googleMap.addMarker(new MarkerOptions().position(ALK).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_3)));
        Marker mPAR = googleMap.addMarker(new MarkerOptions().position(PAR).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_4)));
        Marker mTKY = googleMap.addMarker(new MarkerOptions().position(TKY).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_5)));
        */

        for (User user : userList) {
            LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(userLocation).anchor(0.5f, 0.5f));
            marker.setTitle(user.getVoornaam() + " " + user.getAchternaam());
        }

        // Check if location request permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Ask permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        // Get and push location coordinates to Firebase
        GPSHelper gpsHelper = new GPSHelper(getContext(), firebaseUser);
        Location location = gpsHelper.getLocation();
        gpsHelper.locationPusher(location);

        // TODO: Change 2nd parameter to time specified by user.

        FirebaseHelper.groupDelegate = this;
        FirebaseHelper.updateLocations(userIDs);
    }

    @Override
    public void returnGroupUpdate(ArrayList<User> userData) {
        googleMap.clear();
        for (User user : userData) {
            LatLng userLocation = new LatLng(user.getLatitude(), user.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(userLocation).anchor(0.5f, 0.5f));
            marker.setTitle(user.getVoornaam() + " " + user.getAchternaam());
        }
    }
}
