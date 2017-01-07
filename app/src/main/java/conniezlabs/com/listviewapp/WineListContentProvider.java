package conniezlabs.com.listviewapp;
// this file uses the following sources:
// https://code.tutsplus.com/tutorials/android-fundamentals-working-with-content-providers--mobile-5549

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class WineListContentProvider extends ContentProvider {
    private DatabaseTable mDB;

    private static final String AUTHORITY = "conniezlabs.com.listviewapp.WineListContentProvider";
    public static final int ITEMS = 100;
    public static final int ITEM_ID = 110;

    private static final String BASE_PATH = "winelist";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/wine";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/wine";

    @Override
    public boolean onCreate() {
        mDB = new DatabaseTable(getContext());
        return true;
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }

    @Override
    public String getType(Uri uri) { return null; }

    @Override
    public Uri insert(Uri uri, ContentValues values) { return null; }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) { return null;  }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
}
