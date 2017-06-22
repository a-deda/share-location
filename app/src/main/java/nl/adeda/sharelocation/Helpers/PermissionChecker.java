package nl.adeda.sharelocation.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Antonio on 22-6-2017.
 */

public class PermissionChecker {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public static void locationPermissions(Activity activity, GoogleMap googleMap) {
        // Check if location request permission is granted
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Ask permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Set location on Google Map
            googleMap.setMyLocationEnabled(true);
        }
    }
}
