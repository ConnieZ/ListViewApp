package conniezlabs.com.listviewapp;
// this file used the following sources:
// http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
// https://developer.android.com/training/search/index.html

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchableActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "SearchableActivity";
    ListView listView;
    private static final String[] PROJECTION = new String[] { "_id", "text_column" };

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID = 1;

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    // The adapter that binds our data to the ListView
    private SimpleCursorAdapter mAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Started Searchable onCreate");

        // The following is for Search Functionality
        handleIntent(getIntent());

        }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    // create the database manager object
    DatabaseTable db = new DatabaseTable(this);

    // One of the methods for search functionality, this will handle the Search intent
    private void handleIntent(Intent intent) {
        Log.e(TAG, "inside Searchable handleIntent");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Cursor c = db.getItemMatches(query, null);

            //sanity check - did we find any items?
            if(c.getCount() > 0) {
                Toast.makeText(getApplicationContext(), query + " found " + c.getCount() + " items",
                        Toast.LENGTH_LONG).show();
            }

            //process Cursor and display results
            fillData(c);
        }
    }



    private void fillData(Cursor c) {
    	Log.e(TAG, "entered fillData");
        // Create an array to specify the fields we want to display in the list
        String[] from = new String[]{DatabaseTable.COL_WINE, DatabaseTable.COL_DESCRIPTION};
        Log.e(TAG, "after creating from");

        // and an array of the text fields we want to bind those db fields to
        int[] to = new int[]{R.id.text_name, R.id.text_descr};
        Log.e(TAG, "after creating to");

        // Initialize the adapter. Note that we pass a 'null' Cursor as the
        // third argument. We will pass the adapter a Cursor only when the
        // data has finished loading for the first time (i.e. when the
        // LoaderManager delivers the data to onLoadFinished). Also note
        // that we have passed the '0' flag as the last argument. This
        // prevents the adapter from registering a ContentObserver for the
        // Cursor (the CursorLoader will do this for us!).
        mAdapter = new SimpleCursorAdapter(this, R.layout.item_row,
                null, from, to, 0);

        Log.e(TAG, "after creating SimpleCursorAdapter");

        // Associate the (now empty) adapter with the ListView.
        setListAdapter(mAdapter);

        // The Activity (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        mCallbacks = this;

        // Initialize the Loader with id '1' and callbacks 'mCallbacks'.
        // If the loader doesn't already exist, one is created. Otherwise,
        // the already created Loader is reused. In either case, the
        // LoaderManager will manage the Loader across the Activity/Fragment
        // lifecycle, will receive any new loads once they have completed,
        // and will report this new data back to the 'mCallbacks' object.
        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);

//        listView = (ListView) findViewById(R.id.listView);
//        Log.e(TAG, "after findViewById");
//
//        listView.setAdapter(items);
//        Log.e(TAG, "after setAdapter");


        Log.e(TAG, "finished fillData");
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Create a new CursorLoader with the following query parameters.
        return new android.content.CursorLoader(SearchableActivity.this, WineListContentProvider.CONTENT_URI,
                PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(cursor);
                break;
        }
        // The listview now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // R.id.search is pulled by id from options_menu.xml, where there could be other menu items.
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        // Adding this line causes the app to stop, needs more work (add real Activity)
        // The SearchView attempts to start an activity with the ACTION_SEARCH when a user submits a search query.
        // A searchable activity filters for the ACTION_SEARCH intent and searches for the query in some sort of data set.
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

}
