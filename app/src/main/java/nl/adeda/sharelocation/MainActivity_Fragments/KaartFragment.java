package nl.adeda.sharelocation.MainActivity_Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import nl.adeda.sharelocation.R;

public class KaartFragment extends Fragment implements OnMapReadyCallback {

    // TEST LOCATIONS
    private static final LatLng AMS = new LatLng(52.355207, 4.95411);
    private static final LatLng RTD = new LatLng(51.9267368, 4.4866416);
    private static final LatLng ALK = new LatLng(52.6335989, 4.6841371);
    private static final LatLng PAR = new LatLng(48.8588377, 2.2775169);
    private static final LatLng TKY = new LatLng(35.6732619, 139.5703014);
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kaart, container, false);

        // Create map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.kaart);
        mapFragment.getMapAsync(this);

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
        // When map is loaded, put markers in

        Marker mAMS = googleMap.addMarker(new MarkerOptions().position(AMS).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_1)));
        Marker mRTD = googleMap.addMarker(new MarkerOptions().position(RTD).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_2)));
        Marker mALK = googleMap.addMarker(new MarkerOptions().position(ALK).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_3)));
        Marker mPAR = googleMap.addMarker(new MarkerOptions().position(PAR).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_4)));
        Marker mTKY = googleMap.addMarker(new MarkerOptions().position(TKY).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile_5)));

        // Check if location request permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Ask permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        googleMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

    }
}
