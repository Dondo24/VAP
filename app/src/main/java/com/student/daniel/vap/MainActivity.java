package com.student.daniel.vap;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.student.daniel.vap.provider.Provider;
import com.student.daniel.vap.provider.VAPContentProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.student.daniel.vap.util.Defaults.DISMISS_ACTION;
import static com.student.daniel.vap.util.Defaults.NO_COOKIE;
import static com.student.daniel.vap.util.Defaults.NO_CURSOR;
import static com.student.daniel.vap.util.Defaults.NO_FLAGS;
import static com.student.daniel.vap.util.Defaults.NO_SELECTION;
import static com.student.daniel.vap.util.Defaults.NO_SELECTION_ARGS;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int VAP_LOADER_ID = 0;
    private static final int INSERT_VAP_TOKEN = 0;
    private static final int DELETE_VAP_TOKEN = 0;

    private GridView VAPGridView;
    private SimpleCursorAdapter adapter;
    private EditText newVAPEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(VAP_LOADER_ID, Bundle.EMPTY, this);

        VAPGridView = (GridView) findViewById(R.id.VAPGridView);
        VAPGridView.setAdapter(initializeAdapter());
        VAPGridView.setOnItemClickListener(this);
    }


    private void insertIntoContentProvider(Integer type,Double amount ,String VAPDescription) {
        Uri uri = VAPContentProvider.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Provider.VAP.TYPE,type);
        values.put(Provider.VAP.AMOUNT,amount);
        values.put(Provider.VAP.DESCRIPTION, VAPDescription);

        AsyncQueryHandler insertHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MainActivity.this, "VAP was saved", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        insertHandler.startInsert(INSERT_VAP_TOKEN, NO_COOKIE, uri, values);
    }

    private void createNewP() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);



        final EditText amount = new EditText(this);
        amount.setHint(R.string.amount);
        layout.addView(amount);

        final EditText description = new EditText(this);
        description.setHint(R.string.description);
        layout.addView(description);

        new AlertDialog.Builder(this)
                .setTitle(R.string.addnincome)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        String amount2 = amount.getText().toString();
                        if(!amount2.equals("")){
                            int pocitadlo = 0;

                            for (int i = 0; i < amount2.length(); i++) {
                                if(amount2.charAt(i)>='0' &&amount2.charAt(i)<='9' ||amount2.charAt(i)=='.'){
                                    pocitadlo++;
                                    System.out.println(pocitadlo);
                                    if(pocitadlo==amount2.length()) {
                                        System.out.println(amount2);
                                        double amou = Double.parseDouble(amount2);
                                        String description2 = description.getText().toString();
                                        insertIntoContentProvider(1, amou, description2);

                                    }
                                }
                            }


                        }
                    }
                })
                .setNegativeButton("Cancel", DISMISS_ACTION)
                .show();
    }
    private void createNewV() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);



        final EditText amount = new EditText(this);
        amount.setHint(R.string.amount);
        layout.addView(amount);

        final EditText description = new EditText(this);
        description.setHint(R.string.description);
        layout.addView(description);

        new AlertDialog.Builder(this)
                .setTitle(R.string.addnoutgo)
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String amount2 = amount.getText().toString();
                        if(!amount2.equals("")){
                            int pocitadlo = 0;

                            for (int i = 0; i < amount2.length(); i++) {
                                if(amount2.charAt(i)>='0' &&amount2.charAt(i)<='9' ||amount2.charAt(i)=='.'){
                                    pocitadlo++;
                                    System.out.println(pocitadlo);
                                    if(pocitadlo==amount2.length()) {
                                        System.out.println(amount2);
                                        double amou = Double.parseDouble(amount2);
                                            String description2 = description.getText().toString();
                                            insertIntoContentProvider(2, amou, description2);

                                    }
                                }
                            }


                        }
                    }
                })
                .setNegativeButton("Cancel", DISMISS_ACTION)
                .show();
    }


    private ListAdapter initializeAdapter() {


        String[] from = {Provider.VAP.TIMESTAMP };
        int[] to = {R.id.VAPGridViewItem};


                this.adapter = new SimpleCursorAdapter(this, R.layout.note, NO_CURSOR, from, to, NO_FLAGS);


        return this.adapter;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(VAPContentProvider.CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.adapter.swapCursor(NO_CURSOR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        Cursor selectedVAPCursor = (Cursor) parent.getItemAtPosition(position);
        int timeColumnIndex =selectedVAPCursor.getColumnIndex(Provider.VAP.TIMESTAMP);
        String VAPTime = selectedVAPCursor.getString(timeColumnIndex);

        int typeColumnIndex = selectedVAPCursor.getColumnIndex(Provider.VAP.TYPE);
        String VAPType = selectedVAPCursor.getString(typeColumnIndex);
        if(VAPType.equals("1")){
            VAPType=getString(R.string.in);

        }else{
            VAPType=getString(R.string.out);
        }

        int amountColumnIndex = selectedVAPCursor.getColumnIndex(Provider.VAP.AMOUNT);
        String VAPAmount = selectedVAPCursor.getString(amountColumnIndex);

        int descriptionColumnIndex = selectedVAPCursor.getColumnIndex(Provider.VAP.DESCRIPTION);
        String VAPDescription = selectedVAPCursor.getString(descriptionColumnIndex);

        new AlertDialog.Builder(this)

                .setMessage(getString(R.string.amount)+": "+VAPAmount+" €"+"\n"+getString(R.string.description)+": "+VAPDescription)


                .setTitle(VAPType)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteVAP(id);
                    }
                })
                .setNegativeButton("Close", DISMISS_ACTION)
                .show();
    }

    private void deleteVAP(long id) {
        Uri selectedVAPUri = ContentUris.withAppendedId(VAPContentProvider.CONTENT_URI, id);
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(MainActivity.this, "VAP was deleted", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        deleteHandler.startDelete(DELETE_VAP_TOKEN, NO_COOKIE, selectedVAPUri,
                NO_SELECTION, NO_SELECTION_ARGS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addincome) {
            createNewP();
            return true;
        }

        if (id == R.id.addoutgo) {
            createNewV();
            return true;
        }
        if(id==R.id.change){
            Intent intent = new Intent(this,ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //@Override
  /*  public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new) {
            createNewVAP();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}