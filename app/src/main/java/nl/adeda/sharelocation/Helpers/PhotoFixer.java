package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import nl.adeda.sharelocation.Activities.MainActivity;


/**
 * Created by Antonio on 8-6-2017.
 */

public class PhotoFixer {

    public static Context context;

    public static File fixPhotoMapMarker(File photoFile, String loggedInUserId) {
        Bitmap photo = BitmapFactory.decodeFile(photoFile.getPath());
        Bitmap croppedPhoto;

        if (photo.getWidth() >= photo.getHeight()) {
            croppedPhoto = Bitmap.createBitmap(photo, photo.getWidth() / 2 - photo.getHeight() / 2,
                    0, photo.getHeight(), photo.getHeight());
        } else {
            croppedPhoto = Bitmap.createBitmap(photo, 0, photo.getHeight() / 2 - photo.getWidth()
                    / 2,
                    photo.getWidth(), photo.getWidth());
        }

        Bitmap resizedPhoto = Bitmap.createScaledBitmap(croppedPhoto, 150, 150, false);

        Bitmap roundedPhoto = makeCircle(resizedPhoto);

        File finalPhoto = new File(context.getCacheDir(),
                loggedInUserId + ".png");
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(finalPhoto);
            roundedPhoto.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalPhoto;
    }


    public static Bitmap makeCircle(Bitmap bitmap) {
        RoundedBitmapDrawable roundedPhoto = RoundedBitmapDrawableFactory.create(Resources
                .getSystem(), bitmap);

        roundedPhoto.setCircular(true);

        return roundedPhoto.getBitmap();
    }
}
