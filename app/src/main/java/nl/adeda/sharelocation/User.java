package nl.adeda.sharelocation;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

import com.google.android.gms.maps.model.Circle;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Antonio on 8-6-2017.
 */
public class User implements Parcelable {

    public String voornaam;
    public String achternaam;
    public String email;
    public double latitude;
    public double longitude;
    public String tijdRefresh;
    private String distance;
    private Bitmap photo;
    private Bitmap mapPhoto;

    public User() {}

    protected User(Parcel in) {
        voornaam = in.readString();
        achternaam = in.readString();
        email = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        tijdRefresh = in.readString();
        distance = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
        mapPhoto = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters
    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Bitmap getPhoto() {
        return photo;
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

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setMapPhoto(Bitmap mapPhoto) {
        this.mapPhoto = mapPhoto;
    }

    public Bitmap getMapPhoto() {
        return mapPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voornaam);
        dest.writeString(achternaam);
        dest.writeString(email);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(tijdRefresh);
        dest.writeString(distance);
        dest.writeParcelable(photo, flags);
    }

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

}
