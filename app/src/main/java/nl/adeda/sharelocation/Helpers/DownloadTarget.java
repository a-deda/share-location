/*package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import nl.adeda.sharelocation.User;

/**
 * Created by Antonio on 28-6-2017.
 */
/*
public class DownloadTarget implements Target {

    private static Target downloadTarget;
    public static ArrayList<User> userList;
    public static PhotoInterface photoDelegate;
    private String url;
    private Context context;
    private int i;

    static void loadBitmap(String url, Context context, final int i) {
        if (downloadTarget == null) {
            downloadTarget = new DownloadTarget() {

                @Override
                public void onBitmapLoaded (Bitmap bitmap, Picasso.LoadedFrom from){
                    userList.get(i).setMapPhoto(bitmap);

                    if (i == userList.size()) {
                        photoDelegate.initializeOtherUserMarkers(userList);
                    }
                }

                @Override
                public void onBitmapFailed (Drawable errorDrawable){

                }



            };
            Picasso.with(context).load(url).into(downloadTarget);
        }

    }

}*/
