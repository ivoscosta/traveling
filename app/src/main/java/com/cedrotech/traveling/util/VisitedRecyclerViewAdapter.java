package com.cedrotech.traveling.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cedrotech.traveling.R;
import com.cedrotech.traveling.model.Country;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ivo on 07/06/16.
 */
public class VisitedRecyclerViewAdapter extends RecyclerView.Adapter<VisitedRecyclerViewHolder> {

    private List<Country> itemList;
    private Context context;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public VisitedRecyclerViewAdapter(Context context, List<Country> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public VisitedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_visited, null, false);
        VisitedRecyclerViewHolder rcv = new VisitedRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(VisitedRecyclerViewHolder holder, int position) {
        holder.setCountry(itemList.get(position));
        holder.countryName.setText(itemList.get(position).getShortname());
        holder.countryVisitedDate.setText(formatter.format(itemList.get(position).getVisiteddate()));
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

