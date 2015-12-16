package dbencrypto.dt.com.sqlcipherexample;


import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class SampleDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    public SampleDB(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ContentDescriptor.Table.TABLE_NAME + " ( "
                + ContentDescriptor.Table.Cols.ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContentDescriptor.Table.Cols.TAG_ADDRESS + " TEXT , "
                + ContentDescriptor.Table.Cols.TAG_MOBILE + " TEXT, "
                + ContentDescriptor.Table.Cols.TAG_NAME
                + " TEXT NOT NULL ," + "UNIQUE ("
                + ContentDescriptor.Table.Cols.TAG_NAME
                + ") ON CONFLICT REPLACE)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "
                    + ContentDescriptor.Table.TABLE_NAME);

            onCreate(db);
        }

    }
}
