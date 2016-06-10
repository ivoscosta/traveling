package com.cedrotech.traveling.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.dao.VisitedContract;
import com.cedrotech.traveling.dao.VisitedDbHelper;
import com.cedrotech.traveling.model.Country;
import com.cedrotech.traveling.util.DividerListItemDecorator;
import com.cedrotech.traveling.util.VisitedRecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitedFragment extends Fragment {

    private LinearLayoutManager lLayout;
    private View rootView;
    private List<Country> visitedItems;
    private LinearLayout preloader;
    private RecyclerView rView;
    private VisitedDbHelper mDbHelper;
    private SQLiteDatabase db;

    public VisitedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lLayout = new LinearLayoutManager(getActivity());
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
        rootView = inflater.inflate(R.layout.fragment_visited, container, false);

        preloader = (LinearLayout) rootView.findViewById(R.id.progressBar);
        rView = (RecyclerView) rootView.findViewById(R.id.visited_recycler_view);
        rView.setLayoutManager(lLayout);
        rView.addItemDecoration(new DividerListItemDecorator(getActivity(), R.drawable.list_divider));
        return rootView;
    }

    public void init() {
        preloader.setVisibility(View.VISIBLE);
        rView.setVisibility(View.GONE);
        getVisitedCountries();
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

    private void getVisitedCountries() {
        String[] projection = {
                "*"
        };

        Cursor cursor = db.query(
                VisitedContract.Country.TABLE_NAME,  // The table to query
                projection,                          // The columns to return
                null,                                // The columns for the WHERE clause
                null,                                // The values for the WHERE clause
                null,                                // don't group the rows
                null,                                // don't filter by row groups
                null                                 // The sort order
        );

        visitedItems = new ArrayList<Country>();
        if (cursor.moveToFirst()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            Country c;
            while(cursor.isAfterLast() == false) {
                c = new Country();
                c.setId(cursor.getInt(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_ID)));
                c.setIso(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_ISO)));
                c.setShortname(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_SHORTNAME)));
                c.setLongname(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_LONGNAME)));
                c.setCallingcode(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_CALLINGCODE)));
                c.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_STATUS)));
                c.setCulture(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_CULTURE)));

                //tratando a data
                try {
                    date = formatter.parse(cursor.getString(cursor.getColumnIndexOrThrow(VisitedContract.Country.COLUMN_VISITED_DATE)));
                    c.setVisiteddate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                visitedItems.add(c);
                cursor.moveToNext();
            }
        }

        VisitedRecyclerViewAdapter rcAdapter = new VisitedRecyclerViewAdapter(getActivity(), visitedItems);
        rView.setAdapter(rcAdapter);
        finishLoading();
    }
}
