package com.exchangerates.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exchangerates.R;
import com.exchangerates.model.NbuCurrency;

import java.util.List;


public class NbuTableAdapter extends RecyclerView.Adapter<NbuTableAdapter.NbuCurrencyHolder> {

    private final LayoutInflater mInflater;
    private List<NbuCurrency> mCurrencyList;

    public NbuTableAdapter(Context context, List<NbuCurrency> currencyList) {
        mInflater = LayoutInflater.from(context);
        mCurrencyList = currencyList;
    }

    @NonNull
    @Override
    public NbuTableAdapter.NbuCurrencyHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.nbu_currency_item, parent, false);
        return new NbuCurrencyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NbuTableAdapter.NbuCurrencyHolder holder, int position) {
        if (mCurrencyList != null) {
            NbuCurrency currentItem = mCurrencyList.get(position);
            holder.mNbuCurrencyTextCcy.setText(currentItem.getCcyName());
            holder.mNbuCurrencyTextExchangeRate.setText(String.format
                    ("%.2fUAH", currentItem.getRate()));
            holder.mNbuCurrencyTextCcyName.setText(String.format("1 %s", currentItem.getCcy()));
        } else {
            holder.mNbuCurrencyTextExchangeRate.setText("0.00");
            holder.mNbuCurrencyTextCcyName.setText("0.00");
        }
    }

    public void setCurrencyList(List<NbuCurrency> currencyList) {
        mCurrencyList = currencyList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCurrencyList != null) {
            return mCurrencyList.size();
        } else return 0;
    }

    class NbuCurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mNbuCurrencyTextCcy;
        private final TextView mNbuCurrencyTextExchangeRate;
        private final TextView mNbuCurrencyTextCcyName;

        NbuCurrencyHolder(@NonNull View itemView) {
            super(itemView);
            mNbuCurrencyTextCcy = itemView.findViewById(R.id.ccy_nbu);
            mNbuCurrencyTextExchangeRate = itemView.findViewById(R.id.ccy_nbu_exchange_rate);
            mNbuCurrencyTextCcyName = itemView.findViewById(R.id.ccy_nbu_ratio);
        }

        @Override
        public void onClick(View v) {
            v.setBackgroundColor(Color.parseColor("#a1c8ba"));
        }
    }

}
