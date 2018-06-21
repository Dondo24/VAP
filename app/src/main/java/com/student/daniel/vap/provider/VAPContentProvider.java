package com.student.daniel.vap.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.student.daniel.vap.util.Defaults;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentResolver.SCHEME_CONTENT;
import static com.student.daniel.vap.util.Defaults.ALL_COLUMNS;
import static com.student.daniel.vap.util.Defaults.AUTOGENERATED_ID;
import static com.student.daniel.vap.util.Defaults.NO_CONTENT_OBSERVER;
import static com.student.daniel.vap.util.Defaults.NO_GROUP_BY;
import static com.student.daniel.vap.util.Defaults.NO_HAVING;
import static com.student.daniel.vap.util.Defaults.NO_NULL_COLUMN_HACK;
import static com.student.daniel.vap.util.Defaults.NO_SELECTION;
import static com.student.daniel.vap.util.Defaults.NO_SELECTION_ARGS;
import static com.student.daniel.vap.util.Defaults.NO_SORT_ORDER;
import static com.student.daniel.vap.util.Defaults.NO_TYPE;


public class VAPContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.student.daniel.vap.provider.VAPContentProvider";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Provider.VAP.TABLE_NAME)
            .build();

    private static final int URI_MATCH_VAP = 0;
    private static final int URI_MATCH_VAP_BY_ID = 1;

    private static final String MIME_TYPE_NOTES = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + Provider.VAP.TABLE_NAME;
    private static final String MIME_TYPE_SINGLE_NOTE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + Provider.VAP.TABLE_NAME;

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DatabaseOpenHelper databaseHelper;

    @Override
    public boolean onCreate() {
        uriMatcher.addURI(AUTHORITY, Provider.VAP.TABLE_NAME, URI_MATCH_VAP);
        uriMatcher.addURI(AUTHORITY, Provider.VAP.TABLE_NAME + "/#", URI_MATCH_VAP_BY_ID);

        this.databaseHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_VAP:
                cursor = listVAP();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case URI_MATCH_VAP_BY_ID:
                long id = ContentUris.parseId(uri);
                cursor = findById(id);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                return Defaults.NO_CURSOR;
        }
    }

    private Cursor findById(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = Provider.VAP._ID + "=" + id;
        return db.query(Provider.VAP.TABLE_NAME, ALL_COLUMNS, selection, NO_SELECTION_ARGS, NO_GROUP_BY, NO_HAVING, NO_SORT_ORDER);
    }

    private Cursor listVAP() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        return db.query(Provider.VAP.TABLE_NAME, ALL_COLUMNS, NO_SELECTION, NO_SELECTION_ARGS, NO_GROUP_BY, NO_HAVING, "_ID DESC");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_VAP:
                Uri newItemUri = saveVAP(values);
                getContext().getContentResolver().notifyChange(CONTENT_URI, NO_CONTENT_OBSERVER);
                return newItemUri;
            default:
                return Defaults.NO_URI;
        }
    }

    private Uri saveVAP(ContentValues values) {
        ContentValues VAP = new ContentValues();
        VAP.put(Provider.VAP._ID, AUTOGENERATED_ID);
        VAP.put(Provider.VAP.TYPE,values.getAsInteger(Provider.VAP.TYPE));
        VAP.put(Provider.VAP.AMOUNT,values.getAsDouble(Provider.VAP.AMOUNT));
        VAP.put(Provider.VAP.DESCRIPTION, values.getAsString(Provider.VAP.DESCRIPTION));
        Date d = new Date();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        VAP.put(Provider.VAP.TIMESTAMP, sdf.format(d));

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long newId = db.insert(Provider.VAP.TABLE_NAME, NO_NULL_COLUMN_HACK, VAP);
        return ContentUris.withAppendedId(CONTENT_URI, newId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_VAP_BY_ID:
                long id = ContentUris.parseId(uri);
                int affectedRows = databaseHelper.getWritableDatabase()
                        .delete(Provider.VAP.TABLE_NAME, Provider.VAP._ID + " = " + id, NO_SELECTION_ARGS);
                getContext().getContentResolver().notifyChange(CONTENT_URI, NO_CONTENT_OBSERVER);
                return affectedRows;
            default:
                return 0;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case URI_MATCH_VAP_BY_ID:
                return MIME_TYPE_SINGLE_NOTE;
            case URI_MATCH_VAP:
                return MIME_TYPE_NOTES;
        }
        return NO_TYPE;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}