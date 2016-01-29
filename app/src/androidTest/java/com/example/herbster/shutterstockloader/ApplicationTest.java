package com.example.herbster.shutterstockloader;

import android.test.InstrumentationTestCase;

import com.example.herbster.shutterstockloader.json.ShutterStockJSONParser;
import com.example.herbster.shutterstockloader.model.ShutterStockImage;
import com.example.herbster.shutterstockloader.model.ShutterStockMedia;
import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {

    private final String TEST_FILE_NAME_FIRST = "image_query_first.json";
    private final String TEST_FILE_NAME_SECOND = "image_query_second.json";
    private final String TEST_FILE_NAME_ERROR = "image_query_error.json";

    public ApplicationTest() {

    }

    public void testShutterStockJSONParser_Error() {
        ShutterStockJSONParser parser = ShutterStockJSONParser.getInstance();
        ShutterStockQueryResponse queryResponse = null;
        try {
            queryResponse = parser.parseShutterStockJSONResponse(getInstrumentation().getContext().getAssets().open(TEST_FILE_NAME_ERROR));
        } catch (IOException e) {
            fail("Should not  here");
        }

        assertNotNull(queryResponse);
    }

    public void testShutterStockJSONParser_QueryResponse() {
        ShutterStockJSONParser parser = ShutterStockJSONParser.getInstance();
        ShutterStockQueryResponse queryResponse = null;
        try {
            queryResponse = parser.parseShutterStockJSONResponse(getInstrumentation().getContext().getAssets().open(TEST_FILE_NAME_FIRST));
        } catch (IOException e) {
            fail("Should not  here");
        }

        assertNotNull(queryResponse);
        assertEquals(2, queryResponse.getNumAddedElements());
        assertEquals(72092, queryResponse.getNumElements());
        assertEquals("ln-eKt9vPDYCRVqiPkdloA", queryResponse.getId());

        ShutterStockQueryResponse queryResponseAnother = null;
        try {
            queryResponseAnother = parser.parseShutterStockJSONResponse(getInstrumentation().getContext().getAssets().open(TEST_FILE_NAME_SECOND));
        } catch (IOException e) {
            fail("Should not  here");
        }

        assertNotNull(queryResponseAnother);
        assertEquals(2, queryResponseAnother.getNumAddedElements());
        assertEquals(72092, queryResponseAnother.getNumElements());
        assertEquals("ln-eKt9vPDYCRVqiPkdloA", queryResponseAnother.getId());

        assertTrue(queryResponse.merge(queryResponseAnother));
        assertNotNull(queryResponse);
        assertNotNull(queryResponseAnother);
        assertEquals(4, queryResponse.getNumAddedElements());
        assertEquals(72092, queryResponse.getNumElements());
        assertEquals("ln-eKt9vPDYCRVqiPkdloA", queryResponse.getId());
    }

    public void testShutterStockJSONParser_Image() {
        ShutterStockJSONParser parser = ShutterStockJSONParser.getInstance();
        ShutterStockQueryResponse queryResponse = null;
        try {
            queryResponse = parser.parseShutterStockJSONResponse(getInstrumentation().getContext().getAssets().open(TEST_FILE_NAME_FIRST));
        } catch (IOException e) {
            fail("Should not  here");
        }

        Set<ShutterStockImage> images = queryResponse.getImages();
        assertEquals(2, images.size());

        Iterator<ShutterStockImage> itImages = images.iterator();
        while(itImages.hasNext()) {
            ShutterStockImage image = itImages.next();

            if (image.getId().equals("241570090")) {
                assertEquals(ShutterStockMedia.MediaType.IMAGE, image.getMediaType());
                assertEquals(1.3333, image.getAspect());
                assertEquals(image.getDescription(),"Drum, Snare ,logo, symbol, icon,graphic,vector.");
                assertTrue(image.isIllustrationImage());
                assertFalse(image.isAdultImage());
                String keywords[] = {"abstract","band","brand","branding","business","company","concert","creative","design","drum","element","emblem","graphic","icon","jazz","logo","march","modern","music","pop","rhythm","school","shadow","shape","shop","sign","silhouette","simple","smart","snare","song","sound","stick","studio","symbol","teaching","training","unusual","vector"};
                Set<String> keywordsSet = new HashSet<String>(Arrays.asList(keywords));
                assertTrue(image.getKeywords().containsAll(keywordsSet));
                assertEquals("2364116",image.getContributor().getId());
            }
        }
    }
}