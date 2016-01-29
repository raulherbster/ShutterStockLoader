package com.example.herbster.shutterstockloader.json;

import android.util.JsonReader;
import android.util.Log;

import com.example.herbster.shutterstockloader.model.ShutterStockCategory;
import com.example.herbster.shutterstockloader.model.ShutterStockContributor;
import com.example.herbster.shutterstockloader.model.ShutterStockImage;
import com.example.herbster.shutterstockloader.model.ShutterStockImageAsset;
import com.example.herbster.shutterstockloader.model.ShutterStockImageModel;
import com.example.herbster.shutterstockloader.model.ShutterStockQueryResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class defines a JSON parser for the format provided by StutterStock.
 *
 * To parser the successful query for images, the method parseShutterStockJSONResponse should
 * be used.
 *
 * Created by herbster on 1/27/2016.
 */
public class ShutterStockJSONParser {

    private static final String TAG = "JSONParser";
    public static final String TAG_PAGE = "page";
    public static final String TAG_PER_PAGE = "per_page";
    public static final String TAG_TOTAL_COUNT = "total_count";
    public static final String TAG_SEARCH_ID = "search_id";
    public static final String TAG_DATA = "data";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TAG_ID = "id";
    public static final String TAG_ADDED_DATE = "added_date";
    public static final String TAG_ASPECT = "aspect";
    public static final String TAG_ASSETS = "assets";
    public static final String TAG_CATEGORIES = "categories";
    public static final String TAG_CONTRIBUTOR = "contributor";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_IMAGE_TYPE = "image_type";
    public static final String TAG_MEDIA_TYPE = "media_type";
    public static final String TAG_KEYWORDS = "keywords";
    public static final String TAG_IS_ILLUSTRATION = "is_illustration";
    public static final String TAG_IS_ADULT = "is_adult";
    public static final String TAG_NAME = "name";
    public static final String TAG_DISPLAY_NAME = "display_name";
    public static final String TAG_DPI = "dpi";
    public static final String TAG_FILE_SIZE = "file_size";
    public static final String TAG_FORMAT = "format";
    public static final String TAG_HEIGHT = "height";
    public static final String TAG_IS_LICENSABLE = "is_licensable";
    public static final String TAG_WIDTH = "width";
    public static final String TAG_URL = "url";
    public static final String TAG_HAS_MODEL_RELEASE = "has_model_release";
    public static final String TAG_HAS_PROPERTY_RELEASE = "has_property_release";
    public static final String TAG_MODELS = "models";
    public static final String TAG_IS_EDITORIAL = "is_editorial";

    private static ShutterStockJSONParser singleton = null;

    /**
     * Private constructor.
     */
    private ShutterStockJSONParser() {

    }

    /**
     * Return the instance of this
     * @return the singleton instance of this JSON parser
     */
    public synchronized static ShutterStockJSONParser getInstance() {
        if (singleton == null)
            singleton = new ShutterStockJSONParser();
        return singleton;
    }

