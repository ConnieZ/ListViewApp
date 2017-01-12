package conniezlabs.com.listviewapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseTable {

    private static final String TAG = "DatabaseTable";

    //The columns we'll include in the dictionary table
    public static final String COL_WINE = "WINE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_LONGDESCRIPTION = "LONG_DESCRIPTION";
    public static final String COL_SPECTRUMCOLOR = "SPECTRUM_COLOR";
    public static final String COL_WINECOLOR = "WINE_COLOR";

    private static final String DATABASE_NAME = "WINEDATABASE";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    // constructor for DatabaseTable class
    public DatabaseTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    // use this method to search for the items in the database
    public Cursor getItemMatches(String query, String[] columns) {
        String selection = COL_WINE + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    public Cursor getAllItems() {
        return query(null, null, new String[] {COL_WINE});
    }

    // this method is used by getItemMatches() and actually searches the database
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    // helper class
    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_WINE + ", " +
                        COL_DESCRIPTION + ", " +
                        COL_LONGDESCRIPTION + ", " +
                        COL_SPECTRUMCOLOR + ", " +
                        COL_WINECOLOR + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadList();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        private void loadList() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadItems();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadItems() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.winelist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "|");
                    if (strings.length < 2) continue;
                    long id = addItems(strings);
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
        }

        public long addItems(String[] strings) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_WINE, strings[0]);
            initialValues.put(COL_DESCRIPTION, strings[1]);
            initialValues.put(COL_LONGDESCRIPTION, strings[2]);
            initialValues.put(COL_SPECTRUMCOLOR, strings[3]);
            initialValues.put(COL_WINECOLOR, strings[4]);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }

    }
}
