package com.example.helloandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloandroid.R;
import com.example.helloandroid.SelectCurrency;
import com.example.helloandroid.helper.Utility;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass corresponding to a single tab, the Exchange Rate tab
 */
@SuppressWarnings("deprecation")
public class exchangeRate_fragment extends Fragment {
    // textViewSet is used to monitor which of the two amountText TextView is set/highlighted
    // value 1 for amountText1 and value 2 for amountText2
    private int textViewSet = 1;
    boolean canClear = true;
    Double rate = 1.0;
    private final int REQUEST_CODE1 = 100;
    private final int REQUEST_CODE2 = 200;

    View inflate;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView imageView1, imageView2;
    TextView countryTextView1, countryTextView2;
    TextView amountTextView1, amountTextView2;

    // For Shared preferences, both holder object and filename
    private SharedPreferences preferences;
    int imageId11;
    int imageId22;

    // Number buttons for inputting numbers
    Button one,two,three,four,five,six,seven;
    Button eight,nine,zero,dot,double_zero,del,ce;

    public exchangeRate_fragment() {
        //Required empty constructor
    }

    /**
     * onPause() lifecycle method to use in saving shared preferences value in the case of
     * a change in configuration or destruction of the Activity or Fragment.
     */
    @Override
    public void onPause(){
        super.onPause();
        // Retrieve current values of the state of the various views
        String countryText1 = countryTextView1.getText().toString();
        String countryText2 = countryTextView2.getText().toString();
        String amountText1 = amountTextView1.getText().toString();
        String amountText2 = amountTextView2.getText().toString();

        //TODO: find a way to get a reference to the drawable of an ImageView
        //find a way to convert imageId into a form that can be saved into SharedReference
        //idea: make it into encoded into base64 string format
        int imageId1 = imageId11;
        int imageId2 = imageId22;

        // Then create a SharedPreferences.Editor object to edit the SharedPreferences
        // Add all the necessary values to save and then commit() or apply()
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("countryText1",countryText1);
        preferencesEditor.putString("countryText2",countryText2);
        preferencesEditor.putString("amountText1",amountText1);
        preferencesEditor.putString("amountText2",amountText2);
        preferencesEditor.putInt("imageId1",imageId1);
        preferencesEditor.putInt("imageId2",imageId2);
        preferencesEditor.apply();
    }

