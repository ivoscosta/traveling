package com.cedrotech.traveling.controller;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.dao.VisitedContract;
import com.cedrotech.traveling.dao.VisitedDbHelper;
import com.cedrotech.traveling.model.Country;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CountryActivity extends AppCompatActivity {

    private ImageView flag;
    private TextView shortname, infos, dateVisitedTxt;
    private LinearLayout address, photos, dateVisited;
    private Country country;
    private Intent intent;
    private Calendar calendar;
    private int year, month, day;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private VisitedDbHelper mDbHelper;
    private SQLiteDatabase db;
    private String mode = "update";
    private Menu menuGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mDbHelper = new VisitedDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        country = (Country) getIntent().getSerializableExtra("country");

        flag = (ImageView) findViewById(R.id.flag);
        infos = (TextView) findViewById(R.id.infos);
        shortname = (TextView) findViewById(R.id.shortname);

        Uri uri = Uri.parse(this.getString(R.string.SERVER_URL_COUNTRIES_FLAGS, country.getId()));
        Picasso.with(this).load(uri).into(flag);
        shortname.setText(country.getShortname());
        infos.setText(country.getLongname()+"\n("+country.getCallingcode()+")");

        address = (LinearLayout) findViewById(R.id.address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.co.in/maps?q=" + country.getShortname();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        photos = (LinearLayout) findViewById(R.id.photos);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/explore/tags/" + country.getShortname());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        dateVisited = (LinearLayout) findViewById(R.id.dateVisited);
        dateVisitedTxt = (TextView) findViewById(R.id.dateVisitedTxt);
        if(country.getVisiteddate() != null){
            dateVisitedTxt.setText(getResources().getString(R.string.visited) + " " + formatter.format(country.getVisiteddate()));
            dateVisited.setVisibility(View.VISIBLE);
        }
        else {
            dateVisited.setVisibility(View.GONE);
        }

        dateVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = "update";
                setDate();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            saveDate(arg1, arg2+1, arg3);
        }
    };

    private void saveDate(int year, int month, int day) {
        try {
            Date date = null;
            date = formatter.parse(day+"/"+month+"/"+year);
            country.setVisiteddate(date);
            dateVisitedTxt.setText(getResources().getString(R.string.visited) + " " + formatter.format(country.getVisiteddate()));
            dateVisited.setVisibility(View.VISIBLE);

            if(mode == "insert")
                insertVisitedCountry(country);
            else
                updateVisitedCountry(country);
            inflateMenus();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuGlobal = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        inflateMenus();
        return true;
    }

    private void inflateMenus(){
        menuGlobal.clear();
        getMenuInflater().inflate(R.menu.menu_country, menuGlobal);
        if(country.getVisiteddate() == null){
            menuGlobal.removeItem(R.id.action_remove);
        }
        else {
            menuGlobal.removeItem(R.id.action_visite);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_visite) {
            mode = "insert";
            setDate();
            return true;
        }
        if (id == R.id.action_remove) {
            removeVisitedCountry(country);
            return true;
        }
        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://" + getResources().getString(R.string.app_site) + "/country/" + country.getId());
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, String.format(getResources().getString(R.string.found_object), country.getShortname()));
            startActivity(Intent.createChooser(intent, "Share"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertVisitedCountry(Country country){
        ContentValues values = new ContentValues();
        values.put(VisitedContract.Country.COLUMN_ID, country.getId());
        values.put(VisitedContract.Country.COLUMN_ISO, country.getIso());
        values.put(VisitedContract.Country.COLUMN_SHORTNAME, country.getShortname());
        values.put(VisitedContract.Country.COLUMN_LONGNAME, country.getLongname());
        values.put(VisitedContract.Country.COLUMN_CALLINGCODE, country.getCallingcode());
        values.put(VisitedContract.Country.COLUMN_STATUS, country.getStatus());
        values.put(VisitedContract.Country.COLUMN_CULTURE, country.getCulture());
        values.put(VisitedContract.Country.COLUMN_VISITED_DATE, formatter.format(country.getVisiteddate()));

        long newRowId;
        newRowId = db.insert(
                VisitedContract.Country.TABLE_NAME,
                null,
                values);
    }

    private void updateVisitedCountry(Country country){
        ContentValues values = new ContentValues();
        values.put(VisitedContract.Country.COLUMN_VISITED_DATE, formatter.format(country.getVisiteddate()));

        String selection = VisitedContract.Country.COLUMN_ID + " = " + country.getId();
        String[] selectionArgs = { String.valueOf(country.getId()) };

        long newRowId;
        newRowId = db.update(
                VisitedContract.Country.TABLE_NAME,
                values,
                selection,
                null);
    }

    private void removeVisitedCountry(Country country){
        String selection = VisitedContract.Country.COLUMN_ID + " = " + country.getId();
        db.delete(
                VisitedContract.Country.TABLE_NAME,
                selection,
                null);

        country.setVisiteddate(null);
        dateVisited.setVisibility(View.GONE);
        inflateMenus();
    }
}
