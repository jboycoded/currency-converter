package com.example.helloandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SelectCurrency extends AppCompatActivity {
    RecyclerView recyclerView;
    private final List<Model> countryModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);

        //Set up toolbar as the new ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get support ActionBar corresponding to the toolbar and then enable the parent Up button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        BasicAdapter adapter = new BasicAdapter(countryModelList,this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        prepareAdapterContent();
    }

    /**
     * Overrides ActionBar's getSupportParentActivityIntent to enable sending of an extra
     * The Intent starts the MainActivity containing the extra, thereby specifying which Activity
     * started the MainActivity. This helps in determining the current Tab to select.
     * Since this Activity was started with ExchangeRate Tab, it sets the ViewPager's current tab to
     * ExchangeRate Tab with ViewPager.setCurrentItem(1) since ExchangeRate tab is the second item.
     *
     * @return the Intent used to start the parentActivity
     */
    @Override
    public Intent getSupportParentActivityIntent(){
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("SelectCurrency","NULL");
        return intent;
    }

    private void prepareAdapterContent(){
        Model title;

        // Load resources from arrays.xml in res/values/arrays.xml
        Resources res = getResources();
        TypedArray icons = res.obtainTypedArray(R.array.images);
        TypedArray countries = res.obtainTypedArray(R.array.countries);
        TypedArray currencies = res.obtainTypedArray(R.array.currencies);

        for(int index = 0; index < 72; index++){
            title = new Model(countries.getString(index),currencies.getString(index), icons.getResourceId(index,0));
            countryModelList.add(title);
        }

        icons.recycle();
        countries.recycle();
        currencies.recycle();
    }

}