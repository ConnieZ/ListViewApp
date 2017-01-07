package conniezlabs.com.listviewapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        static final String[] WHITE_WINES = new String[] { "Pinot Blanc", "Pinot Grigio", "Sauvignon Blanc",
                "Chardonnay", "White Rioja", "Muscat Blanc", "Riesling","Tokaji" };
        ListView listView;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);
            //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_wine,FRUITS));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_wine,WHITE_WINES);

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

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            // this adds the ToolBar on top of the app with menu items from options_menu.xml
            inflater.inflate(R.menu.options_menu, menu);
            return true;
        }

}
