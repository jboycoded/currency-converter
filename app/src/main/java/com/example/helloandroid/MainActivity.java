package com.example.helloandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.helloandroid.fragments.calculator_fragment;
import com.example.helloandroid.fragments.exchangeRate_fragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume(){
        super.onResume();
        final Intent intent = getIntent();
        // If intent contains the extra with key:"SelectCurrency",set current Tab to ExchangeRate Tab
        if(intent.hasExtra("SelectCurrency")){
            viewPager.setCurrentItem(1);
            intent.removeExtra("SelectCurrency");
            //Log.e("HI THERE","STARTED BY SECOND ACTIVITY");
        }
    }

    private void setupViewPager(ViewPager viewPager){
        //TODO: Add the third tab fragment after fully completing the first two tabs
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new calculator_fragment(), "Calculator");
        adapter.addFragment(new exchangeRate_fragment(), "Exchange Rate");
        viewPager.setAdapter(adapter);
    }

}