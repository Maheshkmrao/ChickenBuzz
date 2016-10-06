package com.android.chickenbuzz.componenets;

import android.content.Context;

import com.android.chickenbuzz.beans.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 703145805 on 9/15/2016.
 */
public class CountriesFetcher {
    private static CountriesFetcher.CountryList mCountries;

    public CountriesFetcher() {
    }

    private static String getJsonFromRaw(Context context, int resource) {
        try {
            InputStream ex = context.getResources().openRawResource(resource);
            int size = ex.available();
            byte[] buffer = new byte[size];
            ex.read(buffer);
            ex.close();
            String json = new String(buffer, "UTF-8");
            return json;
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static CountriesFetcher.CountryList getCountries(Context context) {
        if(mCountries != null) {
            return mCountries;
        } else {
            mCountries = new CountriesFetcher.CountryList();

            try {
                JSONArray e = new JSONArray(getJsonFromRaw(context, net.rimoto.intlphoneinput.R.raw.countries));

                for(int i = 0; i < e.length(); ++i) {
                    try {
                        JSONObject e1 = (JSONObject)e.get(i);
                        mCountries.add(new Country(e1.getString("name"), e1.getString("iso2"), e1.getInt("dialCode")));
                    } catch (JSONException var4) {
                        var4.printStackTrace();
                    }
                }
            } catch (JSONException var5) {
                var5.printStackTrace();
            }

            return mCountries;
        }
    }

    public static class CountryList extends ArrayList<Country> {
        public CountryList() {
        }

        public int indexOfIso(String iso) {
            for(int i = 0; i < this.size(); ++i) {
                if(((Country)this.get(i)).getIso().toUpperCase().equals(iso.toUpperCase())) {
                    return i;
                }
            }

            return -1;
        }

        public int indexOfDialCode(int dialCode) {
            for(int i = 0; i < this.size(); ++i) {
                if(((Country)this.get(i)).getDialCode() == dialCode) {
                    return i;
                }
            }

            return -1;
        }
    }
}
