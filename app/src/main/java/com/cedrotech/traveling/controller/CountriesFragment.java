package com.cedrotech.traveling.controller;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.dao.VisitedContract;
import com.cedrotech.traveling.dao.VisitedDbHelper;
import com.cedrotech.traveling.model.Country;
import com.cedrotech.traveling.util.JSONParser;
import com.cedrotech.traveling.util.CountryRecyclerViewAdapter;
import com.cedrotech.traveling.util.Utils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {

    private GridLayoutManager lLayout;
    private View rootView;
    private List<Country> allItems;
    private JSONParser jsonParser;
    private String SERVER_URL_COUNTRIES;
    private LinearLayout noInternet;
    private LinearLayout preloader;
    private RecyclerView rView;
    private VisitedDbHelper mDbHelper;
    private SQLiteDatabase db;

    public CountriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jsonParser = new JSONParser();
        lLayout = new GridLayoutManager(getActivity(), 4);
        SERVER_URL_COUNTRIES = getResources().getString(R.string.SERVER_URL_COUNTRIES);
        mDbHelper = new VisitedDbHelper(getContext());
        db = mDbHelper.getReadableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true); //usado para acessar os menus do fragment
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_countries, container, false);

        noInternet = (LinearLayout) rootView.findViewById(R.id.no_internet);
        preloader = (LinearLayout) rootView.findViewById(R.id.progressBar);
        rView = (RecyclerView) rootView.findViewById(R.id.countries_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        noInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
        return rootView;
    }

    public void init() {
        if (Utils.isConnectInternet(getActivity()))
            initLoading();
        else {
            noInternet.setVisibility(View.VISIBLE);
            rView.setVisibility(View.GONE);
        }
    }

    private void initLoading() {
        preloader.setVisibility(View.VISIBLE);
        rView.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
        new GetCountriesServer().execute();
    }

    private void finishLoading() {
        preloader.setVisibility(View.GONE);
        rView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.action_logout);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            System.out.println("##search");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetCountriesServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONArray ar = jsonParser.makeHttpRequest(SERVER_URL_COUNTRIES, "GET", params);
                JSONObject obj;
                allItems = new ArrayList<Country>();
                for (int i = 0; i < ar.length(); i++) {
                    obj = ar.getJSONObject(i);

                    //Criando o objeto country
                    Country c = new Country();
                    c.setCallingcode(obj.getString("callingCode"));
                    c.setCulture(obj.getString("culture"));
                    c.setId(obj.getInt("id"));
                    c.setIso(obj.getString("iso"));
                    c.setLongname(obj.getString("longname"));
                    c.setShortname(obj.getString("shortname"));
                    c.setStatus(obj.getInt("status"));

                    allItems.add(c);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String res) {

            //colocando a data de visita caso tenha
            for(int i=0; i<allItems.size(); i++){
                Date visitedDate = getVisitedDate(allItems.get(i).getId());
                if(visitedDate != null){
                    allItems.get(i).setVisiteddate(visitedDate);
                }
            }

            CountryRecyclerViewAdapter rcAdapter = new CountryRecyclerViewAdapter(getActivity(), allItems);
            rView.setAdapter(rcAdapter);
            finishLoading();
            onCancelled();
        }

    }

    private Date getVisitedDate(int id) {
        String[] projection = {
                VisitedContract.Country.COLUMN_VISITED_DATE
        };

        Cursor cursor = db.query(
                VisitedContract.Country.TABLE_NAME,  // The table to query
                projection,                          // The columns to return
                VisitedContract.Country.COLUMN_ID + " = " +id,   // The columns for the WHERE clause
                null,         // The values for the WHERE clause
                null,                                // don't group the rows
                null,                                // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.moveToFirst()){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            while (cursor.isAfterLast() == false) {
                cursor.moveToFirst();
                try {
                    date = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_VISITED_DATE)));
                    return date;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
