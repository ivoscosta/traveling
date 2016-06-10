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
import com.pkmmte.view.CircularImageView;

public class VisitedRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView countryName, countryVisitedDate;
    public CircularImageView countryFlag;
    private Country country;
    private final Context context;

    public VisitedRecyclerViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        itemView.setOnClickListener(this);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryVisitedDate = (TextView)itemView.findViewById(R.id.country_visited_date);
        countryFlag = (CircularImageView)itemView.findViewById(R.id.country_flag);
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