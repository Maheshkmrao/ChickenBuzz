package com.android.chickenbuzz.componenets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chickenbuzz.beans.Country;


/**
 * Created by 703145805 on 9/15/2016.
 */
public class CountrySpinnerAdapter extends ArrayAdapter<Country> {
    private LayoutInflater mLayoutInflater;

    public CountrySpinnerAdapter(Context context) {
        super(context, 0);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        CountrySpinnerAdapter.ViewHolder viewHolder;
        if(convertView == null) {
            convertView = this.mLayoutInflater.inflate(net.rimoto.intlphoneinput.R.layout.item_country, parent, false);
            viewHolder = new CountrySpinnerAdapter.ViewHolder();
            viewHolder.mImageView = (ImageView)convertView.findViewById(net.rimoto.intlphoneinput.R.id.intl_phone_edit__country__item_image);
            viewHolder.mNameView = (TextView)convertView.findViewById(net.rimoto.intlphoneinput.R.id.intl_phone_edit__country__item_name);
            viewHolder.mDialCode = (TextView)convertView.findViewById(net.rimoto.intlphoneinput.R.id.intl_phone_edit__country__item_dialcode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CountrySpinnerAdapter.ViewHolder)convertView.getTag();
        }

        Country country = (Country)this.getItem(position);
        viewHolder.mImageView.setImageResource(this.getFlagResource(country));
        viewHolder.mNameView.setText(country.getName());
        viewHolder.mDialCode.setText(String.format("+%s", new Object[]{Integer.valueOf(country.getDialCode())}));
        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Country country = (Country)this.getItem(position);
        if(convertView == null) {
            convertView = new ImageView(this.getContext());
        }

        ((ImageView)convertView).setImageResource(this.getFlagResource(country));
        return (View)convertView;
    }

    private int getFlagResource(Country country) {
        return this.getContext().getResources().getIdentifier("country_" + country.getIso().toLowerCase(), "drawable", this.getContext().getPackageName());
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mDialCode;

        private ViewHolder() {
        }
    }
}

