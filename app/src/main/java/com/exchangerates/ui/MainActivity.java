package com.exchangerates.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.exchangerates.R;
import com.exchangerates.adapter.NbuTableAdapter;
import com.exchangerates.adapter.PbTableAdapter;
import com.exchangerates.model.NbuCurrency;
import com.exchangerates.model.PbCurrency;
import com.exchangerates.util.Constants;
import com.exchangerates.util.Converters;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PbTableAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private int mPbSelectedItemIndex;
    private int mNbuSelectedItemIndex;

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private EditText mEtDatePicker;

    private RecyclerView mRecyclerViewPb;
    private RecyclerView mRecyclerViewNbu;

    private LinearLayoutManager mPbLayoutManager;
    private LinearLayoutManager mNbuLayoutManager;

    private PbTableAdapter mPbAdapter;
    private NbuTableAdapter mNbuAdapter;

    private RequestQueue mRequestQueue;

    private List<PbCurrency> mPbCurrencyList;
    private List<NbuCurrency> mNbuCurrencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        mEtDatePicker = findViewById(R.id.et_date);
        setDefaultDateToEditText();

        mEtDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);

        //region PbCurrencyList
        mPbCurrencyList = new ArrayList<>();
        getPbCurrencies();

        mPbAdapter = new PbTableAdapter(this, mPbCurrencyList, this);

        mRecyclerViewPb = findViewById(R.id.table_pb);
        mRecyclerViewPb.setAdapter(mPbAdapter);
        mNbuLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewPb.setLayoutManager(mNbuLayoutManager);
        //endregion

        //region NbuCurrencyList
        mNbuCurrencyList = new ArrayList<>();
        getNbuCurrencies();
        mNbuAdapter = new NbuTableAdapter(this, mNbuCurrencyList);

        mRecyclerViewNbu = findViewById(R.id.table_nbu);
        mRecyclerViewNbu.setAdapter(mNbuAdapter);
        mNbuLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewNbu.setLayoutManager(mNbuLayoutManager);
        //endregion
    }

    @Override
    public void onItemClick(int position) {

        changeCcyStylesToDefault();

        int positionToScroll = 0;
        String ccyCode = mPbCurrencyList.get(position).getCcy();

        switch (ccyCode) {
            case "USD":
                displayToast("USD");
                positionToScroll = getNbuItemPositionByCcy(ccyCode);
                mPbSelectedItemIndex = position;
                mNbuSelectedItemIndex = positionToScroll;
                changeNbuStyleToSelected();
                break;
            case "EUR":
                displayToast("EUR");
                positionToScroll = getNbuItemPositionByCcy(ccyCode);
                mPbSelectedItemIndex = position;
                mNbuSelectedItemIndex = positionToScroll;
                changeNbuStyleToSelected();
                break;
            case "RUR":
                displayToast("RUB");
                // Ccy codes of PB and NBU APIs differ so it has to be changed for scrolling
                positionToScroll = getNbuItemPositionByCcy("RUB");
                mPbSelectedItemIndex = position;
                mNbuSelectedItemIndex = positionToScroll;
                changeNbuStyleToSelected();
                break;

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("pbCcySelectedIndex", mPbSelectedItemIndex);
        outState.putInt("nbuCcySelectedIndex", mNbuSelectedItemIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mPbSelectedItemIndex = savedInstanceState.getInt("pbCcySelectedIndex");
            mNbuSelectedItemIndex = savedInstanceState.getInt("nbuCcySelectedIndex");
        } else {
            mPbSelectedItemIndex = 0;
            mNbuSelectedItemIndex = 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_statistics) {
            displayToast("TBA");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getNbuItemPositionByCcy(String ccy) {
        int position = 0;
        for (int i = 0; i < mNbuCurrencyList.size(); i++) {
            if (mNbuCurrencyList.get(i).getCcy().equals(ccy)) {
                Log.d(TAG, "item found at position: " + i);
                position = i;
                break;
            }
        }
        return position;
    }

    private void changeNbuStyleToSelected() {
        mNbuLayoutManager.scrollToPosition(mNbuSelectedItemIndex);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewNbu.findViewHolderForAdapterPosition(mNbuSelectedItemIndex)
                        .itemView.setBackgroundColor(Color.parseColor("#a1c8ba"));
            }
        }, 50);
    }

    private void changeCcyStylesToDefault() {
        mRecyclerViewPb
                .findViewHolderForAdapterPosition(mPbSelectedItemIndex)
                .itemView.setBackgroundColor(Color.TRANSPARENT);
        mRecyclerViewNbu
                .findViewHolderForAdapterPosition(mNbuSelectedItemIndex)
                .itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    private void getPbCurrencies() {
        JsonArrayRequest request = new JsonArrayRequest(Constants.EXCHANGE_RATES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                PbCurrency currency =
                                        Converters.fromJsonToPbCurrency(response.getJSONObject(i));
                                Log.i(TAG, "onResponse: adding pb ccy" + currency.toString());
                                if (!currency.getCcy().equals("BTC"))
                                    mPbCurrencyList.add(currency);
                            } catch (JSONException e) {
                                Log.e(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                        mPbAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });
        mRequestQueue.add(request);
    }

    private void getNbuCurrencies() {
        JsonArrayRequest request = new JsonArrayRequest(Constants.NBU_CURRENCIES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                NbuCurrency currency =
                                        Converters.fromJsonToNbuCurrency(response.getJSONObject(i));
                                Log.i(TAG, "onResponse: adding nbu ccy" + currency.toString());
                                mNbuCurrencyList.add(currency);
                            } catch (JSONException e) {
                                Log.e(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                        mNbuAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });
        mRequestQueue.add(request);
    }

    private void updateNbuCurrenciesTable(String date) {

        mNbuCurrencyList.clear();

        JsonArrayRequest request = new JsonArrayRequest(String.format("%s%s&json",
                Constants.NBU_CURRENCIES_DATE_URL,
                date),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                NbuCurrency currency =
                                        Converters.fromJsonToNbuCurrency(response.getJSONObject(i));
                                Log.i(TAG, "onResponse: updating nbu ccy " + currency.toString());
                                mNbuCurrencyList.add(currency);
                            } catch (JSONException e) {
                                Log.e(TAG, "onResponse: " + e.getMessage());
                            }
                        }
                        mNbuAdapter.notifyDataSetChanged();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });

        Log.d(TAG, "updateNbuCurrenciesTable: URL: " + request.getUrl());
        mRequestQueue.add(request);
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarTitle = findViewById(R.id.toolbar_title);
    }

    private void setDefaultDateToEditText() {
        Calendar c = Calendar.getInstance();

        String defaultDate =
                Converters.dateToStringWithDots
                        (c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
        mEtDatePicker.setText(defaultDate);
    }

    private void updateEdDate(int day, int month, int year) {
        String newDate = Converters.dateToStringWithDots(day, month, year);
        mEtDatePicker.setText(newDate);
    }

    private void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Displays a toast with chosen date and updates list of nbu currencies.
     */
    public void processDatePickerResult(int year, int month, int day) {
        String dateString = Converters.dateToStringQueryParam(year, month, day);
        displayToast("Курси на " + Converters.dateToStringWithDots(day, month, year));
        updateNbuCurrenciesTable(dateString);
        updateEdDate(day, month, year);
    }

    /**
     * Displays the actual message in a toast message.
     *
     * @param message Message to display.
     */
    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}

