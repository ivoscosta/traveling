package com.cedrotech.traveling.util;

/**
 * Created by ivo on 05/06/16.
 */
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.model.Country;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<CountryRecyclerViewHolder> {

    private List<Country> itemList;
    private Context context;

    public CountryRecyclerViewAdapter(Context context, List<Country> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CountryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_country, null);
        CountryRecyclerViewHolder rcv = new CountryRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(CountryRecyclerViewHolder holder, int position) {
        holder.setCountry(itemList.get(position));
        holder.countryName.setText(itemList.get(position).getShortname());
        if(itemList.get(position).getVisiteddate() != null){
            System.out.println(itemList.get(position).getVisiteddate());
            holder.countryVisitedDate.setVisibility(View.VISIBLE);
        }
        else
            holder.countryVisitedDate.setVisibility(View.GONE);
        Uri uri = Uri.parse(context.getString(R.string.SERVER_URL_COUNTRIES_FLAGS, itemList.get(position).getId()));
        Picasso.with(context).load(uri).into(holder.countryFlag);
    }

    @Override
    public int getItemCount() {
        if(this.itemList != null)
            return this.itemList.size();
        return 0;
    }
}
