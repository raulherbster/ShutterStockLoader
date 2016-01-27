package com.example.herbster.shutterstockloader;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;

import com.example.herbster.shutterstockloader.json.ShutterStockJSONParser;
import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {

    private final String TEST_FILE_NAME = "image_query.json";

    public ApplicationTest() {

    }

    public void testShutterStockJSONParser_QueryResponse() {
        ShutterStockJSONParser parser = ShutterStockJSONParser.getInstance();
        ShutterStockQueryResponse queryResponse = null;
        try {
            queryResponse = parser.parseShutterStockJSONResponse(getInstrumentation().getContext().getAssets().open(TEST_FILE_NAME));
            assertNotNull(queryResponse);
            assertEquals(2,queryResponse.getNumAddedElements());
        } catch (IOException e) {
            fail("Should not  here");
        }

    }
}