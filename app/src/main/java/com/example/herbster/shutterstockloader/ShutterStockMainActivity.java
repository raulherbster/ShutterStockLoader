package com.example.herbster.shutterstockloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.herbster.shutterstockloader.model.ShutterStockImage;
import com.example.herbster.shutterstockloader.model.ShutterStockImageAsset;
import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;
import com.example.herbster.shutterstockloader.ui.DownloadImageWorker;
import com.example.herbster.shutterstockloader.ui.ShutterStockItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShutterStockMainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, ShutterStockQueryListener, ShutterStockImageListener {

    public static final String PREVIEW_ASSET = "preview";
    List<Integer> leftViewsHeights;
    List<Integer> rightViewsHeights;
    private ListView listViewLeft;
    private ListView listViewRight;
    private boolean mLoading;
    private int mNumImagesLoading;

    // Passing the touch event to the opposite list

    private class InnerPerformQueryTask extends PerformQueryTask {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ShutterStockMainActivity.this);
            this.dialog.setMessage("Retrieving images...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(ShutterStockQueryResponse shutterStockQueryResponse) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(shutterStockQueryResponse);
        }
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        boolean dispatched = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.equals(listViewLeft) && !dispatched) {
                dispatched = true;
                listViewRight.dispatchTouchEvent(event);
            } else if (v.equals(listViewRight) && !dispatched) {
                dispatched = true;
                listViewLeft.dispatchTouchEvent(event);
            }

            dispatched = false;
            return false;
        }
    };
    /**
     * Synchronizing scrolling
     * Distance from the top of the first visible element opposite list:
     * sum_heights(opposite invisible screens) - sum_heights(invisible screens) + distance from top of the first visible child
     */
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentTotalItemCount;
        private int mCurrentScrollState;

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            isScrollCompleted();
        }

        private void isScrollCompleted() {
            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE && !mLoading) {
                if(mCurrentFirstVisibleItem + mCurrentVisibleItemCount == mCurrentTotalItemCount && mCurrentTotalItemCount != 0) {
                    PerformQueryTask queryTask = new InnerPerformQueryTask();
                    queryTask.addQueryListener(ShutterStockMainActivity.this);
                    if (mCurrentPage > 0) {
                        mLoading = true;
                        queryTask.execute(new String[]{mCurrentQuery, Integer.toString(mCurrentPage)});
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if (view.getChildAt(0) != null) {
                int firstVisiblePosition = view.getFirstVisiblePosition();
                if (view.equals(listViewLeft) && (leftViewsHeights.size() > firstVisiblePosition)) {
                    leftViewsHeights.set(firstVisiblePosition, view.getChildAt(0).getHeight());

                    int h = 0;
                    for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
                        h += rightViewsHeights.get(i);
                    }

                    int hi = 0;
                    for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
                        hi += leftViewsHeights.get(i);
                    }

                    int top = h - hi + view.getChildAt(0).getTop();
                    listViewRight.setSelectionFromTop(listViewRight.getFirstVisiblePosition(), top);
                } else if (view.equals(listViewRight) && (rightViewsHeights.size() > firstVisiblePosition)) {
                    rightViewsHeights.set(firstVisiblePosition, view.getChildAt(0).getHeight());

                    int h = 0;
                    for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
                        h += leftViewsHeights.get(i);
                    }

                    int hi = 0;
                    for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
                        hi += rightViewsHeights.get(i);
                    }

                    int top = h - hi + view.getChildAt(0).getTop();
                    listViewLeft.setSelectionFromTop(listViewLeft.getFirstVisiblePosition(), top);
                }

                mCurrentFirstVisibleItem = firstVisibleItem;
                mCurrentVisibleItemCount = visibleItemCount;
                mCurrentTotalItemCount = totalItemCount;
            }

        }
    };
    private ShutterStockItemAdapter leftAdapter;
    private ShutterStockItemAdapter rightAdapter;
    private boolean onLeftSide = true;
    private ShutterStockAppProperties mProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shutter_stock_main);

        mLoading = false;

        mProperties = ShutterStockAppProperties.from(getApplicationContext());

        listViewLeft = (ListView) findViewById(R.id.list_view_left);
        listViewRight = (ListView) findViewById(R.id.list_view_right);

        preloadItems();

        listViewLeft.setOnTouchListener(touchListener);
        listViewRight.setOnTouchListener(touchListener);
        listViewLeft.setOnScrollListener(scrollListener);
        listViewRight.setOnScrollListener(scrollListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!isNetworkAvailable()) {
            popupNetworkErrorMessage();
            return false;
        } else {
            performQuery(query);
        }
        return false;
    }


    private String mCurrentQuery;
    private int mCurrentPage = 1;
    private int mTotalItemsDownloaded = 0;

    private void performQuery(String query) {
        leftAdapter.clear();
        rightAdapter.clear();
        PerformQueryTask queryTask = new InnerPerformQueryTask();
        queryTask.addQueryListener(this);
        mCurrentQuery = query;
        mLoading = true;
        mNumImagesLoading = 0;
        queryTask.execute( new String[] { mCurrentQuery,Integer.toString(mCurrentPage) } );
    }

    private void popupNetworkErrorMessage() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Connection Error")
                .setMessage("Your phone is not connected to the Internet. We cannot search for images.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onQueryFinished(ShutterStockQueryResponse response) {
        mTotalItemsDownloaded += response.getNumAddedElements();
        if (mTotalItemsDownloaded < response.getNumElements())
            mCurrentPage++;
        else
            mCurrentPage = 0;
        mNumImagesLoading = response.getNumAddedElements();
        Set<ShutterStockImage> images = response.getImages();
        for (ShutterStockImage image : images) {
            ShutterStockImageAsset asset = image.getAsset(PREVIEW_ASSET);
            DownloadImageWorker downloadImageWorker = new DownloadImageWorker();
            downloadImageWorker.addImageListener(this);
            downloadImageWorker.execute(asset);
        }
    }

    public void onBitmapLoaded(Bitmap bitmap) {
        if (onLeftSide) {
            leftAdapter.add(bitmap);
        } else {
            rightAdapter.add(bitmap);
        }
        onLeftSide = !onLeftSide;
        mNumImagesLoading--;
        if (mNumImagesLoading == 0)
            mLoading = false;
    }

    @Override
    public void onQueryError() {

    }

    private void preloadItems() {
        List<Bitmap> leftItems = new ArrayList<Bitmap>();
        List<Bitmap> rightItems = new ArrayList<Bitmap>();

        leftAdapter = new ShutterStockItemAdapter(this, R.layout.image_item, leftItems);
        rightAdapter = new ShutterStockItemAdapter(this, R.layout.image_item, rightItems);
        listViewLeft.setAdapter(leftAdapter);
        listViewRight.setAdapter(rightAdapter);

        leftViewsHeights = new ArrayList<Integer>();
        rightViewsHeights = new ArrayList<Integer>();
    }
}
