package conniezlabs.com.listviewapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "KitchenMaster-Search";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "Started onCreate");

//        setContentView(R.layout.activity_main);
        // The following is for Search Functionality
        handleIntent(getIntent());

        }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    // One of the methods for search functionality, this will handle the Search intent
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        	Log.e(TAG, "inside handleIntent");

        }
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
