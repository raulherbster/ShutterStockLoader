package com.example.herbster.shutterstockloader;

import android.graphics.Bitmap;

/**
 * Listener interface used to notifify to the objects that a certain image has been properly loaded.
 * Created by herbster on 1/29/2016.
 */
public interface ShutterStockImageListener {

    /**
     * Received when a bitmap is loaded.
     * @param bitmap the loaded bitmap.
     */
    public void onBitmapLoaded(Bitmap bitmap);

}
