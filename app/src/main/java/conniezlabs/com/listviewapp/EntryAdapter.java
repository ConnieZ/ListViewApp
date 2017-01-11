package conniezlabs.com.listviewapp;

// Grateful for this source: http://stackoverflow.com/questions/11678909/use-array-adapter-with-more-views-in-row-in-listview

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends BaseAdapter {
    private List list;
    LayoutInflater inflater;
    Context context;

    public EntryAdapter(Context context, ArrayList<Entry> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) convertView = inflater.inflate(R.layout.item_row, parent, false);
        // assign the view we are converting to a local variable
        View v = convertView;
        Entry item = (Entry) list.get(position);
        if (item != null) {
            TextView name = (TextView) v.findViewById(R.id.text_name);
            TextView description = (TextView) v.findViewById(R.id.text_descr);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (name != null){
                name.setText(item.getName());
            }
            if (description != null){
                description.setText(item.getDescription());
            }

        }

        // the view must be returned to our activity
        return v;
    }
}