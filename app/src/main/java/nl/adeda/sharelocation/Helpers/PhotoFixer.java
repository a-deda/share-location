package nl.adeda.sharelocation.Helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Antonio on 8-6-2017.
 */

public class PhotoFixer {

    public Bitmap fixPhoto(Resources res, int fileId) {
        Bitmap photo = BitmapFactory.decodeResource(res, fileId);

        // TODO: Crop to square aspect ratio

        // TODO: Resize to smaller size

        return photo;
    }

}