    /**
     * This method inflates the Fragment's views, restores previously saved SharedPreferences values
     * and handles clicks on the various views
     *
     * @param inflater LayoutInflater object to inflate views
     * @param container ViewGroup that the Fragment's UI should be attached to
     * @param savedInstanceState the Bundle that holds the onSavedInstanceState values
     * @return inflated Fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_exchange_fragment, container, false);
        initializeButtons();

        this.context = getActivity();
        Intent intent = new Intent();
        intent.setClass(getActivity(), SelectCurrency.class);

        // View Binding of all needed ImageViews and TextViews
        swipeRefreshLayout = inflate.findViewById(R.id.refresh);
        imageView1 = inflate.findViewById(R.id.imageView1);
        imageView2 = inflate.findViewById(R.id.imageView2);
        countryTextView1 = inflate.findViewById(R.id.countryText1);
        countryTextView2 = inflate.findViewById(R.id.countryText2);
        amountTextView1 = inflate.findViewById(R.id.amountText1);
        amountTextView2 = inflate.findViewById(R.id.amountText2);
        setChosenTextView(textViewSet);

        // Restore previously saved SharedPreferences values
        restorePreferenceValues();

        // onClick() Handlers for the various textView widgets
        countryTextView1.setOnClickListener(v -> startActivityForResult(intent, REQUEST_CODE1));
        countryTextView2.setOnClickListener(v -> startActivityForResult(intent, REQUEST_CODE2));
        amountTextView1.setOnClickListener(v -> {
            textViewSet = 1;
            setChosenTextView(textViewSet);
        });
        swipeRefreshLayout.setOnRefreshListener(this::upDateCurrency);

        return inflate;
    }

    private void setRefreshAction(){
        canClear = true;
        swipeRefreshLayout.setRefreshing(false);
    }

    private void upDateCurrency() {
        OkHttpClient client = new OkHttpClient();
        final String[] exchangeRate = new String[1];
        String api_key = "a922963973ce38489df52586d3ddd8712a5b6714";

        String from = countryTextView1.getText().toString();
        String to = countryTextView2.getText().toString();
        from = from.substring(from.lastIndexOf(" "));
        to = to.substring(to.lastIndexOf(" "));

        Request request = Utility.sendRequest(from, to, api_key);
        String finalTo = to.trim();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                try {
                    JSONObject obj = new JSONObject(responseData);
                    exchangeRate[0] = obj.getJSONObject("rates").getJSONObject(finalTo).getString("rate");
                    rate = Double.parseDouble(exchangeRate[0]);

                    new Handler(Looper.getMainLooper()).post(() -> {
                        String text = amountTextView1.getText().toString();
                        text = String.valueOf(Double.parseDouble(text) * rate);
                        setTextToTextView(amountTextView2, text);
                    });
                    //Log.e("HI THERE", "SUCCESS 4" );
                    //Toast.makeText(context, "Currency value updated", Toast.LENGTH_SHORT).show();
                    //Log.e("HI THERE", "SUCCESS 5" );

                } catch (JSONException e) {
                    //Log.e("HI THERE", "FAILURE 1" );
                    //Toast.makeText(context, "Failure to update currency value", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                e.printStackTrace();
                //Log.e("HI THERE", "FAILURE 2" );
                //Toast.makeText(context, "Failure to update currency value", Toast.LENGTH_SHORT).show();
            }
        });
        setRefreshAction();
    }

    private void restorePreferenceValues(){
        String preferenceFile = context.getPackageName();
        preferences = context.getSharedPreferences(preferenceFile,Context.MODE_PRIVATE);

        // Default values for the shared preferences if there is no previously stored value
        String defCountryText = "US Dollar USD";
        String defAmountText = "100";
        int defImageId = R.drawable.ic_flag_of_the_united_states;

        // Read and Restore the saved shared preferences
        amountTextView1.setText(preferences.getString("amountText1", defAmountText));
        amountTextView2.setText(preferences.getString("amountText2", defAmountText));
        countryTextView1.setText(preferences.getString("countryText1", defCountryText));
        countryTextView2.setText(preferences.getString("countryText2", defCountryText));
        imageView1.setImageResource(preferences.getInt("imageId1", defImageId));
        imageView2.setImageResource(preferences.getInt("imageId2", defImageId));
    }

    private void setChosenTextView(int textViewSet){
        canClear = true;
        if (textViewSet == 1){
            amountTextView1.setTextColor(ContextCompat.getColor(context,R.color.cyanA100));
            amountTextView2.setTextColor(ContextCompat.getColor(context,R.color.text_color));
        }
    }

    /**
     * Helper method to bind all the buttons in the fragment view to a Button object
     */
    private void initializeButtons() {
        one = inflate.findViewById(R.id.button1);
        two = inflate.findViewById(R.id.button2);
        three = inflate.findViewById(R.id.button3);
        four = inflate.findViewById(R.id.button4);
        five = inflate.findViewById(R.id.button5);
        six = inflate.findViewById(R.id.button6);
        seven = inflate.findViewById(R.id.button7);
        eight = inflate.findViewById(R.id.button8);
        nine = inflate.findViewById(R.id.button9);
        zero = inflate.findViewById(R.id.button0);
        double_zero = inflate.findViewById(R.id.button00);
        dot = inflate.findViewById(R.id.button_dot);
        ce = inflate.findViewById(R.id.button_c);
        del = inflate.findViewById(R.id.button_x);

        // onClick() Handlers for each of the button widgets so they can respond to click events
        one.setOnClickListener(v -> addToTextView("1"));
        two.setOnClickListener(v -> addToTextView("2"));
        three.setOnClickListener(v -> addToTextView("3"));
        four.setOnClickListener(v -> addToTextView("4"));
        five.setOnClickListener(v -> addToTextView("5"));
        six.setOnClickListener(v -> addToTextView("6"));
        seven.setOnClickListener(v -> addToTextView("7"));
        eight.setOnClickListener(v -> addToTextView("8"));
        nine.setOnClickListener(v -> addToTextView("9"));
        dot.setOnClickListener(v -> {
            if (textViewSet == 1){
                String text = amountTextView1.getText().toString();
                // if it doesn't contain a decimal point, append to the TextView
                if (!text.contains(".")) {
                    amountTextView1.append(".");
                }
            }
        });
        zero.setOnClickListener(v -> addToTextView("0"));
        double_zero.setOnClickListener(v -> addToTextView("00"));
        ce.setOnClickListener(v -> {
            amountTextView1.setText("0");
            amountTextView2.setText("0");
        });
    }

