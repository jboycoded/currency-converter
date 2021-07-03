package com.example.helloandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasicAdapter extends RecyclerView.Adapter<BasicAdapter.BasicViewHolder> {
    // Adapter for RecyclerView

    private final List<Model> countryModelList;
    private final Context context;
    private final Activity mActivity;

    public BasicAdapter(List<Model> countryModelList, Context context, Activity mActivity){
        this.countryModelList = countryModelList;
        this.context = context;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public BasicAdapter.BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new BasicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicAdapter.BasicViewHolder holder, int position) {
        final Model countryModel = countryModelList.get(position);

        holder.country.setText(countryModel.getCountry());
        holder.currency.setText(countryModel.getCurrency());
        holder.image.setImageResource(countryModel.getImage());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, countryModel.getCountry(), Toast.LENGTH_SHORT).show();
            passData(countryModel.getCountry(), countryModel.getCurrency(),countryModel.getImage());
        });

    }

    /**
     * Private method to send the chosen RecyclerView item back to the MainActivity as a replyIntent,
     * The chosen item is sent in the form of a string message  using the text of the item.
     * The String to send is passed to the passData() and will be used as an EXTRA MESSAGE
     */
    private void passData(String country, String currency, int image){
        String message = country +" "+ currency;
        Bundle extras = new Bundle();
        extras.putString("COUNTRY", message);
        extras.putInt("DRAWABLE_ID", image);

        Intent replyIntent = new Intent();
        replyIntent.putExtras(extras);
        mActivity.setResult(Activity.RESULT_OK, replyIntent);
        mActivity.finish();
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    public static class BasicViewHolder extends RecyclerView.ViewHolder{
        private final ImageView image;
        private final TextView country, currency;

        public BasicViewHolder(@NonNull View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.image);
            country = itemView.findViewById(R.id.country_name);
            currency = itemView.findViewById(R.id.country_currency);
        }
    }
}
