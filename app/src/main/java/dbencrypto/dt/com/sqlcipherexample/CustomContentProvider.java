package dbencrypto.dt.com.sqlcipherexample;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteQueryBuilder;

import javax.crypto.SecretKey;

/**
 * Sample secure content provider
 */

public class CustomContentProvider extends ContentProvider {


    /* Note:Do not hard code password here. This password should be entered by the
    user or sent from the server side.  */
    private static final String password = "qwerty1234";
    private static SampleDB mSampleDB;
    private static String mEncryptedPwd;

    @Override
    public boolean onCreate() {
        SQLiteDatabase.loadLibs(getContext());
        Context ctx = getContext();
        mSampleDB = new SampleDB(ctx);
        // the method that sets the encrypted password to encrypt the DB;
        setEncryptedPassword();
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mSampleDB.getReadableDatabase(password);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor resultSet;
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {
            case ContentDescriptor.Table.TABLE_PATH_TOKEN:
                queryBuilder.setTables(ContentDescriptor.Table.TABLE_NAME);
                sortOrder = ContentDescriptor.Table.Cols.TAG_NAME;
                resultSet = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                resultSet.setNotificationUri(getContext().getContentResolver(), uri);

                return resultSet;
        }
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {
            case ContentDescriptor.Table.TABLE_PATH_TOKEN:
                return ContentDescriptor.Table.CONTENT_TYPE_DIR;
            default:
                throw new UnsupportedOperationException("URI " + uri
                        + " is not supported.");
        }

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mSampleDB.getWritableDatabase(mEncryptedPwd);
        final int match = ContentDescriptor.URI_MATCHER.match(uri);

        switch (match) {
            case ContentDescriptor.Table.TABLE_PATH_TOKEN:
                long rowID = db.insert(ContentDescriptor.Table.TABLE_NAME, "", values);

                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(uri, rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return _uri;
                }

                throw new SQLiteException("Failed to add a record into " + uri);
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mSampleDB.getWritableDatabase(mEncryptedPwd);
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {

            case ContentDescriptor.Table.TABLE_PATH_TOKEN:
                count = db.delete(ContentDescriptor.Table.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mSampleDB.getWritableDatabase(mEncryptedPwd);
        final int match = ContentDescriptor.URI_MATCHER.match(uri);
        switch (match) {
            case ContentDescriptor.Table.TABLE_PATH_TOKEN:
                count = db.update(ContentDescriptor.Table.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    private void setEncryptedPassword() {
        // If salt is not present then generate the salt and store it in shared preferences.
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                "dt.com.securecodesqlcipher", Context.MODE_PRIVATE);
        String salt_value = sharedPref.getString("salt", "InvalidValue");
        byte[] salt;
        if (salt_value.equals("InvalidValue")) {
            salt = Crypto.generateSalt();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("salt", Crypto.toBase64(salt));
            editor.apply();
        } else {
            salt = Crypto.fromBase64(salt_value);
        }
        SecretKey key = Crypto.deriveKeyPbkdf2(salt, password);
        mEncryptedPwd = Crypto.toHex(key.getEncoded());
    }
}
