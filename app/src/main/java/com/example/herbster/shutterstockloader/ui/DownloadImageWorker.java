package com.example.herbster.shutterstockloader.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.herbster.shutterstockloader.ShutterStockImageListener;
import com.example.herbster.shutterstockloader.model.ShutterStockImageAsset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by herbster on 1/28/2016.
 */
public class DownloadImageWorker extends AsyncTask<ShutterStockImageAsset, Void, Bitmap> {

    private final String TAG = "DownloadImageWorker";

    private List<ShutterStockImageListener> mImageListenersList;
    private URLConnection mCurrentConnection;

    public DownloadImageWorker() {
        mImageListenersList = new ArrayList<ShutterStockImageListener>();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public boolean addImageListener(ShutterStockImageListener l) {
        if (!mImageListenersList.contains(l))
            return mImageListenersList.add(l);
        return false;
    }

    public boolean removeImageListener(ShutterStockImageListener l) {
        return mImageListenersList.remove(l);
    }

    public Bitmap decodeSampledBitmapFromStream(InputStream in_a, InputStream in_b, int reqWidth, int reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in_a, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in_b, null, options);
    }

    @Override
    protected Bitmap doInBackground(ShutterStockImageAsset... params) {
        ShutterStockImageAsset imageAsset = params[0];
        InputStream is = fetchStreamFromURL(imageAsset.getURL());
        if (is == null)
            return null;

        ByteArrayOutputStream byteArrayStream = null;
        InputStream in_a = null;
        InputStream in_b = null;

        Bitmap bitmap = null;
        try {
            byteArrayStream = copyInputStream(is);
            in_a = new ByteArrayInputStream(byteArrayStream.toByteArray());
            in_b = new ByteArrayInputStream(byteArrayStream.toByteArray());
            bitmap = decodeSampledBitmapFromStream(in_a, in_b, imageAsset.getWidth(), imageAsset.getHeight());
        } catch (IOException e) {
            Log.e(TAG, "Could not load bitmap");
            bitmap = null;
        }

        try {
            is.close();
            if (in_a != null)
                in_a.close();
            if (in_b != null)
                in_b.close();

            byteArrayStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error trying to close the streams : " + e.getMessage().toString());
        }

        return bitmap;
    }

    private InputStream fetchStreamFromURL(String url) {
        try {
            URL imageURL = new URL(url);
            mCurrentConnection = imageURL.openConnection();
            mCurrentConnection.setUseCaches(true);
            mCurrentConnection.connect();
            return mCurrentConnection.getInputStream();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not load the image URL : " + url);
            return null;
        }
    }

    private ByteArrayOutputStream copyInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();

        return baos;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        for (ShutterStockImageListener listener : mImageListenersList)
            listener.onBitmapLoaded(bitmap);
    }

}
