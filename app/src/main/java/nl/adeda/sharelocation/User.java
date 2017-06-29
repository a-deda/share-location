package nl.adeda.sharelocation;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * User class object containing all information that calling classes need from a user.
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
    private String userId;

    public User() {
    }

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
    public String getFirstName() {
        return voornaam;
    }

    public String getLastName() {
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

    public Bitmap getMapPhoto() {
        return mapPhoto;
    }

    public String getDistance() {
        return distance;
    }

    public String getUserId() {
        return userId;
    }


    // Setters
    public void setFirstName(String voornaam) {
        this.voornaam = voornaam;
    }

    public void setLastName(String achternaam) {
        this.achternaam = achternaam;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setMapPhoto(Bitmap mapPhoto) {
        this.mapPhoto = mapPhoto;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}