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
import com.exchangerates.model.PbCurrency;

import java.util.List;


public class PbTableAdapter extends RecyclerView.Adapter<PbTableAdapter.PbCurrencyHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final LayoutInflater mInflater;
    private List<PbCurrency> mCurrencyList;
    private OnItemClickListener onItemClickListener;

    public PbTableAdapter(Context context,
                          List<PbCurrency> currencyList,
                          OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        mCurrencyList = currencyList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public PbTableAdapter.PbCurrencyHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.pb_currency_item, parent, false);
        return new PbCurrencyHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PbTableAdapter.PbCurrencyHolder holder, int position) {
        if (mCurrencyList != null) {
            PbCurrency currentItem = mCurrencyList.get(position);
            holder.mPbCurrencyTextCcy.setText(currentItem.getCcy());
            holder.mPbCurrencyTextBuyRate.setText(String.valueOf(currentItem.getBuyRate()));
            holder.mPbCurrencyTextSaleRate.setText(String.valueOf(currentItem.getSaleRate()));
        } else {
            holder.mPbCurrencyTextBuyRate.setText("0.00");
            holder.mPbCurrencyTextSaleRate.setText("0.00");
        }
    }

    public void setCurrencyList(List<PbCurrency> currencyList) {
        mCurrencyList = currencyList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCurrencyList != null) {
            return mCurrencyList.size();
        } else return 0;
    }

    class PbCurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mPbCurrencyTextCcy;
        private final TextView mPbCurrencyTextBuyRate;
        private final TextView mPbCurrencyTextSaleRate;

        private OnItemClickListener onItemClickListener;

        PbCurrencyHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mPbCurrencyTextCcy = itemView.findViewById(R.id.ccy);
            mPbCurrencyTextBuyRate = itemView.findViewById(R.id.ccy_buyrate);
            mPbCurrencyTextSaleRate = itemView.findViewById(R.id.ccy_salerate);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
            v.setBackgroundColor(Color.parseColor("#a1c8ba"));
        }
    }

}
