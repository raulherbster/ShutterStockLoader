package com.example.herbster.shutterstockloader.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.herbster.shutterstockloader.R;

import java.util.List;

public class ShutterStockItemAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private LayoutInflater inflater;
    private int layoutResourceId;
    private float imageWidth;

    public ShutterStockItemAdapter(Context context, int layoutResourceId, List<Bitmap> items) {
        super(context, layoutResourceId, items);
        this.context = context;
        this.layoutResourceId = layoutResourceId;

        float width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        float margin = (int) convertDpToPixel(10f, (Activity) context);
        // two images, three margins of 10dips
        imageWidth = ((width - (3 * margin)) / 2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FrameLayout row = (FrameLayout) convertView;
        ItemHolder holder;
        final Bitmap item = getItem(position);

        if (row == null) {
            holder = new ItemHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (FrameLayout) inflater.inflate(layoutResourceId, parent, false);
            ImageView itemImage = (ImageView) row.findViewById(R.id.item_image);
            holder.itemImage = itemImage;
        } else {
            holder = (ItemHolder) row.getTag();
        }

        row.setTag(holder);
        setImageBitmap(item, holder.itemImage, 1);

        // setting the listener to show a larger image when the initial one is pressed.
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog alertDialog = new Dialog(context);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.image_view_layout);

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView detailView = (ImageView) alertDialog.findViewById(R.id.details_image);
                setImageBitmap(item, detailView, (float) 1.5);
                alertDialog.show();

            }
        });

        return row;
    }

    /**
     * Resizes the image proportionately so it fits the entire space
     */
    private void setImageBitmap(Bitmap itemBitmap, ImageView imageView, float increase) {
        if (itemBitmap == null)
            return;
        float i = ((float) imageWidth) / ((float) itemBitmap.getWidth());
        float imageHeight = i * (itemBitmap.getHeight());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.height = (int) (imageHeight * increase);
        params.width = (int) (imageWidth * increase);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(itemBitmap);
    }

    /**
     * Converts dp into pixel values.
     * @param dp
     * @param context
     * @return
     */
    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    class ItemHolder {
        ImageView itemImage;
    }

}