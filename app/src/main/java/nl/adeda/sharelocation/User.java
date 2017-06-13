package nl.adeda.sharelocation;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.Circle;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Antonio on 8-6-2017.
 */
public class User {

    public String voornaam;
    public String achternaam;
    public Bitmap foto;
    public String latitude;
    public String longitude;
    public String tijdRefresh;

    // Constructor for contact list
    public User(String voornaam, String achternaam, Bitmap foto) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.foto = foto;
    }

    // Constructor for list with map
    public User(String voornaam, String achternaam, Bitmap foto, String latitude, String longitude, String tijdRefresh) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.foto = foto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tijdRefresh = tijdRefresh;
    }
}
