package dbencrypto.dt.com.sqlcipherexample;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ranjeet on 10/03/15.
 */
public class ContentDescriptor {

    // utility variables
    public static final String AUTHORITY = "dbencrypto.dt.com.securecodesqlcipher.app";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    private ContentDescriptor() {
    }

    ;

    // register identifying URIs for AppsCategory entity
    // the TOKEN value is associated with each URI registered
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;
        matcher.addURI(authority, Table.TABLE_PATH,
                Table.TABLE_PATH_TOKEN);
        return matcher;
    }

    // Define a static class that represents description of stored content
    // entity.
    // Here we define AppsCategory
    public static class Table {
        // an identifying name for entity
        public static final String TABLE_NAME = "sampletable";

        public static final String TABLE_PATH = "sampletable";


        public static final Uri TABLE_CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(TABLE_PATH).build();

        public static final int TABLE_PATH_TOKEN = 100;

        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.sampletable.app";


        // a static class to store columns in entity
        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            public static final String TAG_NAME = "name";
            public static final String TAG_ADDRESS = "address";
            public static final String TAG_MOBILE = "mobile_no";


        }
    }

}
