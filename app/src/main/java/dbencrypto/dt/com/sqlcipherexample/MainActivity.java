package dbencrypto.dt.com.sqlcipherexample;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private static final int URL_LOADER = 0;
    private SimpleCursorAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        ListView listView = (ListView) findViewById(android.R.id.list);
        mAdapter =
                new SimpleCursorAdapter(
                        this,                // Current context
                        android.R.layout.simple_list_item_1,  // Layout for a single row
                        null,                // No Cursor yet
                        new String[]{ContentDescriptor.Table.Cols.TAG_NAME},        // Cursor columns to use
                        new int[]{android.R.id.text1},           // Layout fields to use
                        0                    // No flags
                );
        // Sets the adapter for the view
        listView.setAdapter(mAdapter);
    }

    /**
     * sample code to insert data
     */
    private void insertData(String name, String addr, String mobileNo) {
        ContentValues values = new ContentValues();
        values.put(ContentDescriptor.Table.Cols.TAG_NAME, name);
        values.put(ContentDescriptor.Table.Cols.TAG_MOBILE, addr);
        values.put(ContentDescriptor.Table.Cols.TAG_ADDRESS, mobileNo);

        Uri uri = getContentResolver().insert(ContentDescriptor.Table.TABLE_CONTENT_URI, values);
        Log.d(TAG, "uri is " + uri.toString());
    }

    /**
     * sample code to delete data
     */

    private void deleteData(String name) {
        getContentResolver().delete(ContentDescriptor.Table.TABLE_CONTENT_URI, ContentDescriptor.Table.Cols.TAG_NAME +
                " = ?", new String[]{name});
    }

    /**
     * sample code to delete data
     */
    private void queryData() {
        Cursor c = getContentResolver().query(ContentDescriptor.Table.TABLE_CONTENT_URI, null, null, null, null);
        Log.d(TAG, "count is " + c.getCount());
        c.close();
    }

    /**
     * sample code to update data
     */
    private void updateData() {
        // Intentionally left blank. Add code to update data here.

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                // start dialog
                showInsertView();
                break;
            case R.id.delete:
                Cursor c = (Cursor) mAdapter.getItem(0);
                if (c.moveToFirst()) {
                    deleteData(c.getString(c.getColumnIndex(ContentDescriptor.Table.Cols.TAG_NAME)));
                    Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    private void showInsertView() {

        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Insert Data");
        AlertDialog alertDialog = builder.create();
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(MainActivity.this);
        titleBox.setHint("Enter Name");
        layout.addView(titleBox);
        final EditText addrBox = new EditText(MainActivity.this);
        addrBox.setHint("Enter Address");
        layout.addView(addrBox);

        final EditText mobileNo = new EditText(MainActivity.this);
        mobileNo.setHint("Enter Phone No.");
        layout.addView(mobileNo);
        alertDialog.setView(layout);


        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //insert data
                String name = titleBox.getText().toString();
                String addr = addrBox.getText().toString();
                String mobNo = mobileNo.getText().toString();
                insertData(name, addr, mobNo);

            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //insert data

            }
        });
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case URL_LOADER:
                CursorLoader loader = new CursorLoader(
                        this,
                        ContentDescriptor.Table.TABLE_CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                return loader;
            default:
                // An invalid id was passed in
                return null;

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.changeCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);

    }


}
