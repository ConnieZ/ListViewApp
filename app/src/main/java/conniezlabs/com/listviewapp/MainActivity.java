package conniezlabs.com.listviewapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        private static final String TAG = "MainActivity";

        static final String[] WHITE_WINES = new String[] { "Pinot Blanc", "Pinot Grigio", "Sauvignon Blanc",
                    "Chardonnay", "White Rioja", "Muscat Blanc", "Riesling","Tokaji" };

        // create the database manager object
        DatabaseTable db = new DatabaseTable(this);

        ListView listView;


        private static final int INSERT_ID = Menu.FIRST;
        private static final int SHOP_LIST_ID = Menu.NONE;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.e(TAG, "entered onCreate");

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            //showing all wines from db
            ArrayAdapter<String> adapter;

            Cursor c = db.getAllItems();
            ArrayList<String> items = new ArrayList<String>();
            try {
                if (c.moveToFirst()) {
                    do {
                        items.add(c.getString(c.getColumnIndexOrThrow(DatabaseTable.COL_WINE)));
                    } while (c.moveToNext());
                }
                c.close();
                adapter = new ArrayAdapter<String>(this, R.layout.list_wine,items);
            } catch( RuntimeException e){

            }finally {
                adapter = new ArrayAdapter<String>(this, R.layout.list_wine,WHITE_WINES);
            }


            // ListView listView = getListView();

            // the following two lines are replacements for commented out code
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);


            listView.setTextFilterEnabled(true);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getApplicationContext(),
                            ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }
            });
            Log.e(TAG, "onCreate success");

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            Log.e(TAG, "entered onCreateOptionsMenu");
            MenuInflater inflater = getMenuInflater();
            // this adds the ToolBar on top of the app with menu items from options_menu.xml
            inflater.inflate(R.menu.options_menu, menu);
            menu.add(0, INSERT_ID, 0, R.string.menu_insert);
            //new menu option to pull up the Shopping List
            menu.add(0, SHOP_LIST_ID, 0, R.string.shop_list);
            Log.e(TAG, "finished onCreateOptionsMenu");


            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getTitle().equals("Search")) {
                Toast.makeText(getApplicationContext(), "Search = "+onSearchRequested(), Toast.LENGTH_LONG).show();
                return onSearchRequested();
            } else {
                return false;
            }
        }

}
