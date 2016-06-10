package com.cedrotech.traveling.util;

/**
 * Created by ivo on 05/06/16.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.controller.CountryActivity;
import com.cedrotech.traveling.model.Country;

public class CountryRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView countryName;
    public ImageView countryFlag, countryVisitedDate;
    private Country country;
    private final Context context;

    public CountryRecyclerViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryFlag = (ImageView)itemView.findViewById(R.id.country_flag);
        countryVisitedDate = (ImageView)itemView.findViewById(R.id.country_visited);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, CountryActivity.class);
        intent.putExtra("country", country);
        context.startActivity(intent);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}