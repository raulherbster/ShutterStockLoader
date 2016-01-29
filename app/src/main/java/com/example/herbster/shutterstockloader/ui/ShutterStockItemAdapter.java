package com.example.herbster.shutterstockloader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.herbster.shutterstockloader.R;

import java.util.List;

public class ShutterStockItemAdapter extends ArrayAdapter<Bitmap> {

    Context context;
    LayoutInflater inflater;
    int layoutResourceId;
    float imageWidth;

    public ShutterStockItemAdapter(Context context, int layoutResourceId, List<Bitmap> items) {
        super(context, layoutResourceId, items);
        this.context = context;
        this.layoutResourceId = layoutResourceId;

        float width = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        float margin = (int)convertDpToPixel(10f, (Activity)context);
        // two images, three margins of 10dips
        imageWidth = ((width - (3 * margin)) / 2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout row = (FrameLayout) convertView;
        ItemHolder holder;
        Bitmap item = getItem(position);

        if (row == null) {
            holder = new ItemHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (FrameLayout) inflater.inflate(layoutResourceId, parent, false);
            ImageView itemImage = (ImageView)row.findViewById(R.id.item_image);
            holder.itemImage = itemImage;
        } else {
            holder = (ItemHolder) row.getTag();
        }

        row.setTag(holder);
        setImageBitmap(item, holder.itemImage);
        return row;
    }

    public static class ItemHolder
    {
        ImageView itemImage;
    }

    // resize the image proportionately so it fits the entire space
    private void setImageBitmap(Bitmap itemBitmap, ImageView imageView){
        float i = ((float) imageWidth) / ((float) itemBitmap.getWidth());
        float imageHeight = i * (itemBitmap.getHeight());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.height = (int) imageHeight;
        params.width = (int) imageWidth;
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(itemBitmap);
    }

    public float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return px;
    }

}