    /**
     * Parses the response (successful) into a meaningful ShutterStock object. In this case,
     * a ShutterStockQueryResponse with the content structured as objects.
     * @param in the input stream from the service response.
     * @return a ShutterStockQueryResponse object with the content structured as objects; null, if
     * the input stream is null or if there's a parser error.
     */
    public ShutterStockQueryResponse parseShutterStockJSONResponse(InputStream in) {
        if (in == null)
            return null;

        ShutterStockQueryResponse response = new ShutterStockQueryResponse();

        JsonReader jsonReader = new JsonReader(new InputStreamReader(in));

        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals(TAG_PAGE)) {
                    jsonReader.nextInt();
                } else if (name.equals(TAG_PER_PAGE)) {
                    jsonReader.nextInt();
                } else if (name.equals(TAG_TOTAL_COUNT)) {
                    response.setNumElements(jsonReader.nextInt());
                } else if (name.equals(TAG_SEARCH_ID)) {
                    response.setId(jsonReader.nextString());
                } else if (name.equals(TAG_DATA)) {
                    List<ShutterStockImage> images = parseShutterStockImages(jsonReader);
                    response.addImages(images);
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            Log.e(TAG, "Error while reading input stream : " + convertStreamToString(in));
            return null;
        }
        return response;
    }

    private List<ShutterStockImage> parseShutterStockImages(JsonReader jsonReader) throws IOException {
        List<ShutterStockImage> listResult = new ArrayList<ShutterStockImage>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            listResult.add(parseSingleShutterStockImage(jsonReader));
        }
        jsonReader.endArray();

        return listResult;
    }

    private ShutterStockImage parseSingleShutterStockImage(JsonReader jsonReader) throws IOException {
        ShutterStockImage image = new ShutterStockImage();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals(TAG_ID)) {
                image.setId(jsonReader.nextString());
            } else if (name.equals(TAG_ADDED_DATE)) {
                image.setAddedDate(dateFromString(jsonReader.nextString(), DATE_FORMAT));
            } else if (name.equals(TAG_ASPECT)) {
                image.setAspect(jsonReader.nextDouble());
            } else if (name.equals(TAG_ASSETS)) {
                image.addAssets(parseShutterStockImageAssets(jsonReader));
            } else if (name.equals(TAG_CATEGORIES)) {
                image.addCategories(parseShutterStockImageCategories(jsonReader));
            } else if (name.equals(TAG_CONTRIBUTOR)) {
                ShutterStockContributor contributor = parseShutterStockContributor(jsonReader);
                image.setContributor(contributor);
            } else if (name.equals(TAG_DESCRIPTION)) {
                image.setDescription(jsonReader.nextString());
            } else if (name.equals(TAG_IMAGE_TYPE)) {
                image.setImageType(jsonReader.nextString());
            } else if (name.equals(TAG_IS_ADULT)) {
                image.setIsAdult(jsonReader.nextBoolean());
            } else if (name.equals(TAG_HAS_MODEL_RELEASE)) {
                image.setHasModelRelease(jsonReader.nextBoolean());
            } else if (name.equals(TAG_HAS_PROPERTY_RELEASE)) {
                image.setHasPropertyRelease(jsonReader.nextBoolean());
            } else if (name.equals(TAG_IS_ILLUSTRATION)) {
                image.setIsIllustration(jsonReader.nextBoolean());
            } else if (name.equals(TAG_IS_EDITORIAL)) {
                image.setIsEditorial(jsonReader.nextBoolean());
            } else if (name.equals(TAG_KEYWORDS)) {
                List<String> keywords = parseShutterStockImageKeywords(jsonReader);
                image.setKeywords(keywords);
            } else if (name.equals(TAG_MEDIA_TYPE)) {
                jsonReader.nextString();
            } else if (name.equals(TAG_MODELS)) {
                List<ShutterStockImageModel> models = parseShutterStockImageModels(jsonReader);
                image.setModels(models);
            }
        }
        jsonReader.endObject();
        return image;
    }

    private List<ShutterStockImageModel> parseShutterStockImageModels(JsonReader jsonReader) throws IOException {
        List<ShutterStockImageModel> models = new ArrayList<ShutterStockImageModel>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            models.add(parseShutterStockSingleModel(jsonReader));
        }
        jsonReader.endArray();

        return models;
    }

    private ShutterStockImageModel parseShutterStockSingleModel(JsonReader jsonReader) throws IOException {
        ShutterStockImageModel model = new ShutterStockImageModel();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals(TAG_ID)) {
                model.setId(jsonReader.nextString());
            }
        }
        jsonReader.endObject();

        return model;
    }

    private List<ShutterStockCategory> parseShutterStockImageCategories(JsonReader jsonReader) throws IOException {
        List<ShutterStockCategory> categories = new ArrayList<ShutterStockCategory>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            categories.add(parseShutterStockSingleCategory(jsonReader));
        }
        jsonReader.endArray();

        return categories;
    }

    private ShutterStockCategory parseShutterStockSingleCategory(JsonReader jsonReader) throws IOException {
        ShutterStockCategory category = new ShutterStockCategory();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals(TAG_ID)) {
                category.setId(jsonReader.nextString());
            } else if (name.equals(TAG_NAME)) {
                category.setName(jsonReader.nextString());
            }
        }
        jsonReader.endObject();

        return category;
    }

    private List<ShutterStockImageAsset> parseShutterStockImageAssets(JsonReader jsonReader) throws IOException {
        List<ShutterStockImageAsset> assets = new ArrayList<ShutterStockImageAsset>();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            assets.add(parseShutterStockSingleAsset(jsonReader,name));
        }
        jsonReader.endObject();
        return assets;
    }

    private ShutterStockImageAsset parseShutterStockSingleAsset(JsonReader jsonReader, String nameAsset) throws IOException {
        ShutterStockImageAsset asset = new ShutterStockImageAsset(nameAsset);

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals(TAG_DISPLAY_NAME)) {
                asset.setDisplayName(jsonReader.nextString());
            } else if (name.equals(TAG_DPI)) {
                asset.setDPI(jsonReader.nextInt());
            } else if (name.equals(TAG_FILE_SIZE)) {
                asset.setFileSize(jsonReader.nextInt());
            } else if (name.equals(TAG_FORMAT)) {
                asset.setFormat(jsonReader.nextString());
            } else if (name.equals(TAG_HEIGHT)) {
                asset.setHeight(jsonReader.nextInt());
            } else if (name.equals(TAG_IS_LICENSABLE)) {
                asset.setIsLicensable(jsonReader.nextBoolean());
            } else if (name.equals(TAG_WIDTH)) {
                asset.setWidth(jsonReader.nextInt());
            } else if (name.equals(TAG_URL)) {
                asset.setURL(jsonReader.nextString());
            }
        }
        jsonReader.endObject();

        return asset;
    }

    private List<String> parseShutterStockImageKeywords(JsonReader jsonReader) throws IOException {
        List<String> keywords = new ArrayList<String>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            keywords.add(jsonReader.nextString());
        }
        jsonReader.endArray();
        return keywords;
    }

    private ShutterStockContributor parseShutterStockContributor(JsonReader jsonReader) throws IOException {
        ShutterStockContributor contributor = new ShutterStockContributor();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("id")) {
                contributor.setId(jsonReader.nextString());
            }
            // TODO maybe more attributes for contributor to come?
        }
        jsonReader.endObject();

        return contributor;
    }

    /**
     * Creates a Date from a string considering a certain format.
     * @param dateString the date as a string.
     * @param formatString the string containing the format to be used.
     * @return the date from the given string.
     */
    private Date dateFromString(String dateString, String formatString) {
        if (dateString == null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG,"Could not parse date");
            return null;
        }
    }

    /**
     * Utility method. Reads the content of a input stream and return it as a String.
     * @param in the input stream to be read.
     * @return the String with the content of the input stream.
     */
    private String convertStreamToString(InputStream in) {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG,"Cannot retrieve the content of the input stream.");
            return "";
        }
        return total.toString();
    }
}