    private void addToTextView(String str) {
        String text,text2;
        if (textViewSet == 1) {
            if (canClear) {
                amountTextView1.setText("");
                canClear = false;
            }
            text = amountTextView1.getText().toString().replace(",", "");
            if (text.equals("0")) text = "";
            text = text + str;
            setTextToTextView(amountTextView1, text);

            //multiply typed value with the exchange rate to update the other textview
            text2 = String.valueOf(Double.parseDouble(text) * rate);
            //update the second currency immediately
            setTextToTextView(amountTextView2, text2);
        }
    }

    /** Helper method to append letter to the TextView. It formats the text using NumberFormat and
     * then set to the TextView
     *
     * @param textView the TextView to set the text to
     * @param text the text to be formatted
     */
    private void setTextToTextView(TextView textView, String text){
        //New thread to perform non-UI actions in the background
        new Thread(() -> {
            int length = text.length();
            String newText;
            boolean isDecimal = text.contains(".");

            // if it is a decimal, reduce the length by 1 to exclude the decimal point
            if (isDecimal) length = length - 1;
            if (length > 10 && textView == amountTextView1){
                new Handler(Looper.getMainLooper()).post(
                        () -> Toast.makeText(context, "Maximum digits (10) reached", Toast.LENGTH_SHORT).show());
            }
            else{
                NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en","US"));
                numberFormat.setMaximumFractionDigits(10);
                if (isDecimal) newText = numberFormat.format(Double.parseDouble(text));
                else newText = numberFormat.format(Long.parseLong(text));
                textView.post(() -> textView.setText(newText));
            }
        }).start();
    }

    /**
     * This method receives result from SelectCurrency activity. It receives the chosen country as
     * a bundle of extras and then loads them to a String and an int object to be used by a
     * TextView and ImageView
     *
     * @param requestCode the requestCode that is used in starting this method
     * @param resultCode the resultCode indicating the success or failure of the Intent
     * @param intent the intent received from the SelectCurrency Activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        String country;
        if (intent == null){
            return;
        }
        Bundle extras = intent.getExtras();
        if (resultCode == Activity.RESULT_OK){
            switch(requestCode) {
                case 100:
                    country = extras.getString("COUNTRY");
                    imageId11 = extras.getInt("DRAWABLE_ID");
                    imageView1.setImageResource(imageId11);
                    countryTextView1.setText(country);
                    break;
                case 200:
                    country = extras.getString("COUNTRY");
                    imageId22 = extras.getInt("DRAWABLE_ID");
                    imageView2.setImageResource(imageId22);
                    countryTextView2.setText(country);
                    break;
                default:
                    break;
            }
            swipeRefreshLayout.setRefreshing(true);
            upDateCurrency();
        }
    }
}