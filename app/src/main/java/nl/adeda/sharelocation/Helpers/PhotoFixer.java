package nl.adeda.sharelocation.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Helper class that takes a photo, crops and resizes it and converts it to a File that can be
 * put in Firebase Storage.
 */

public class PhotoFixer {

    public static Context context;

    public static File fixPhotoMapMarker(File photoFile, String loggedInUserId) {
        Bitmap photo = BitmapFactory.decodeFile(photoFile.getPath());
        Bitmap croppedPhoto;

        // Crop middle part of photo to a square shape
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

        // Convert to file
        File finalPhoto = convertToFile(roundedPhoto, loggedInUserId);

        return finalPhoto;
    }

    // Make photo circular
    public static Bitmap makeCircle(Bitmap bitmap) {
        RoundedBitmapDrawable roundedPhoto = RoundedBitmapDrawableFactory.create(Resources
                .getSystem(), bitmap);

        roundedPhoto.setCircular(true);

        return roundedPhoto.getBitmap();
    }

    private static File convertToFile(Bitmap roundedPhoto, String loggedInUserId) {
        File photoFile = new File(context.getCacheDir(),
                loggedInUserId + ".png");
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(photoFile);
            roundedPhoto.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoFile;
    }
}
