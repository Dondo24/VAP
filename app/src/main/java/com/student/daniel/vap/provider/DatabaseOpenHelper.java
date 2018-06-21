package com.student.daniel.vap.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.student.daniel.vap.util.Defaults;

import static android.provider.BaseColumns._ID;
import static com.student.daniel.vap.util.Defaults.DEFAULT_CURSOR_FACTORY;

public class DatabaseOpenHelper extends SQLiteOpenHelper {


        public static final String DATABASE_NAME = "vap";
        public static final int DATABASE_VERSION = 1;

        public DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, DEFAULT_CURSOR_FACTORY, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTableSql());



        }

        private void insertSampleEntry(SQLiteDatabase db,Integer type,Double amount, String description) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Provider.VAP.TYPE,type);
            contentValues.put(Provider.VAP.AMOUNT,amount);
            contentValues.put(Provider.VAP.DESCRIPTION, description);
            contentValues.put(Provider.VAP.TIMESTAMP, System.currentTimeMillis() / 1000);
            db.insert(Provider.VAP.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, contentValues);
        }

        private String createTableSql() {
            String sqlTemplate = "CREATE TABLE %s ("
                    + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "%s INTEGER,"
                    + "%s REAL,"
                    + "%s TEXT,"
                    + "%s DATETIME"
                    + ")";
            return String.format(sqlTemplate, Provider.VAP.TABLE_NAME, _ID,Provider.VAP.TYPE,Provider.VAP.AMOUNT ,Provider.VAP.DESCRIPTION, Provider.VAP.TIMESTAMP);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // do nothing
        }
    }