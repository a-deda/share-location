package nl.adeda.sharelocation;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.Circle;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Antonio on 8-6-2017.
 */
public class User implements Serializable {

    public String voornaam;
    public String achternaam;
    public String email;
    public Bitmap foto;
    public double latitude;
    public double longitude;
    public String tijdRefresh;
    private String distance;

    /*
    // Constructors
    public User(@NonNull String voornaam, @NonNull String achternaam) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
    }

    public User(@NonNull String voornaam, @NonNull String achternaam, Bitmap foto) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.foto = foto;
    }

    // Constructor for list with map
    public User(String voornaam, String achternaam, Bitmap foto, double latitude, double longitude, String tijdRefresh) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.foto = foto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tijdRefresh = tijdRefresh;
    }
    
    public User(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    */


    // Getters
    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTijdRefresh() {
        return tijdRefresh;
    }

    // Setters

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTijdRefresh(String tijdRefresh) {
        this.tijdRefresh = tijdRefresh;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) { this.distance = distance; }
}
