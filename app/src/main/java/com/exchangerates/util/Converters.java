package com.exchangerates.util;

import android.util.Log;

import com.exchangerates.model.NbuCurrency;
import com.exchangerates.model.PbCurrency;
import com.google.gson.Gson;

import org.json.JSONObject;


/**
 * Convenience methods for data parsing.
 */
public class Converters {

    private static final String TAG = "Converters";

    /**
     * Converts date from DatePicker to string for further HTTP queries.
     *
     * @return converted string
     */
    public static String dateToStringQueryParam(int year, int month, int day) {
        Log.d(TAG, "dateToStringQueryParam: day " + day);
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month);
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day);
        return sb.toString();
    }

    /**
     * Converts values from Calendar instance to date with dots for edit text field.
     *
     * @return converted string
     */
    public static String dateToStringWithDots(int day, int month, int year) {
        Log.d(TAG, "dateToStringQueryParam: day " + day);
        StringBuilder sb = new StringBuilder();
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day).append(".");
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month).append(".");
        sb.append(year);
        return sb.toString();
    }

    public static PbCurrency fromJsonToPbCurrency(JSONObject jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), PbCurrency.class);
    }

    public static NbuCurrency fromJsonToNbuCurrency(JSONObject jsonObject) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), NbuCurrency.class);
    }

}
